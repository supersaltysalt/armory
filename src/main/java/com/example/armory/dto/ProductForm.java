package com.example.armory.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductForm {

    private Integer id;                        // 新規登録のときは null

    @NotNull(message = "カテゴリーを選択してください")
    private Integer categoryId;

    @NotBlank(message = "商品名を入力してください")
    @Size(max = 100, message = "商品名は100文字以内で入力してください")
    private String name;

    @Size(max = 2000, message = "説明は2000文字以内で入力してください")
    private String description;

    @NotNull(message = "価格を入力してください")
    @Min(value = 0, message = "価格は0以上で入力してください")
    @Max(value = 100000000, message = "価格は1億円以下で入力してください")
    private Integer price;

    @NotNull(message = "在庫数を入力してください")
    @Min(value = 0, message = "在庫数は0以上で入力してください")
    @Max(value = 100000, message = "在庫数は10万以下で入力してください")
    private Integer stock;

    private MultipartFile image;               // アップロードされた画像（任意）

    private String imagePath;                  // 現在の画像（編集画面での表示用）
}