package com.example.armory.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.armory.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderHistoryController {
	private final OrderService orderService;
	
	//注文履歴の一覧
	@GetMapping
	public String list(Principal principal,Model model) {
		model.addAttribute("orders",orderService.history(principal.getName()));
		return "orders/list";
	}
	//注文の詳細
	@GetMapping("/{id}")
	public String detail(@PathVariable int id,Principal principal,Model model) { //@Pathvariable URLの一部を変数として受け取る
		model.addAttribute("order",orderService.detail(principal.getName(), id));
		return "orders/detail";
	}
	/*
	 * Principal（主体という抽象的な概念を指す）
	 * javaでユーザーやエンティティを表すインターフェースで、認証やセキュリティ管理に利用
	 * Spring BootやSpring Securityではこれを使ってログインユーザー情報を取得できる
	 * 今回の場合だと、ログイン中の会員のメールアドレスを返している
	 * まあとにかく、ログイン中のユーザーを表すJava標準のインターフェースのこと
	 * 標準インターフェースなので事前準備なく使用可（自分でnewする必要はない）
	 * Principalを引数で受け取れるのはコントローラーのメソッドだけ
	 * 
	 * 仕組みの流れ
	 * ログイン画面でメールとパスワードを送信
	 * Spring SecurityがCustomUserDetailServiceで会員を探し、パスワードを照合
	 * 成功すると、「この人はログイン済み」という情報（Autehntication）をセッションに保存する
	 * 次のリクエストからSpringMVCが自動でコントローラーのPrincipal引き数にその情報を渡してくれる
	 */
}
