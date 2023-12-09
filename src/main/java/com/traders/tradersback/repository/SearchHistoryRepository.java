package com.traders.tradersback.repository;

import com.traders.tradersback.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByMemberNumOrderBySearchDateDesc(Long memberNum);
}
