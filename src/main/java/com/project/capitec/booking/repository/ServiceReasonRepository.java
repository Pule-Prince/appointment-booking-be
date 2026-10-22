package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.ServiceReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceReasonRepository extends JpaRepository<ServiceReason, Long> {
}
