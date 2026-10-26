package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.response.DocumentRequirementResponse;
import com.project.capitec.booking.dto.response.ProductResponse;
import com.project.capitec.booking.dto.response.ServiceCategoryResponse;
import com.project.capitec.booking.dto.response.ServiceReasonResponse;
import com.project.capitec.booking.dto.response.ServiceTypeResponse;
import com.project.capitec.booking.entity.Product;
import com.project.capitec.booking.entity.ServiceCategory;
import com.project.capitec.booking.entity.ServiceType;
import com.project.capitec.booking.repository.DocumentRequirementRepository;
import com.project.capitec.booking.repository.ProductRepository;
import com.project.capitec.booking.repository.ServiceCategoryRepository;
import com.project.capitec.booking.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCatalogueService {

    private final ServiceCategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final DocumentRequirementRepository documentRequirementRepository;

    @Autowired
    public ServiceCatalogueService(ServiceCategoryRepository categoryRepository, ProductRepository productRepository, ServiceTypeRepository serviceTypeRepository, DocumentRequirementRepository documentRequirementRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.documentRequirementRepository = documentRequirementRepository;
    }

    public List<ServiceCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::mapCategoryToResponse)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        ServiceCategory category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Service category not found"));

        return category.getProducts().stream()
            .map(this::mapProductToResponse)
            .collect(Collectors.toList());
    }

    public List<ServiceTypeResponse> getServiceTypesByProduct(Long productId) {
        Product product = productRepository.findByIdWithServiceTypes(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return product.getServiceTypes().stream()
            .map(this::mapServiceTypeToResponse)
            .collect(Collectors.toList());
    }

    public List<ServiceReasonResponse> getServiceReasonsByType(Long serviceTypeId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
            .orElseThrow(() -> new IllegalArgumentException("Service type not found"));
        return serviceType.getServiceReasons().stream()
            .map(reason -> new ServiceReasonResponse(reason.getId(), reason.getDescription()))
            .collect(Collectors.toList());
    }

    public List<DocumentRequirementResponse> getDocumentRequirementsByServiceType(Long serviceTypeId) {
        return documentRequirementRepository.findByServiceTypeId(serviceTypeId).stream()
            .map(doc -> new DocumentRequirementResponse(doc.getId(), doc.getDocumentName(), doc.getDescription()))
            .collect(Collectors.toList());
    }

    private ServiceCategoryResponse mapCategoryToResponse(ServiceCategory category) {
        List<ProductResponse> products = category.getProducts() != null
            ? category.getProducts().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList())
            : List.of();

        return new ServiceCategoryResponse(category.getId(), category.getName(), products);
    }

    private ProductResponse mapProductToResponse(Product product) {
        List<ServiceTypeResponse> serviceTypes = product.getServiceTypes() != null
            ? product.getServiceTypes().stream()
                .map(this::mapServiceTypeToResponse)
                .collect(Collectors.toList())
            : List.of();

        return new ProductResponse(product.getId(), product.getName(), serviceTypes);
    }

    private ServiceTypeResponse mapServiceTypeToResponse(ServiceType serviceType) {
        List<ServiceReasonResponse> reasons = serviceType.getServiceReasons() != null
            ? serviceType.getServiceReasons().stream()
                .map(reason -> new ServiceReasonResponse(reason.getId(), reason.getDescription()))
                .collect(Collectors.toList())
            : List.of();

        return new ServiceTypeResponse(serviceType.getId(), serviceType.getName(), reasons);
    }
}