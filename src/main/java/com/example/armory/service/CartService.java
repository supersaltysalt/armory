package com.example.armory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.armory.dto.Cart;
import com.example.armory.dto.CartLine;
import com.example.armory.entity.Product;
import com.example.armory.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final Cart cart;
    private final ProductMapper productMapper;

    /** カートの中身を、最新の商品情報つきで返す */
    public List<CartLine> lines() {
        List<CartLine> lines = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : cart.snapshot().entrySet()) {
            Product p = productMapper.findById(e.getKey());
            if (p == null) {
                cart.remove(e.getKey());   // 販売終了（削除）された商品は、カートから外す
                continue;
            }
            lines.add(new CartLine(p.getId(), p.getName(), p.getPrice(),
                                   e.getValue(), p.getStock(), p.getImagePath()));
        }
        return lines;
    }

    public int total(List<CartLine> lines) {
        return lines.stream().mapToInt(CartLine::getSubtotal).sum();
    }
}