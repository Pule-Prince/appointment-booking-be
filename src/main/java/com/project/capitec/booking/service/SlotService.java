package com.project.capitec.booking.service;

import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.project.capitec.booking.enums.AppointmentStatus.CANCELLED;

@Service
public class SlotService {

    private final AppointmentSlotRepository slotRepository;

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public SlotService(AppointmentSlotRepository slotRepository, AppointmentRepository appointmentRepository) {
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public boolean isSlotValid(Long slotId) {
        AppointmentSlot slot = slotRepository.findById(slotId).orElse(null);
        return slot != null && !slot.getSlotStartTime().isBefore(LocalDateTime.now());
    }

    public boolean isSlotAvailable(Long slotId) {
        AppointmentSlot slot = slotRepository.findById(slotId).orElse(null);
        if (!isSlotValid(slotId)) {
            return false;
        }
        return getBookedCount(slotId) < slot.getTimeSlot().getCapacity();
    }

    public long getBookedCount(Long slotId) {
        return appointmentRepository.findAll().stream()
            .filter(a -> a.getSlot() != null && a.getSlot().getId().equals(slotId))
            .filter(a -> !a.getStatus().equals(CANCELLED.name()))
            .count();
    }
}
