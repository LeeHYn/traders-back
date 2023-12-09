package com.traders.tradersback.repository;

import com.traders.tradersback.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    // 필요한 경우 추가적인 쿼리 메소드를 여기에 정의할 수 있습니다.
}
