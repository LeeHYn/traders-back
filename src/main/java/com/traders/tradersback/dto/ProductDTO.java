package com.traders.tradersback.dto;

import com.traders.tradersback.model.Product;
import com.traders.tradersback.model.ProductImage;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    private Long productNum;
    private String memberId;
    private Long mainCategoryNum;
    private String productName;
    private Integer price;
    private String productDescription;
    private String productCondition;
    private String productStatus;
    private String mainCategoryName;
    private LocalDateTime createdAt;

    private List<MultipartFile> imageFiles;
    private List<String> imageUrls; // 이미지 URL 목록

    // 기본 생성자 추가
    public ProductDTO() {
    }

    // 추가된 생성자: Product 엔티티와 ProductImage 목록을 기반으로 DTO 생성
    public ProductDTO(Product product, List<ProductImage> images) {
        this.memberId = product.getMemberNum().toString(); // 필요에 따라 memberId 설정
        this.mainCategoryNum = product.getMainCategoryNum();
        this.productName = product.getProductName();
        this.productNum = product.getProductNum();
        this.price = product.getPrice();
        this.productDescription = product.getProductDescription();
        this.productCondition = product.getProductCondition();
        this.productStatus = product.getProductStatus();
        this.createdAt = product.getCreatedAt();

        this.imageUrls = images.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }
    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Long getMainCategoryNum() {
        return mainCategoryNum;
    }

    public void setMainCategoryNum(Long mainCategoryNum) {
        this.mainCategoryNum = mainCategoryNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
