package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.response.AppointmentSlotResponse;
import com.project.capitec.booking.entity.Address;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.AppointmentTimeSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import com.project.capitec.booking.repository.AppointmentTimeSlotRepository;
import com.project.capitec.booking.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private AppointmentSlotRepository slotRepository;

    @Mock
    private AppointmentTimeSlotRepository timeSlotRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch activeBranch;
    private Branch inactiveBranch;
    private AppointmentTimeSlot timeSlot;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        // Setup active branch
        Address address = new Address(1L, "Mall of Africa", "Waterfall", "1686");

        activeBranch = new Branch();
        activeBranch.setId(1L);
        activeBranch.setCode("1041A");
        activeBranch.setActive(true);
        activeBranch.setAddress(address);

        // Setup inactive branch
        inactiveBranch = new Branch();
        inactiveBranch.setId(2L);
        inactiveBranch.setCode("CLOSED");
        inactiveBranch.setActive(false);
        inactiveBranch.setAddress(address);

        // Setup time slot
        timeSlot = new AppointmentTimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStartTime(LocalTime.of(10, 0));
        timeSlot.setEndTime(LocalTime.of(10, 30));
        timeSlot.setCapacity(5L);

        date = LocalDate.of(2026, 10, 27);
    }

    @Test
    void testGetAllActiveBranches() {
        // Arrange
        when(branchRepository.findAllActive()).thenReturn(List.of(activeBranch));

        // Act
        var branches = branchService.getAllActiveBranches();

        // Assert
        assertEquals(1, branches.size());
        assertTrue(branches.get(0).isActive());
        assertEquals("1041A", branches.get(0).getCode());
    }

    @Test
    void testGetAllActiveBranchesFiltersInactive() {
        // Arrange
        when(branchRepository.findAllActive()).thenReturn(List.of(activeBranch));

        // Act
        var branches = branchService.getAllActiveBranches();

        // Assert
        assertTrue(branches.stream().allMatch(b -> b.isActive()));
        assertFalse(branches.stream().anyMatch(b -> !b.isActive()));
    }

    @Test
    void testGetAvailableSlotsWithCapacity() {
        // Arrange
        AppointmentSlot slot = new AppointmentSlot(activeBranch, date, timeSlot);
        slot.setId(1L);
        slot.setBookedCount(0);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(activeBranch));
        when(timeSlotRepository.findAllByOrderByStartTimeAsc()).thenReturn(List.of(timeSlot));
        when(slotRepository.findByBranchIdAndDateAndTimeSlotId(1L, date, 1L))
            .thenReturn(Optional.of(slot));

        // Act
        List<AppointmentSlotResponse> slots = branchService.getAvailableSlots(1L, date);

        // Assert
        assertFalse(slots.isEmpty());
        assertEquals(5L, slots.get(0).getCapacity());
    }

    @Test
    void testGetAvailableSlotsFiltersNoCapacity() {
        // Arrange
        AppointmentSlot slot = new AppointmentSlot(activeBranch, date, timeSlot);
        slot.setId(1L);
        slot.setBookedCount(5); // Full

        when(branchRepository.findById(1L)).thenReturn(Optional.of(activeBranch));
        when(timeSlotRepository.findAllByOrderByStartTimeAsc()).thenReturn(List.of(timeSlot));
        when(slotRepository.findByBranchIdAndDateAndTimeSlotId(1L, date, 1L))
            .thenReturn(Optional.of(slot));

        // Act
        List<AppointmentSlotResponse> slots = branchService.getAvailableSlots(1L, date);

        // Assert
        assertTrue(slots.isEmpty());
    }

}
