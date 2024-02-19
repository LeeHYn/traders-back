package com.traders.tradersback.controller;

import com.traders.tradersback.dto.PriceTrendDTO;
import com.traders.tradersback.dto.ProductDTO;
import com.traders.tradersback.dto.ProductStatusUpdateDTO;
import com.traders.tradersback.model.Product;
import com.traders.tradersback.model.ProductImage;
import com.traders.tradersback.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(
            @RequestParam("memberId") String memberId,
            @RequestParam("mainCategoryNum") Long mainCategoryNum,
            @RequestParam("productName") String productName,
            @RequestParam("price") int price,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productCondition") String productCondition,
            @RequestParam("productStatus") String productStatus,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setMemberId(memberId);
        productDTO.setMainCategoryNum(mainCategoryNum);
        productDTO.setProductName(productName);
        productDTO.setPrice(price);
        productDTO.setProductDescription(productDescription);
        productDTO.setProductCondition(productCondition);
        productDTO.setProductStatus(productStatus);
        productDTO.setImageFiles(imageFiles);

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
            List<ProductDTO> products = productService.getProductsInLatestOrder();
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving latest products: " + ex.getMessage());
        }
    }

    // 사용자의 최근 검색어 기반 제품 검색: 사용자의 최근 검색어와 일치하는 제품을 검색합니다.
    @GetMapping("/search/recent/{memberNum}")
    public ResponseEntity<?> getProductsBasedOnRecentSearch(@PathVariable Long memberNum) {
        try {
            List<ProductDTO> products = productService.getProductsBasedOnRecentSearch(memberNum);
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error searching products: " + ex.getMessage());
        }
    }

    // 최근 거래량이 가장 많은 물품 가져오기: 최근 거래량이 많은 메인 카테고리의 물품을 반환합니다.
    @GetMapping("/top-category-products")
    public ResponseEntity<?> getTopProductsInMainCategory() {
        try {
            List<ProductDTO> products = productService.getTopProductsInMainCategory();
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
            ProductDTO productDTO = productService.getProductById(productId);
            return ResponseEntity.ok(productDTO);
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
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long mainCategoryNum) {
        List<Product> products = productService.getProductsByMainCategory(mainCategoryNum);
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Product product : products) {
            List<ProductImage> images = productService.getProductImages(product.getProductNum());
            productDTOs.add(new ProductDTO(product, images));
        }

        return ResponseEntity.ok(productDTOs);
    }


    // 최근 거래
    @GetMapping("/price-trend/{productName}")
    public ResponseEntity<?> getProductPriceTrendWithRecentPrices(@PathVariable String productName) {
        try {
            PriceTrendDTO priceTrendDTO = productService.getProductPriceTrendWithRecentPrices(productName);
            return ResponseEntity.ok(priceTrendDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product price trend: " + ex.getMessage());
        }
    }
    // 검색어로 카테고리 또는 제품 이름 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String query, @RequestParam Optional<Long> memberNum) {
        try {
            List<ProductDTO> products = productService.searchProducts(query, memberNum);
            return ResponseEntity.ok(products);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching products: " + ex.getMessage());
        }
    }
    @GetMapping("/similar/{productNum}")
    public ResponseEntity<List<ProductDTO>> findSimilarProducts(@PathVariable Long productNum) {
        try {
            List<ProductDTO> productsDTO = productService.findProductsInSameCategory(productNum);
            return ResponseEntity.ok(productsDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
