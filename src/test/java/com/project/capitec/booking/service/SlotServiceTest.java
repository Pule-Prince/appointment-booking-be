package com.project.capitec.booking.service;

import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.AppointmentTimeSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlotServiceTest {

    @Mock
    private AppointmentSlotRepository slotRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private SlotService slotService;

    private AppointmentSlot futureSlot;
    private AppointmentSlot pastSlot;

    @BeforeEach
    void setUp() {
        // Setup future slot (tomorrow at 10:00)
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        AppointmentTimeSlot timeSlot = new AppointmentTimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStartTime(LocalTime.of(10, 0));
        timeSlot.setEndTime(LocalTime.of(10, 30));
        timeSlot.setCapacity(5L);

        Branch branch = new Branch();
        branch.setId(1L);

        futureSlot = new AppointmentSlot(branch, tomorrow, timeSlot);
        futureSlot.setId(1L);
        futureSlot.setBookedCount(0);

        // Setup past slot (yesterday at 10:00)
        LocalDate yesterday = LocalDate.now().minusDays(1);
        pastSlot = new AppointmentSlot(branch, yesterday, timeSlot);
        pastSlot.setId(2L);
        pastSlot.setBookedCount(0);
    }

    @Test
    void testIsSlotValidFuture() {
        // Arrange
        when(slotRepository.findById(1L)).thenReturn(Optional.of(futureSlot));

        // Act
        boolean isValid = slotService.isSlotValid(1L);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsSlotValidPast() {
        // Arrange
        when(slotRepository.findById(2L)).thenReturn(Optional.of(pastSlot));

        // Act
        boolean isValid = slotService.isSlotValid(2L);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testIsSlotAvailableWithCapacity() {
        // Arrange
        when(slotRepository.findById(1L)).thenReturn(Optional.of(futureSlot));
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        boolean isAvailable = slotService.isSlotAvailable(1L);

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    void testIsSlotNotAvailableNoCapacity() {
        // Arrange - create a booked appointment for this slot
        Appointment bookedApt = new Appointment();
        bookedApt.setSlot(futureSlot);
        bookedApt.setStatus("CONFIRMED");

        when(slotRepository.findById(1L)).thenReturn(Optional.of(futureSlot));
        futureSlot.setBookedCount(5); // All 5 spots taken

        when(appointmentRepository.findAll()).thenReturn(List.of(bookedApt, bookedApt, bookedApt, bookedApt, bookedApt));

        // Act
        boolean isAvailable = slotService.isSlotAvailable(1L);

        // Assert
        assertFalse(isAvailable);
    }

    @Test
    void testGetBookedCount() {
        // Arrange
        Appointment apt1 = new Appointment();
        apt1.setId(1L);
        apt1.setSlot(futureSlot);
        apt1.setStatus("CONFIRMED");

        when(appointmentRepository.findAll()).thenReturn(List.of(apt1));

        // Act
        long bookedCount = slotService.getBookedCount(1L);

        // Assert
        assertTrue(bookedCount > 0);
    }
}
