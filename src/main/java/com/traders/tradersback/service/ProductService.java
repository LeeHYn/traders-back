package com.traders.tradersback.service;

import com.traders.tradersback.dto.PriceTrendDTO;
import com.traders.tradersback.dto.ProductDTO;
import com.traders.tradersback.model.*;
import com.traders.tradersback.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ImageService imageService; // 이 서비스는 이미지 파일을 처리하고 URL을 반환합니다.
    @Autowired
    private MainCategoryRepository mainCategoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);



    // 상품 추가 메소드
    public Product addProduct(ProductDTO productDTO) {
        logger.info("상품 추가 시작: {}", productDTO.getProductName());

        if (productDTO.getImageFiles() == null || productDTO.getImageFiles().isEmpty()) {
            logger.warn("상품 이미지가 없습니다: {}", productDTO.getProductName());
            throw new IllegalArgumentException("최소 하나의 이미지가 필요합니다");
        }

        try {
            logger.debug("회원 ID로 회원 조회: {}", productDTO.getMemberId());
            Member member = memberRepository.findByMemberId(productDTO.getMemberId());
            if (member == null) {
                logger.warn("회원을 찾을 수 없습니다: {}", productDTO.getMemberId());
                throw new IllegalArgumentException("회원을 찾을 수 없습니다");
            }

            Product product = new Product();
            // 중요 데이터 설정 로그
            logger.debug("상품 정보 설정: 회원번호={}, 카테고리번호={}, 상품명={}",
                    member.getMemberNum(), productDTO.getMainCategoryNum(), productDTO.getProductName());

            // 상품 정보 설정
            product.setMemberNum(member.getMemberNum()); // 회원 번호 설정
            product.setMainCategoryNum(productDTO.getMainCategoryNum()); // 메인 카테고리 번호 설정
            product.setProductName(productDTO.getProductName()); // 상품명 설정
            product.setPrice(productDTO.getPrice()); // 가격 설정
            product.setProductDescription(productDTO.getProductDescription()); // 상품 설명 설정
            product.setProductCondition(productDTO.getProductCondition()); // 상품 상태 설정
            product.setProductStatus(productDTO.getProductStatus()); // 상품 판매 상태 설정
            product.setCreatedAt(LocalDateTime.now()); // 생성 시간 설정

            Product savedProduct = productRepository.save(product);
            logger.info("상품 저장 완료: 상품번호={}", savedProduct.getProductNum());

            for (MultipartFile file : productDTO.getImageFiles()) {
                logger.debug("이미지 파일 처리 시작: 파일명={}", file.getOriginalFilename());
                String imageUrl = imageService.saveImage(file); // 이미지 파일 저장 및 URL 반환
                logger.debug("이미지 파일 저장 완료: URL={}", imageUrl);

                ProductImage productImage = new ProductImage();
                productImage.setProductNum(savedProduct.getProductNum());
                productImage.setImageUrl(imageUrl);
                productImageRepository.save(productImage);
                logger.debug("상품 이미지 정보 저장: 상품번호={}, URL={}", savedProduct.getProductNum(), imageUrl);
            }

            return savedProduct;
        } catch (Exception ex) {
            logger.error("상품 추가 중 오류 발생: ", ex);
            throw new RuntimeException("Error adding product", ex);
        }
    }

    //제품 번호에 따른 이름을 반환하는 메소드
    public String getProductNameById(Long productId) {
        return productRepository.findById(productId)
                .map(Product::getProductName)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
    }
    //제품의 번호에 따른 이미지를 반환하는 메소드
    public List<ProductImage> getProductImages(Long productNum) {
        return productImageRepository.findByProductNum(productNum);
    }
    // 최신순으로 모든 제품을 반환하는 메소드
    public List<ProductDTO> getProductsInLatestOrder() {
        try {
            List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();
            return products.stream().map(product -> {
                List<ProductImage> images = getProductImages(product.getProductNum());
                return new ProductDTO(product, images);
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving products in latest order", ex);
        }
    }


    // 사용자의 최근 검색 기반으로 제품을 검색하는 메소드
    public List<ProductDTO> getProductsBasedOnRecentSearch(Long memberNum) {
        try {
            List<SearchHistory> searchHistoryList = searchHistoryRepository.findByMemberNumOrderBySearchDateDesc(memberNum);
            if (!searchHistoryList.isEmpty()) {
                String recentSearchTerm = searchHistoryList.get(0).getSearchTerm();
                List<Product> products = productRepository.findByProductNameContaining(recentSearchTerm);

                return products.stream().map(product -> {
                    List<ProductImage> images = getProductImages(product.getProductNum());
                    return new ProductDTO(product, images);
                }).collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            throw new RuntimeException("Error searching products based on recent search", ex);
        }
    }

    // 최근 거래량이 가장 많은 메인 카테고리의 제품들을 반환하는 메소드
    public List<ProductDTO> getTopProductsInMainCategory() {
        try {
            List<Object[]> topCategories = transactionRepository.findTopMainCategories();
            if (!topCategories.isEmpty()) {
                Long mainCategoryNum = (Long) topCategories.get(0)[0];
                List<Product> products = productRepository.findByMainCategoryNum(mainCategoryNum);
                Optional<MainCategory> mainCategoryOpt = mainCategoryRepository.findByMainCategoryNum(mainCategoryNum);

                return products.stream().map(product -> {
                    List<ProductImage> images = getProductImages(product.getProductNum());
                    ProductDTO productDTO = new ProductDTO(product, images);
                    mainCategoryOpt.ifPresent(mainCategory -> productDTO.setMainCategoryName(mainCategory.getMainCategoryName()));
                    return productDTO;
                }).collect(Collectors.toList());
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


    // 제품 ID로 특정 제품을 찾아 반환하는 메소드
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        List<ProductImage> images = productImageRepository.findByProductNum(productId);

        return new ProductDTO(product, images);
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
    // 특정 제품의 지난 3개월 간의 평균 가격 추이를 계산하는 메소드
    public Map<String, Double> getProductPriceTrend(String productName) {
        try {
            Map<String, Double> priceTrend = new LinkedHashMap<>();
            LocalDateTime now = LocalDateTime.now();

            for (int i = 2; i >= 0; i--) {
                LocalDateTime startDate = now.minusMonths(i).withDayOfMonth(1);
                LocalDateTime endDate = (i == 0 ? now : now.minusMonths(i - 1).withDayOfMonth(1)).minusSeconds(1);
                Optional<Double> averagePrice = productRepository.findAveragePriceByProductNameAndDateRange(productName, startDate, endDate);
                priceTrend.put(startDate.getMonth().toString() + " " + startDate.getYear(), averagePrice.orElse(0.0));
            }

            return priceTrend;
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving product price trend", ex);
        }
    }

    public PriceTrendDTO getProductPriceTrendWithRecentPrices(String productName) {
        Map<String, Double> averagePrices = getProductPriceTrend(productName); // 기존 메소드 사용
        List<Double> recentPrices = productRepository.findRecentPricesByProductName(productName); // 최근 가격을 가져오는 쿼리 필요

        PriceTrendDTO priceTrendDTO = new PriceTrendDTO();
        priceTrendDTO.setAveragePrices(averagePrices);
        priceTrendDTO.setRecentPrices(recentPrices);

        return priceTrendDTO;
    }

    // 검색어로 카테고리 이름 또는 제품 이름 검색
    public List<ProductDTO> searchProducts(String query) {
        try {
            // 먼저 카테고리 이름으로 검색 시도
            Optional<MainCategory> categoryOpt = mainCategoryRepository.findByMainCategoryName(query);
            if (categoryOpt.isPresent()) {
                // 카테고리 이름으로 일치하는 제품 검색
                Long mainCategoryNum = categoryOpt.get().getMainCategoryNum();
                List<Product> productsByCategory = productRepository.findByMainCategoryNum(mainCategoryNum);
                return mapProductsToDTOs(productsByCategory);
            } else {
                // 카테고리 이름이 일치하지 않으면 제품 이름으로 검색
                List<Product> productsByName = productRepository.findByProductNameContaining(query);
                return mapProductsToDTOs(productsByName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error searching products", ex);
        }
    }

    private List<ProductDTO> mapProductsToDTOs(List<Product> products) {
        return products.stream().map(product -> {
            List<ProductImage> images = getProductImages(product.getProductNum());
            return new ProductDTO(product, images);
        }).collect(Collectors.toList());
    }

}
