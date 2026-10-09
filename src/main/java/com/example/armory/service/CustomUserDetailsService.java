package com.example.armory.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.armory.entity.User;
import com.example.armory.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

//このコードはDBのUserをSpringSecurityのUserDetalsに変換するための橋

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{ 
	//UserDetailsService=SpringSecurityが用意しているインターフェース
	private final UserMapper userMapper; //DBからユーザーを検索するためのマッパー
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		User u = userMapper.findByEmail(email.trim().toLowerCase());
		//右でメアドを使ってDBに情報を検索する
		//左でDBの検索情報をJavaオブジェクトとして受け取っている
		if(u == null) {
			throw new UsernameNotFoundException("会員が見つかりません");//throw 異常を検知→例外を発生させる
		}
		return org.springframework.security.core.userdetails.User
				//Userエンティティとはまた別のUser
				//Spring Securitが認証に使うユーザー情報
				//DB用のユーザーからSpring Securityが理解できるUserDetailsに変換している
				.withUsername(u.getEmail())//SpringSecurity上のユーザー名としてメアドを設定する
				.password(u.getPassword())//DBから取得したPWをSpringSecurityに渡す
				.authorities(u.getRole())//このユーザーが持っている権限をSpringSecurityに教える
				.build(); //上記3つで設定した内容からSpringSecurity用のUserDetailオブジェクトを完成させる
	}
	//SpringSecurityでパスワードを照合

}
