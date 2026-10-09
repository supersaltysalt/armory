package com.example.armory.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.armory.dto.Cart;
import com.example.armory.dto.CartLine;
import com.example.armory.entity.Product;
import com.example.armory.service.CartService;
import com.example.armory.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final Cart cart;
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping
    public String view(Model model) {
        List<CartLine> lines = cartService.lines();
        model.addAttribute("lines", lines);
        model.addAttribute("total", cartService.total(lines));
        model.addAttribute("canCheckout",
                !lines.isEmpty() && lines.stream().allMatch(CartLine::isInStock));
        return "cart/cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam int productId,
                      @RequestParam(defaultValue = "1") int quantity,
                      RedirectAttributes ra) {
        Product p = productService.get(productId);       // 存在しなければ 404
        int add = Math.max(1, quantity);
        int current = cart.getQuantity(productId);
        int next = Math.min(current + add, p.getStock()); // 在庫を超えない

        if (next <= current) {
            ra.addFlashAttribute("error",
                    "「" + p.getName() + "」はこれ以上追加できません（在庫：" + p.getStock() + "）");
        } else {
            cart.set(productId, next);
            ra.addFlashAttribute("message",
                    next < current + add
                    ? "在庫の上限（" + p.getStock() + "個）までカートに入れました"
                    : "「" + p.getName() + "」をカートに入れました");
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam int productId,
                         @RequestParam int quantity,
                         RedirectAttributes ra) {
        Product p = productService.get(productId);
        int next = Math.min(Math.max(quantity, 0), p.getStock());
        cart.set(productId, next);                        // 0 なら削除される
        if (quantity > p.getStock()) {
            ra.addFlashAttribute("error", "在庫は" + p.getStock() + "個です。数量を調整しました");
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam int productId) {
        cart.remove(productId);
        return "redirect:/cart";
    }
}