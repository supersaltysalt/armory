package com.example.armory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.armory.entity.Order;
import com.example.armory.entity.OrderItem;
import com.example.armory.entity.Product;
import com.example.armory.entity.User;
import com.example.armory.mapper.OrderMapper;
import com.example.armory.mapper.ProductMapper;
import com.example.armory.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final UserMapper userMapper;
	private final ProductMapper productMapper;
	private final OrderMapper orderMapper;

	/**
	 * 購入を確定する。途中で例外が出たら、在庫の減算も注文の保存もすべて取り消される。
	 * @param email 購入する会員のメールアドレス
	 * @param items 商品ID → 数量
	 * @return 注文ID
	 */
	@Transactional
	public int place(String email, Map<Integer, Integer> items) {
		User user = userMapper.findByEmail(email);
		if (user == null) {
			throw new OrderException("会員情報が見つかりません");
		}
		if (items.isEmpty()) {
			throw new OrderException("カートが空です");
		}

		// 商品IDの昇順で処理する（複数人が同時に購入したときの、行ロックの取り合い＝デッドロックを防ぐ）
		List<OrderItem> orderItems = new ArrayList<>();
		int total = 0;
		for (Map.Entry<Integer, Integer> e : new TreeMap<>(items).entrySet()) {
			Product p = productMapper.findById(e.getKey());   // 最新の名前・価格を DB から読む
			if (p == null) {
				throw new OrderException("販売が終了した商品が含まれています");
			}
			int qty = e.getValue();

			// 在庫が足りるときだけ減る。0件更新なら在庫不足
			if (productMapper.decreaseStock(p.getId(), qty) == 0) {
				throw new OrderException("「" + p.getName() + "」の在庫が不足しています");
			}

			OrderItem item = new OrderItem();
			item.setProductId(p.getId());
			item.setProductName(p.getName());   // 購入時点の名前と単価を保存
			item.setUnitPrice(p.getPrice());
			item.setQuantity(qty);
			orderItems.add(item);
			total += p.getPrice() * qty;
		}

		Order order = new Order();
		order.setUserId(user.getId());
		order.setTotalPrice(total);
		orderMapper.insertOrder(order);                   // order.id に採番された値が入る

		for (OrderItem item : orderItems) {
			item.setOrderId(order.getId());
			orderMapper.insertItem(item);
		}
		return order.getId();
	}
	@Transactional(readOnly = true)
	public List<Order> history(String email){
		return orderMapper.findByUserId(requireUser(email).getId());
	}
	@Transactional(readOnly = true)
	public Order detail(String email,int orderId) {
		Order order = orderMapper.findByIdAndUserId(orderId,requireUser(email).getId());
		if(order == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"注文が見つかりません");
		}
		return order;
	}
	private User requireUser(String email) {
		User user = userMapper.findByEmail(email);
		if(user == null) {
			throw new OrderException("会員情報が見つかりません");
		}
		return user;
	}
}