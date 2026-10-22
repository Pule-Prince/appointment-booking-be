package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    @Query("SELECT s FROM AppointmentSlot s WHERE s.branch.id = :branchId AND s.startTime >= :startTime AND s.endTime <= :endTime")
    List<AppointmentSlot> findByBranchIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            @Param("branchId") Long branchId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AppointmentSlot s WHERE s.id = :id")
    Optional<AppointmentSlot> findByIdWithLock(@Param("id") Long id);
}
