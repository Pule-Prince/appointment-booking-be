package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.DocumentRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRequirementRepository extends JpaRepository<DocumentRequirement, Long> {

    List<DocumentRequirement> findByServiceTypeId(Long serviceTypeId);
}
