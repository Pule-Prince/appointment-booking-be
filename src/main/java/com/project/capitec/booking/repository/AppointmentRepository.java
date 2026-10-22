package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByReferenceNumber(String referenceNumber);

    List<Appointment> findByCustomerId(Long customerId);
}
