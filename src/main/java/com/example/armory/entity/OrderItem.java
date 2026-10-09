package com.example.armory.entity;

import lombok.Data;

@Data
public class OrderItem {
	private Integer id;
	private Integer orderId;
	private Integer productId;
	private String productName;
	private Integer unitPrice;
	private Integer quantity;
}
