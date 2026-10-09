package com.example.armory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //このクラスには設定が記されていますよとSpringに示している
public class SecurityConfig { //セキュリティ設定クラス

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	//SecurityFilterChain→Webアクセスチェックするセキュリティの仕組み
    	//HttpSecurity http→HTTP通信に対するセキュリティ設定を行うオブジェクト
        http.authorizeHttpRequests(auth -> auth //ここでHTTPリクエストごとにアクセス許可を設定
                .requestMatchers("/", "/products/**", "/login", "/register",
                                 "/css/**", "/images/**", "/uploads/**","/error").permitAll() //permit=許可する ALL全員
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN持ってるやつだけ
                .anyRequest().authenticated()) //ここまでのルールに該当しないURL→ログイン済みユーザーだけ許可
            .formLogin(form -> form
            		.loginPage("/login")
            		.defaultSuccessUrl("/",false)
            		.permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { //DBへ安全にPWを登録するためここでハッシュ化して登録している
        return new BCryptPasswordEncoder();
        //Beanをつけることで、BC～をSpring管理の部品として登録しておいてと命令している
    }
}