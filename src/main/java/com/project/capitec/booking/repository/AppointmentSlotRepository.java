package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    @Query("SELECT s FROM AppointmentSlot s WHERE s.branch.id = :branchId AND s.appointmentDate = :date AND s.timeSlot.id = :timeSlotId")
    Optional<AppointmentSlot> findByBranchIdAndDateAndTimeSlotId(
            @Param("branchId") Long branchId, @Param("date") LocalDate date, @Param("timeSlotId") Long timeSlotId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AppointmentSlot s WHERE s.id = :id")
    Optional<AppointmentSlot> findByIdWithLock(@Param("id") Long id);
}
