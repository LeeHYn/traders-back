package com.traders.tradersback.repository;

import com.traders.tradersback.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    Optional<MainCategory> findByMainCategoryNum(Long mainCategoryNum);
    Optional<MainCategory> findByMainCategoryName(String mainCategoryName);
}
