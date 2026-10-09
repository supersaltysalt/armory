package com.example.armory.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.armory.dto.RegisterForm;
import com.example.armory.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute RegisterForm form,
                           BindingResult result) {
        // 1. 入力チェック（項目ごとの規則）
        // 2. パスワードの一致チェック
        if (!result.hasFieldErrors("password") && !result.hasFieldErrors("passwordConfirm")
                && !form.getPassword().equals(form.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "mismatch", "パスワードが一致しません");
        }
        // 3. メールの重複チェック
        if (!result.hasFieldErrors("email") && userService.existsByEmail(form.getEmail())) {
            result.rejectValue("email", "duplicate", "このメールアドレスは登録済みです");
        }
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.register(form);
        } catch (DuplicateKeyException e) {
            // 同時に同じメールで登録された場合の最終防衛線（DB の UNIQUE 制約）
            result.rejectValue("email", "duplicate", "このメールアドレスは登録済みです");
            return "auth/register";
        }
        return "redirect:/login?registered";
    }
}