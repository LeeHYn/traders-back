package com.traders.tradersback.model;

import javax.persistence.*;

@Entity
@Table(name = "main_category")
public class MainCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_category_num")
    private Long mainCategoryNum;

    @Column(name = "main_category_name")
    private String mainCategoryName;

    // 게터와 세터

    public Long getMainCategoryNum() {
        return mainCategoryNum;
    }

    public void setMainCategoryNum(Long mainCategoryNum) {
        this.mainCategoryNum = mainCategoryNum;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }
}
