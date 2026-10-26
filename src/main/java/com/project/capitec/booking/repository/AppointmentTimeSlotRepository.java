package com.project.capitec.booking.repository;

import com.project.capitec.booking.entity.AppointmentTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentTimeSlotRepository extends JpaRepository<AppointmentTimeSlot, Long> {

    List<AppointmentTimeSlot> findAllByOrderByStartTimeAsc();
}
