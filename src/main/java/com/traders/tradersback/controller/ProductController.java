package com.traders.tradersback.controller;

import com.traders.tradersback.dto.ProductDTO;
import com.traders.tradersback.dto.ProductStatusUpdateDTO;
import com.traders.tradersback.model.Product;
import com.traders.tradersback.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // 물품 추가: 사용자로부터 받은 ProductDTO를 사용하여 새로운 물품을 추가합니다.
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product product = productService.addProduct(productDTO);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding product: " + ex.getMessage());
        }
    }

    // 최신순으로 물품 가져오기: 데이터베이스에서 최신순으로 정렬된 물품 목록을 반환합니다.
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestProducts() {
        try {
            List<Product> products = productService.getProductsInLatestOrder();
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving latest products: " + ex.getMessage());
        }
    }

    // 사용자의 최근 검색어 기반 제품 검색: 사용자의 최근 검색어와 일치하는 제품을 검색합니다.
    @GetMapping("/search/recent/{memberNum}")
    public ResponseEntity<?> getProductsBasedOnRecentSearch(@PathVariable Long memberNum) {
        try {
            List<Product> products = productService.getProductsBasedOnRecentSearch(memberNum);
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error searching products: " + ex.getMessage());
        }
    }

    // 최근 거래량이 가장 많은 물품 가져오기: 최근 거래량이 많은 메인 카테고리의 물품을 반환합니다.
    @GetMapping("/top-category-products")
    public ResponseEntity<?> getTopProductsInMainCategory() {
        try {
            List<Product> products = productService.getTopProductsInMainCategory();
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving top category products: " + ex.getMessage());
        }
    }

    // 물품의 가격 추이 분석: 특정 물품의 지난 3달간의 평균 가격 추이를 분석하여 반환합니다.
    @GetMapping("/price-trend")
    public ResponseEntity<?> getProductPriceTrend(@RequestParam String productName) {
        try {
            Map<String, Double> priceTrend = productService.getProductPriceTrend(productName);
            return ResponseEntity.ok(priceTrend);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product price trend: " + ex.getMessage());
        }
    }

    // 특정 물품의 정보를 가져오는 엔드포인트
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product: " + ex.getMessage());
        }
    }

    //물품 상태 병경을 위한 엔드포인트
    @PostMapping("/update-status")
    public ResponseEntity<?> updateProductStatus(@RequestBody ProductStatusUpdateDTO statusUpdateDTO) {
        try {
            Product updatedProduct = productService.updateProductStatus(statusUpdateDTO.getProductId(), statusUpdateDTO.getNewStatus());
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product status: " + ex.getMessage());
        }
    }
    //메인 카테고리의 제품들을 가져오는 엔드포인트
    @GetMapping("/category/{mainCategoryNum}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long mainCategoryNum) {
        List<Product> products = productService.getProductsByMainCategory(mainCategoryNum);
        return ResponseEntity.ok(products);
    }
}
