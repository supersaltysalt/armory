package com.example.armory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartLine {
	private int productId;
	private String name;
	private int price;
	private int quantity;
	private int stock;
	private String imagePath;
	
	public int getSubtotal() {
		return price * quantity;
	}
	public boolean isInStock() {
		return stock >= quantity;
	}

}
