package com.example.armory.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.armory.dto.ProductForm;
import com.example.armory.entity.Product;
import com.example.armory.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductMapper productMapper;
    private final ImageStorageService imageStorage;

    @Transactional
    public void create(ProductForm form) {
        Product p = toEntity(form);
        p.setImagePath(storeImage(form.getImage()));
        productMapper.insert(p);
    }

    @Transactional
    public void update(ProductForm form) {
        if (productMapper.findById(form.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商品が見つかりません");
        }
        Product p = toEntity(form);
        p.setId(form.getId());
        p.setImagePath(storeImage(form.getImage()));   // 画像の指定がなければ null（変更しない）
        productMapper.update(p);
    }

    @Transactional
    public void delete(int id) {
        productMapper.logicalDelete(id);
    }

    /** 編集画面用に、商品をフォームの形で返す */
    public ProductForm getForm(int id) {
        Product p = productMapper.findById(id);
        if (p == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商品が見つかりません");
        }
        ProductForm f = new ProductForm();
        f.setId(p.getId());
        f.setCategoryId(p.getCategoryId());
        f.setName(p.getName());
        f.setDescription(p.getDescription());
        f.setPrice(p.getPrice());
        f.setStock(p.getStock());
        f.setImagePath(p.getImagePath());
        return f;
    }

    private Product toEntity(ProductForm f) {
        Product p = new Product();
        p.setCategoryId(f.getCategoryId());
        p.setName(f.getName().trim());
        p.setDescription(f.getDescription());
        p.setPrice(f.getPrice());
        p.setStock(f.getStock());
        return p;
    }

    private String storeImage(MultipartFile file) {
        return (file == null || file.isEmpty()) ? null : imageStorage.save(file);
    }
}