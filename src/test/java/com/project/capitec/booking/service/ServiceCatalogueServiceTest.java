package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.response.ProductResponse;
import com.project.capitec.booking.dto.response.ServiceCategoryResponse;
import com.project.capitec.booking.dto.response.ServiceTypeResponse;
import com.project.capitec.booking.entity.DocumentRequirement;
import com.project.capitec.booking.entity.Product;
import com.project.capitec.booking.entity.ServiceCategory;
import com.project.capitec.booking.entity.ServiceType;
import com.project.capitec.booking.repository.DocumentRequirementRepository;
import com.project.capitec.booking.repository.ProductRepository;
import com.project.capitec.booking.repository.ServiceCategoryRepository;
import com.project.capitec.booking.repository.ServiceTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceCatalogueServiceTest {

    @Mock
    private ServiceCategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private DocumentRequirementRepository documentRequirementRepository;

    @InjectMocks
    private ServiceCatalogueService serviceCatalogueService;

    private ServiceCategory category;
    private Product product;
    private ServiceType serviceType;
    private DocumentRequirement documentRequirement;

    @BeforeEach
    void setUp() {
        // Create mock category
        category = new ServiceCategory();
        category.setId(1L);
        category.setName("Insure");

        // Create mock product
        product = new Product();
        product.setId(1L);
        product.setName("Life Cover");
        product.setCategory(category);

        // Create mock service type
        serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName("New Policy");
        serviceType.setProduct(product);

        // Create mock document requirement
        documentRequirement = new DocumentRequirement();
        documentRequirement.setId(1L);
        documentRequirement.setDocumentName("Death Certificate");
        documentRequirement.setDescription("Original or certified copy");
        documentRequirement.setServiceType(serviceType);

        // Setup relationships
        category.setProducts(List.of(product));
        product.setServiceTypes(List.of(serviceType));
        serviceType.setServiceReasons(List.of());
        serviceType.setDocumentRequirements(List.of(documentRequirement));
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // Act
        List<ServiceCategoryResponse> categories = serviceCatalogueService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Insure", categories.get(0).getName());
    }

    @Test
    void testGetAllCategoriesEmpty() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<ServiceCategoryResponse> categories = serviceCatalogueService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void testGetProductsByCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        List<ProductResponse> products = serviceCatalogueService.getProductsByCategory(1L);

        // Assert
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Life Cover", products.get(0).getName());
    }

    @Test
    void testGetProductsByCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> serviceCatalogueService.getProductsByCategory(999L));
    }

    @Test
    void testGetProductsByCategoryEmpty() {
        // Arrange
        category.setProducts(List.of());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        List<ProductResponse> products = serviceCatalogueService.getProductsByCategory(1L);

        // Assert
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void testGetServiceTypesByProduct() {
        // Arrange
        when(productRepository.findByIdWithServiceTypes(1L)).thenReturn(Optional.of(product));

        // Act
        List<ServiceTypeResponse> serviceTypes = serviceCatalogueService.getServiceTypesByProduct(1L);

        // Assert
        assertNotNull(serviceTypes);
        assertEquals(1, serviceTypes.size());
        assertEquals("New Policy", serviceTypes.get(0).getName());
    }

    @Test
    void testGetServiceTypesByProductNotFound() {
        // Arrange
        when(productRepository.findByIdWithServiceTypes(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> serviceCatalogueService.getServiceTypesByProduct(999L));
    }

    @Test
    void testGetServiceTypesByProductEmpty() {
        // Arrange
        product.setServiceTypes(List.of());
        when(productRepository.findByIdWithServiceTypes(1L)).thenReturn(Optional.of(product));

        // Act
        List<ServiceTypeResponse> serviceTypes = serviceCatalogueService.getServiceTypesByProduct(1L);

        // Assert
        assertNotNull(serviceTypes);
        assertTrue(serviceTypes.isEmpty());
    }

    @Test
    void testGetDocumentRequirementsByServiceType() {
        // Arrange
        when(documentRequirementRepository.findByServiceTypeId(1L))
            .thenReturn(List.of(documentRequirement));

        // Act
        var requirements = serviceCatalogueService.getDocumentRequirementsByServiceType(1L);

        // Assert
        assertNotNull(requirements);
        assertEquals(1, requirements.size());
        assertEquals("Death Certificate", requirements.get(0).getDocumentName());
        assertEquals("Original or certified copy", requirements.get(0).getDescription());
    }

    @Test
    void testGetDocumentRequirementsByServiceTypeEmpty() {
        // Arrange
        when(documentRequirementRepository.findByServiceTypeId(1L)).thenReturn(List.of());

        // Act
        var requirements = serviceCatalogueService.getDocumentRequirementsByServiceType(1L);

        // Assert
        assertNotNull(requirements);
        assertTrue(requirements.isEmpty());
    }

    @Test
    void testGetServiceReasonsByType() {
        // Arrange
        when(serviceTypeRepository.findById(1L)).thenReturn(Optional.of(serviceType));

        // Act
        var reasons = serviceCatalogueService.getServiceReasonsByType(1L);

        // Assert
        assertNotNull(reasons);
    }

    @Test
    void testGetServiceReasonsByTypeNotFound() {
        // Arrange
        when(serviceTypeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> serviceCatalogueService.getServiceReasonsByType(999L));
    }
}
