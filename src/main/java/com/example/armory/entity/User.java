package com.example.armory.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
private Integer id;
private String name;
private String email;
private String password;
private String role;
private LocalDateTime createdAt;
}
