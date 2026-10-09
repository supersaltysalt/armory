package com.example.armory.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.armory.dto.RegisterForm;
import com.example.armory.entity.User;
import com.example.armory.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	public boolean existsByEmail(String email) {
		return userMapper.existsByEmail(normalize(email));
	}
	
	@Transactional
	public void register(RegisterForm form) {
		User user = new User();
		user.setName(form.getName().trim());
		user.setEmail(normalize(form.getEmail()));
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setRole("ROLE_USER");
		userMapper.insert(user);
	}
	static String normalize(String email) {
		return email.trim().toLowerCase();
}
}
