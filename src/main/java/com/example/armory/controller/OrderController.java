package com.example.armory.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.armory.dto.Cart;
import com.example.armory.dto.CartLine;
import com.example.armory.service.CartService;
import com.example.armory.service.OrderException;
import com.example.armory.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final Cart cart;
    private final CartService cartService;
    private final OrderService orderService;

    /** 購入内容の確認画面 */
    @GetMapping("/confirm")
    public String confirm(Model model, RedirectAttributes ra) {
        List<CartLine> lines = cartService.lines();
        if (lines.isEmpty()) {
            ra.addFlashAttribute("error", "カートが空です");
            return "redirect:/cart";
        }
        if (!lines.stream().allMatch(CartLine::isInStock)) {
            ra.addFlashAttribute("error", "在庫が不足している商品があります。数量を調整してください");
            return "redirect:/cart";
        }
        model.addAttribute("lines", lines);
        model.addAttribute("total", cartService.total(lines));
        return "order/confirm";
    }

    /** 購入確定 */
    @PostMapping("/complete")
    public String place(Principal principal, RedirectAttributes ra) {
        try {
            int orderId = orderService.place(principal.getName(), cart.snapshot());
            cart.clear();
            ra.addFlashAttribute("orderId", orderId);
            return "redirect:/order/complete";      // 画面更新による二重購入を防ぐ
        } catch (OrderException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    /** 購入完了画面 */
    @GetMapping("/complete")
    public String complete(Model model) {
        if (!model.containsAttribute("orderId")) {
            return "redirect:/";                    // 直接開いたときは商品一覧へ
        }
        return "order/complete";
    }
}