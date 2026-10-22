package com.project.capitec.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
@Table(name = "service_type")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_type_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY)
    private List<ServiceReason> serviceReasons;

    public ServiceType() {}

    public ServiceType(Long id, Product product, String name) {
        this.id = id;
        this.product = product;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceReason> getServiceReasons() {
        return serviceReasons;
    }

    public void setServiceReasons(List<ServiceReason> serviceReasons) {
        this.serviceReasons = serviceReasons;
    }
}
