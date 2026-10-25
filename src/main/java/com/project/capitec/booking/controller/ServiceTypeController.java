package com.project.capitec.booking.controller;

import com.project.capitec.booking.dto.response.ServiceReasonResponse;
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
@RequestMapping("/api/v1/service-types")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ServiceTypeController {

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
