package com.traders.tradersback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ImageService {
    private final String uploadDir = "C:/traders/upload/directory"; // 실제 파일 시스템의 경로로 변경

    @Value("${server.host:127.0.0.1}")
    private String serverHost;

    public String saveImage(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            File savePath = new File(uploadDir, filename);

            // 디렉토리가 존재하지 않으면 생성
            if (!savePath.getParentFile().exists()) {
                savePath.getParentFile().mkdirs();
            }

            // 파일 저장
            file.transferTo(savePath);

            // URL 생성 및 반환
            String imageUrl = "http://" + serverHost + "/images/" + filename;
            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
