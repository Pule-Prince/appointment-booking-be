package com.project.capitec.booking.controller;

import com.project.capitec.booking.dto.response.ServiceTypeResponse;
import com.project.capitec.booking.service.ServiceCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {

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
