package com.example.armory.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    /** 許可する画像の種類 → 拡張子（SVG は、中にスクリプトを入れられるため許可しない） */
    private static final Map<String, String> ALLOWED = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/gif", ".gif",
            "image/webp", ".webp");

    private final Path root;

    public ImageStorageService(@Value("${app.upload-dir:./uploads}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /** 画像を保存し、画面から参照するパス（例：/uploads/xxxx.jpg）を返す */
    public String save(MultipartFile file) {
        String ext = ALLOWED.get(file.getContentType());
        if (ext == null) {
            throw new IllegalArgumentException("画像は JPEG・PNG・GIF・WebP のみ登録できます");
        }
        // ファイル名は、利用者が付けた名前を使わず、ランダムな名前にする（不正なパスの指定を防ぐ）
        String filename = UUID.randomUUID() + ext;
        try {
            Files.createDirectories(root);
            Files.copy(file.getInputStream(), root.resolve(filename));
        } catch (IOException e) {
            throw new UncheckedIOException("画像の保存に失敗しました", e);
        }
        return "/uploads/" + filename;
    }
}