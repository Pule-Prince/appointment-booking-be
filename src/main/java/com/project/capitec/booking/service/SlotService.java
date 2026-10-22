package com.project.capitec.booking.service;

import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        if (slot == null) {
            return false;
        }
        // Check if slot is in the future
        return !slot.getStartTime().isBefore(LocalDateTime.now());
    }

    public boolean isSlotAvailable(Long slotId) {
        AppointmentSlot slot = slotRepository.findById(slotId).orElse(null);
        if (slot == null || !isSlotValid(slotId)) {
            return false;
        }

        long bookedCount = appointmentRepository.findAll().stream()
            .filter(a -> a.getSlot() != null && a.getSlot().getId().equals(slotId))
            .filter(a -> !a.getStatus().equals("CANCELLED"))
            .count();

        return bookedCount < slot.getCapacity();
    }

    public long getBookedCount(Long slotId) {
        return appointmentRepository.findAll().stream()
            .filter(a -> a.getSlot() != null && a.getSlot().getId().equals(slotId))
            .filter(a -> !a.getStatus().equals("CANCELLED"))
            .count();
    }

    public long getAvailableCapacity(Long slotId) {
        AppointmentSlot slot = slotRepository.findById(slotId).orElse(null);
        if (slot == null) {
            return 0;
        }
        return Math.max(0, slot.getCapacity() - getBookedCount(slotId));
    }
}
