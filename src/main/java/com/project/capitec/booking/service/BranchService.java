package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.response.AddressResponse;
import com.project.capitec.booking.dto.response.AppointmentSlotResponse;
import com.project.capitec.booking.dto.response.BranchResponse;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.AppointmentTimeSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import com.project.capitec.booking.repository.AppointmentTimeSlotRepository;
import com.project.capitec.booking.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final BranchRepository branchRepository;

    private final AppointmentSlotRepository slotRepository;

    private final AppointmentTimeSlotRepository timeSlotRepository;


    @Autowired
    public BranchService(BranchRepository branchRepository, AppointmentSlotRepository slotRepository,
                         AppointmentTimeSlotRepository timeSlotRepository) {
        this.branchRepository = branchRepository;
        this.slotRepository = slotRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    private Branch getBranchByID(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
    }

    public List<BranchResponse> getAllActiveBranches() {
        return branchRepository.findAllActive().stream()
            .map(this::mapToBranchResponse)
            .collect(Collectors.toList());
    }

    public BranchResponse getBranchResponseById(Long branchId) {
        Branch branch = getBranchByID(branchId);
        return mapToBranchResponse(branch);
    }

    public List<AppointmentSlotResponse> getAvailableSlots(Long branchId, LocalDate date) {
        Branch branch = getBranchByID(branchId);
        // Get all static time slots
        List<AppointmentTimeSlot> timeSlots = timeSlotRepository.findAllByOrderByStartTimeAsc();
        // Check which ones have availability
        return timeSlots.stream()
            .map(timeSlot -> {
                AppointmentSlot appointmentSlot = slotRepository.findByBranchIdAndDateAndTimeSlotId(branchId, date, timeSlot.getId())
                    .orElse(null);

                long bookedCount = appointmentSlot != null ? appointmentSlot.getBookedCount() : 0;
                long availableCapacity = timeSlot.getCapacity() - bookedCount;

                if (availableCapacity > 0) {
                    LocalDateTime startTime = LocalDateTime.of(date, timeSlot.getStartTime());
                    LocalDateTime endTime = LocalDateTime.of(date, timeSlot.getEndTime());
                    return mapToAppointmentSlotResponse(timeSlot, mapToBranchResponse(branch), startTime, endTime, availableCapacity);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private AppointmentSlotResponse mapToAppointmentSlotResponse(AppointmentTimeSlot appointmentTimeSlot, BranchResponse branchResponse, LocalDateTime startTime, LocalDateTime endTime, long capacity) {
        return new AppointmentSlotResponse(
            appointmentTimeSlot.getId(),
            branchResponse,
            startTime,
            endTime,
            appointmentTimeSlot.getCapacity(),
            capacity
        );
    }

    private BranchResponse mapToBranchResponse(Branch branch) {
        AddressResponse addressResponse = new AddressResponse(
            branch.getAddress().getId(),
            branch.getAddress().getAddressLine(),
            branch.getAddress().getSuburb(),
            branch.getAddress().getPostalCode()
        );
        return new BranchResponse(
            branch.getId(),
            branch.getCode(),
            addressResponse,
            branch.isActive()
        );
    }
}
