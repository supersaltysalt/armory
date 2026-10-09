package com.example.armory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.armory.service.ProductService;

import lombok.RequiredArgsConstructor;

/*このクラスはコントローラー
 * Controllerは「URLを受け取る → Serviceにお願いする → Modelに結果を詰める → HTMLを返す」という橋渡し役
 */

@Controller //このクラスはWebリクエストを処理しますよとSpringに伝える
			//例：ブラウザ「GET /products」→Springが@Controllerを探す→@GetMappingに従いページを返す
@RequiredArgsConstructor //finalフィールドのコンストラクタを自動生成（LOMBOK）
public class ProductController {

    private final ProductService productService;
    /*
     * コントローラーの役割は「リクエストを受け取って必要な処理をサービスにお願いする」こと
     * サービスに何か渡さなあかんのでフィールドにこれが設定される*/

    //商品一覧
    @GetMapping({"/", "/products"})
    public String list(@RequestParam(required = false) Integer categoryId,
                       @RequestParam(required = false) String keyword,
                       Model model) { //このモデルはコントローラーからHTML（VIEW）へデータを渡す入れ物
        model.addAttribute("products", productService.search(categoryId, keyword));
        //modelに"products"という名前で第二引数の結果が保存される
        //"products"はHTML側で使う名前として利用される（HTML側：${products}）
        model.addAttribute("categories", productService.getCategories());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);
        return "products/list";//templates/products/list.htmlがブラウザに表示される
    }

    //商品詳細
    @GetMapping("/products/{id}")
    public String detail(@PathVariable int id, Model model) {
    	//@PathValiable→URLに埋め込まれている{id}の値をJavaの変数int idに取り出す
        model.addAttribute("product", productService.get(id));
        return "products/detail";
    }
}