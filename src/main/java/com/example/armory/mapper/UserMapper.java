package com.example.armory.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.armory.entity.User;

@Mapper
public interface UserMapper {
	User findByEmail(String email);
	boolean existsByEmail(String oemail);
	void insert(User user);
}
