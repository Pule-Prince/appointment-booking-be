package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.response.AddressResponse;
import com.project.capitec.booking.dto.response.AppointmentSlotResponse;
import com.project.capitec.booking.dto.response.BranchResponse;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import com.project.capitec.booking.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {

    private final BranchRepository branchRepository;

    private final AppointmentSlotRepository slotRepository;

    private final SlotService slotService;

    @Autowired
    public BranchService(BranchRepository branchRepository, AppointmentSlotRepository slotRepository, SlotService slotService) {
        this.branchRepository = branchRepository;
        this.slotRepository = slotRepository;
        this.slotService = slotService;
    }

    public List<BranchResponse> getAllActiveBranches() {
        return branchRepository.findAllActive().stream()
            .map(this::mapToBranchResponse)
            .collect(Collectors.toList());
    }

    public BranchResponse getBranchById(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
        return mapToBranchResponse(branch);
    }

    public List<AppointmentSlotResponse> getAvailableSlots(Long branchId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return slotRepository.findByBranchIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchId, startOfDay, endOfDay)
            .stream()
            .filter(slot -> slotService.isSlotAvailable(slot.getId()))
            .map(this::mapToSlotResponse)
            .collect(Collectors.toList());
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

    private AppointmentSlotResponse mapToSlotResponse(AppointmentSlot slot) {
        long bookedCount = slotService.getBookedCount(slot.getId());
        BranchResponse branchResponse = mapToBranchResponse(slot.getBranch());
        return new AppointmentSlotResponse(
            slot.getId(),
            branchResponse,
            slot.getStartTime(),
            slot.getEndTime(),
            slot.getCapacity(),
            bookedCount
        );
    }
}
