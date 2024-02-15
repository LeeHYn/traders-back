package com.traders.tradersback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    private final String uploadDir = "C:/traders/upload/directory"; // 실제 파일 시스템의 경로로 변경

    @Value("${server.host:127.0.0.1}")
    private String serverHost;

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = Objects.requireNonNull(originalFilename)
                .substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + fileExtension; // 고유 식별자를 사용한 파일명

        File savePath = new File(uploadDir, filename);

        if (!savePath.getParentFile().exists()) {
            savePath.getParentFile().mkdirs();
        }

        file.transferTo(savePath);

        return "http://" + serverHost + "/images/" + filename;
    }

}
