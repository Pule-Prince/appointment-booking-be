package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByReferenceNumber(String referenceNumber);

    @Query("SELECT a FROM Appointment a WHERE a.slot.appointmentDate = :date ORDER BY a.slot.appointmentDate ASC")
    List<Appointment> findAppointmentsByDate(@Param("date") LocalDate date);
}
