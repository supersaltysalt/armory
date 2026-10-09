package com.example.armory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.armory.entity.Category;

@Mapper
public interface CategoryMapper {
	List<Category> findAll();
	
}
