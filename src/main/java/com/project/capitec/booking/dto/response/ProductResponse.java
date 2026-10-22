package com.project.capitec.booking.dto.response;

import java.util.List;

public class ProductResponse {
    private Long productId;
    private String name;
    private List<ServiceTypeResponse> serviceTypes;

    public ProductResponse() {}

    public ProductResponse(Long productId, String name, List<ServiceTypeResponse> serviceTypes) {
        this.productId = productId;
        this.name = name;
        this.serviceTypes = serviceTypes;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ServiceTypeResponse> getServiceTypes() { return serviceTypes; }
    public void setServiceTypes(List<ServiceTypeResponse> serviceTypes) { this.serviceTypes = serviceTypes; }
}
