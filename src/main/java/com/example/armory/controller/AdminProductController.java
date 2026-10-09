package com.example.armory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.armory.dto.ProductForm;
import com.example.armory.service.AdminProductService;
import com.example.armory.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminService;
    private final ProductService productService;

    /** 商品一覧（管理用） */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.search(null, null));
        return "admin/products/list";
    }

    /** 新規登録フォーム */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return formView(model, "/admin/products", "商品の登録");
    }

    /** 新規登録 */
    @PostMapping
    public String create(@Validated @ModelAttribute("productForm") ProductForm form,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return formView(model, "/admin/products", "商品の登録");
        }
        try {
            adminService.create(form);
        } catch (IllegalArgumentException e) {
            result.rejectValue("image", "invalid", e.getMessage());
            return formView(model, "/admin/products", "商品の登録");
        }
        ra.addFlashAttribute("message", "商品を登録しました");
        return "redirect:/admin/products";
    }

    /** 編集フォーム */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("productForm", adminService.getForm(id));
        return formView(model, "/admin/products/" + id, "商品の編集");
    }

    /** 更新 */
    @PostMapping("/{id}")
    public String update(@PathVariable int id,
                         @Validated @ModelAttribute("productForm") ProductForm form,
                         BindingResult result, Model model, RedirectAttributes ra) {
        form.setId(id);                              // URL の id を正とする
        if (result.hasErrors()) {
            return formView(model, "/admin/products/" + id, "商品の編集");
        }
        try {
            adminService.update(form);
        } catch (IllegalArgumentException e) {
            result.rejectValue("image", "invalid", e.getMessage());
            return formView(model, "/admin/products/" + id, "商品の編集");
        }
        ra.addFlashAttribute("message", "商品を更新しました");
        return "redirect:/admin/products";
    }

    /** 削除（論理削除） */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        adminService.delete(id);
        ra.addFlashAttribute("message", "商品を削除しました");
        return "redirect:/admin/products";
    }

    private String formView(Model model, String action, String title) {
        model.addAttribute("categories", productService.getCategories());
        model.addAttribute("action", action);
        model.addAttribute("title", title);
        return "admin/products/form";
    }
}