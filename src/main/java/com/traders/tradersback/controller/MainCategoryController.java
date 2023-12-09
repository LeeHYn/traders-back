package com.traders.tradersback.controller;

import com.traders.tradersback.model.MainCategory;
import com.traders.tradersback.service.MainCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class MainCategoryController {

    @Autowired
    private MainCategoryService mainCategoryService;

    @GetMapping("/main")
    public ResponseEntity<List<MainCategory>> getAllMainCategories() {
        List<MainCategory> categories = mainCategoryService.getAllMainCategories();
        return ResponseEntity.ok(categories);
    }
}
