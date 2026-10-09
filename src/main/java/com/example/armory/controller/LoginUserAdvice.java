package com.example.armory.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.armory.dto.Cart;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class LoginUserAdvice {

    private final Cart cart;

    @ModelAttribute("loginName")
    public String loginName(Authentication auth) {
        return isLoggedIn(auth) ? auth.getName() : null;
    }

    /** ヘッダーのカート数量。未ログインなら 0 */
    @ModelAttribute("cartCount")
    public int cartCount(Authentication auth) {
        return isLoggedIn(auth) ? cart.getTotalQuantity() : 0;
    }

    private boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }
    /** 管理者かどうか（ヘッダーの表示切り替え用） */
    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        return isLoggedIn(auth) && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}