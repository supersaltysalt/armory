package com.example.armory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterForm {

    @NotBlank(message = "名前を入力してください")
    @Size(max = 50, message = "名前は50文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 50, message = "パスワードは8文字以上50文字以内で入力してください")
    private String password;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String passwordConfirm;
}