package com.project.capitec.booking.controller;

import com.project.capitec.booking.dto.response.ProductResponse;
import com.project.capitec.booking.dto.response.ServiceCategoryResponse;
import com.project.capitec.booking.dto.response.ServiceReasonResponse;
import com.project.capitec.booking.dto.response.ServiceTypeResponse;
import com.project.capitec.booking.service.ServiceCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-categories")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.DELETE})
public class ServiceCatalogueController {

    private final ServiceCatalogueService serviceCatalogueService;

    @Autowired
    public ServiceCatalogueController(ServiceCatalogueService serviceCatalogueService) {
        this.serviceCatalogueService = serviceCatalogueService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceCategoryResponse>> getAllCategories() {
        List<ServiceCategoryResponse> categories = serviceCatalogueService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponse> products = serviceCatalogueService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
}

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.DELETE})
class ProductController {

    private final ServiceCatalogueService serviceCatalogueService;

    @Autowired
    public ProductController(ServiceCatalogueService serviceCatalogueService) {
        this.serviceCatalogueService = serviceCatalogueService;
    }

    @GetMapping("/{productId}/service-types")
    public ResponseEntity<List<ServiceTypeResponse>> getServiceTypesByProduct(@PathVariable Long productId) {
        List<ServiceTypeResponse> types = serviceCatalogueService.getServiceTypesByProduct(productId);
        return ResponseEntity.ok(types);
    }
}

@RestController
@RequestMapping("/api/v1/service-types")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.DELETE})
class ServiceTypeController {

    private final ServiceCatalogueService serviceCatalogueService;

    @Autowired
    public ServiceTypeController(ServiceCatalogueService serviceCatalogueService) {
        this.serviceCatalogueService = serviceCatalogueService;
    }

    @GetMapping("/{serviceTypeId}/reasons")
    public ResponseEntity<List<ServiceReasonResponse>> getServiceReasons(@PathVariable Long serviceTypeId) {
        List<ServiceReasonResponse> reasons = serviceCatalogueService.getServiceReasonsByType(serviceTypeId);
        return ResponseEntity.ok(reasons);
    }
}
