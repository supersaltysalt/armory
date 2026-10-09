package com.example.armory.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Order {
	private Integer id;
	private Integer userId;
	private Integer totalPrice;
	private String status;
	private LocalDateTime orderedAt;
	private List<OrderItem> items;//注文明細（このテーブルの列ではない）
}
