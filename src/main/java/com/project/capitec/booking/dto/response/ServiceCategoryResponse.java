package com.project.capitec.booking.dto.response;

import java.util.List;

public class ServiceCategoryResponse {
    private Long categoryId;
    private String name;
    private List<ProductResponse> products;

    public ServiceCategoryResponse() {}

    public ServiceCategoryResponse(Long categoryId, String name, List<ProductResponse> products) {
        this.categoryId = categoryId;
        this.name = name;
        this.products = products;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ProductResponse> getProducts() { return products; }
    public void setProducts(List<ProductResponse> products) { this.products = products; }
}
