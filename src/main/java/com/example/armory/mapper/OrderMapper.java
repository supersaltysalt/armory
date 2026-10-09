package com.example.armory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.armory.entity.Order;
import com.example.armory.entity.OrderItem;

@Mapper //注文1件につき複数商品が対応する1対多の関係
public interface OrderMapper {
	void insertOrder(Order order);
	void insertItem(OrderItem item);
	List<Order> findByUserId(int userId);
	Order findByIdAndUserId(@Param("id") int id,@Param("userId") int userId);
}
