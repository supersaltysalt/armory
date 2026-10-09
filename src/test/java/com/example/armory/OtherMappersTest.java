package com.example.armory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.armory.entity.Order;
import com.example.armory.entity.OrderItem;
import com.example.armory.entity.User;
import com.example.armory.mapper.CategoryMapper;
import com.example.armory.mapper.OrderMapper;
import com.example.armory.mapper.UserMapper;

@SpringBootTest
@Transactional
class OtherMappersTest {

    @Autowired CategoryMapper categoryMapper;
    @Autowired UserMapper userMapper;
    @Autowired OrderMapper orderMapper;

    private User newUser() {
        User u = new User();
        u.setName("テスト太郎");
        u.setEmail("test@example.com");
        u.setPassword("dummy-hash");
        u.setRole("ROLE_USER");
        userMapper.insert(u);
        return u;
    }

    @Test
    void カテゴリーが全件取得できる() {
        assertEquals(3, categoryMapper.findAll().size());   // data.sql の3件
    }

    @Test
    void 会員の登録と検索ができる() {
        assertFalse(userMapper.existsByEmail("test@example.com"));
        User u = newUser();
        assertNotNull(u.getId());                            // 採番された id が戻っている
        assertTrue(userMapper.existsByEmail("test@example.com"));
        assertEquals("テスト太郎", userMapper.findByEmail("test@example.com").getName());
        assertNull(userMapper.findByEmail("nobody@example.com"));
    }

    @Test
    void 注文の保存と履歴の取得ができる() {
        User u = newUser();

        Order o = new Order();
        o.setUserId(u.getId());
        o.setTotalPrice(25600);
        orderMapper.insertOrder(o);

        OrderItem i = new OrderItem();
        i.setOrderId(o.getId());
        i.setProductId(2);                                   // 月影のダガー
        i.setProductName("月影のダガー");
        i.setUnitPrice(12800);
        i.setQuantity(2);
        orderMapper.insertItem(i);

        List<Order> list = orderMapper.findByUserId(u.getId());
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getItems().size());      // 明細がまとまって入っている
        assertEquals("月影のダガー", list.get(0).getItems().get(0).getProductName());

        assertNotNull(orderMapper.findByIdAndUserId(o.getId(), u.getId()));
        assertNull(orderMapper.findByIdAndUserId(o.getId(), u.getId() + 999)); // 他人は見えない
    }
}