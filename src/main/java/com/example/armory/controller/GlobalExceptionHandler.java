package com.example.armory.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /** 画像が大きすぎるとき：商品管理に戻して理由を表示する */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleUploadSize() {
        return "redirect:/admin/products?sizeError";
    }
}