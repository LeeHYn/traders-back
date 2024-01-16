package com.traders.tradersback.service;

import com.traders.tradersback.dto.ProductDTO;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.model.Product;
import com.traders.tradersback.model.ProductImage;
import com.traders.tradersback.model.SearchHistory;
import com.traders.tradersback.repository.MemberRepository;
import com.traders.tradersback.repository.ProductRepository;
import com.traders.tradersback.repository.SearchHistoryRepository;
import com.traders.tradersback.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MemberRepository memberRepository;
    // 새로운 물품을 데이터베이스에 추가하는 메소드
    public Product addProduct(ProductDTO productDTO) {
        if (productDTO.getImageUrls() == null || productDTO.getImageUrls().isEmpty()) {
        throw new IllegalArgumentException("최소 하나의 이미지가 필요합니다");
    }

        try {
            Member member = memberRepository.findByMemberId(productDTO.getMemberId());
            if (member == null) {
                throw new IllegalArgumentException("회원을 찾을 수 없습니다");
            }

            Product product = new Product();
            // 찾은 회원의 memberNum을 설정
            product.setMemberNum(member.getMemberNum());
            product.setMainCategoryNum(productDTO.getMainCategoryNum());
            product.setProductName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());
            product.setProductDescription(productDTO.getProductDescription());
            product.setProductCondition(productDTO.getProductCondition());
            product.setProductStatus(productDTO.getProductStatus());

            // 제품 저장
            Product savedProduct = productRepository.save(product);

            // 이미지 정보 저장
            for (String imageUrl : productDTO.getImageUrls()) {
                ProductImage productImage = new ProductImage();
                productImage.setProductNum(savedProduct.getProductNum());
                productImage.setImageUrl(imageUrl);
                // productImageRepository.save(productImage); // 이미지 정보를 저장하는 리포지토리
            }

            return savedProduct;
        } catch (Exception ex) {
            throw new RuntimeException("Error adding product", ex);
        }
    }

    // 최신순으로 모든 제품을 반환하는 메소드
    public List<Product> getProductsInLatestOrder() {
        try {
            return productRepository.findAllByOrderByCreatedAtDesc();
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving products in latest order", ex);
        }
    }

    // 사용자의 최근 검색 기반으로 제품을 검색하는 메소드
    public List<Product> getProductsBasedOnRecentSearch(Long memberNum) {
        try {
            List<SearchHistory> searchHistoryList = searchHistoryRepository.findByMemberNumOrderBySearchDateDesc(memberNum);
            if (!searchHistoryList.isEmpty()) {
                // 가장 최근 검색어 가져오기
                String recentSearchTerm = searchHistoryList.get(0).getSearchTerm();

                // 검색어와 일치하는 제품 찾기
                return productRepository.findByProductNameContaining(recentSearchTerm);
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            throw new RuntimeException("Error searching products based on recent search", ex);
        }
    }

    // 최근 거래량이 가장 많은 메인 카테고리의 제품들을 반환하는 메소드
    public List<Product> getTopProductsInMainCategory() {
        try {
            List<Object[]> topCategories = transactionRepository.findTopMainCategories();
            if (!topCategories.isEmpty()) {
                Long mainCategoryNum = (Long) topCategories.get(0)[0];
                return productRepository.findByMainCategoryNum(mainCategoryNum);
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving top products in main category", ex);
        }
    }

    // 메인 카테고리의 제품 반환
    public List<Product> getProductsByMainCategory(Long mainCategoryNum) {
        try {
            return productRepository.findByMainCategoryNum(mainCategoryNum);
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving products by main category", ex);
        }
    }

    // 특정 제품의 지난 3개월 간의 평균 가격 추이를 계산하는 메소드
    public Map<String, Double> getProductPriceTrend(String productName) {
        try {
            Map<String, Double> priceTrend = new LinkedHashMap<>();
            LocalDateTime now = LocalDateTime.now();

            for (int i = 3; i > 0; i--) {
                LocalDateTime startDate = now.minusMonths(i).withDayOfMonth(1);
                LocalDateTime endDate = now.minusMonths(i - 1).withDayOfMonth(1).minusSeconds(1);
                Optional<Double> averagePrice = productRepository.findAveragePriceByProductNameAndDateRange(productName, startDate, endDate);
                priceTrend.put(startDate.getMonth().toString() + " " + startDate.getYear(), averagePrice.orElse(0.0));
            }

            return priceTrend;
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving product price trend", ex);
        }
    }

    // 제품 ID로 특정 제품을 찾아 반환하는 메소드
    public Product getProductById(Long productId) {
        try {
            return productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving product by id", ex);
        }
    }
    // 채팅방 생성을 위해 물품의 상태를 확인하는 메소드
    public boolean isAvailableForChat(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        String status = product.getProductStatus();
        return !(status.equals("예약중") || status.equals("판매완료") );
    }

    // 물품 상태 변경을 위한 코드
    public Product updateProductStatus(Long productId, String newStatus) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
            product.setProductStatus(newStatus);
            return productRepository.save(product);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Error updating product status", ex);
        }
    }

}
