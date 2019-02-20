package com.kris.questademo2;

import java.util.List;

public class DataItem {

    private String categoryId;
    private String categoryName;
    private String categoryInfo;
    private String categoryIsLTQ = "false";
    private String isChecked = "NO";
    private List<SubCategoryItem> subCategory;

    public DataItem() {
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryInfo(){
        return  categoryInfo;
    }

    public void setCategoryInfo(String categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public String getCategoryIsLTQ() {
        return categoryIsLTQ;
    }

    public void setCategoryIsLTQ(String categoryIsLTQ) {
        this.categoryIsLTQ = categoryIsLTQ;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }


    public List<SubCategoryItem> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategoryItem> subCategory) {
        this.subCategory = subCategory;
    }



}
