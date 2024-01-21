package com.traders.tradersback.repository;

import com.traders.tradersback.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 제품 이름을 포함하는 모든 제품을 찾기 위한 메소드
    // 예: '노트북'을 포함하는 모든 제품 검색
    List<Product> findByProductNameContaining(String productName);

    // 생성 날짜 기준으로 모든 제품을 최신순으로 정렬하여 반환
    // 예: 가장 최근에 등록된 제품부터 순서대로 반환
    List<Product> findAllByOrderByCreatedAtDesc();

    // 주어진 메인 카테고리 번호에 해당하는 모든 제품을 반환
    // 예: '전자기기' 카테고리에 속하는 모든 제품 리스트 반환
    List<Product> findByMainCategoryNum(Long mainCategoryNum);

    // 제품 이름과 날짜 범위를 기준으로 평균 가격을 계산하는 쿼리
    // 사용 예: '노트북' 제품의 특정 날짜 범위 내 평균 가격 계산
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.productName LIKE %:productName% AND p.createdAt BETWEEN :startDate AND :endDate")
    Optional<Double> findAveragePriceByProductNameAndDateRange(@Param("productName") String productName, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.price FROM Product p WHERE p.productName = :productName ORDER BY p.createdAt DESC")
    List<Double> findRecentPricesByProductName(String productName);

}








