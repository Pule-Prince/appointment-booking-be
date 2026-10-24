package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.serviceTypes WHERE p.id = :id")
    Optional<Product> findByIdWithServiceTypes(@Param("id") Long id);
}
