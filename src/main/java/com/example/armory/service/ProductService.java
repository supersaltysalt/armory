package com.example.armory.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.armory.entity.Category;
import com.example.armory.entity.Product;
import com.example.armory.mapper.CategoryMapper;
import com.example.armory.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductMapper productMapper;
	private final CategoryMapper categoryMapper;
	
	public List<Product> search(Integer categoryId,String keyword){
		return productMapper.search(categoryId,keyword);
	}
	public Product get(int id) {
		Product p = productMapper.findById(id);
		if(p == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"商品が見つかりません");
		}
		return p;
		}
	public List<Category> getCategories(){
		return categoryMapper.findAll();
	}
}
