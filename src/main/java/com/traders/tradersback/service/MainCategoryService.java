package com.traders.tradersback.service;

import com.traders.tradersback.model.MainCategory;
import com.traders.tradersback.repository.MainCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainCategoryService {
    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    public List<MainCategory> getAllMainCategories() {
        return mainCategoryRepository.findAll();
    }
}
