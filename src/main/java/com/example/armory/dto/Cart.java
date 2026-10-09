package com.example.armory.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component //このクラスをSpringに管理してもらうための目印
@SessionScope //ユーザーのセッションごとにカートをひとつ持つ（ユーザーAとBの買い物籠が混ざらない）
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L; //Serializableを使うための定型的な前準備みたいなもん

    /** 商品ID → 数量 */
    private final Map<Integer, Integer> items = new LinkedHashMap<>();
    //第一引数→商品ID　第二引数→その商品の数量

    public int getQuantity(int productId) { //指定した商品がカートに何個入っているか精げるメソッド
        return items.getOrDefault(productId, 0);//商品が存在すればその数量を返し存在しなければ0を返す
    }

    /** 数量を設定する。0以下なら削除 */
    public void set(int productId, int quantity) {
        if (quantity <= 0) {
            items.remove(productId);
        } else {
            items.put(productId, quantity);
        }
    }

    public void remove(int productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** 中身のコピーを返す（カート内部のデータを外から直接書き換えられないように） */
    public Map<Integer, Integer> snapshot() {
        return new LinkedHashMap<>(items);
    }
}