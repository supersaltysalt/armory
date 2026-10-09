package com.example.armory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.armory.entity.Product;

//このインターフェイスはProduct表に対して、どんな操作をするかの一覧表のようなもの
//インターフェイスなので当然処理の中身は記されていない
@Mapper //このインターフェイスはMyBatisが引き受けるという目印
		//これによりMBが自動で実装を用意し、SpringがBeanとしてコンテナに登録する
		//MapperはいわばJavaからSQLを呼び出すための窓口として機能する
public interface ProductMapper {
	List<Product> search(@Param("categoryId") Integer categoryId,@Param("keyword") String keyword);
	//商品が複数あるのでリストで返している
	Product findById(int id); //IDを指定して商品を１件取り出す
	
	void insert(Product p); //商品の登録
	void update(Product p); //商品情報の更新
	void logicalDelete(int id);	 //論理削除(行そのものを消さず「削除したことにする」)
	int decreaseStock(@Param("id") int id,@Param("qty") int qty);
	//在庫の減算（購入された数だけ在庫を減らす）
}

/*
 * @Paramについて
 * 引数が2つ以上ある場合、MBは引数の名前を知ることができない
 * そのため便宜上このアノテーションを用いて引数に名前をつける必要がある
 */
