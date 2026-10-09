package com.example.armory.entity;

import lombok.Data;

@Data
public class Product {
	private Integer id;
	private Integer categoryId;
	private String categoryName;
	private String name;
	private String description;
	private Integer price;
	private Integer stock;
	private String imagePath;
	}
