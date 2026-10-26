package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.request.CreateAppointmentRequest;
import com.project.capitec.booking.dto.response.AppointmentResponse;
import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.AppointmentTimeSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.entity.Customer;
import com.project.capitec.booking.entity.ServiceType;
import com.project.capitec.booking.interfaces.Notification;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import com.project.capitec.booking.repository.CustomerRepository;
import com.project.capitec.booking.repository.ServiceReasonRepository;
import com.project.capitec.booking.repository.ServiceTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private ServiceReasonRepository serviceReasonRepository;

    @Mock
    private AppointmentSlotRepository slotRepository;

    @Mock
    private ReferenceNumberGenerator referenceNumberGenerator;

    @Mock
    private SlotService slotService;

    @Mock
    private BranchService branchService;

    @Mock
    private Notification notification;

    @InjectMocks
    private AppointmentService appointmentService;

    private CreateAppointmentRequest request;
    private ServiceType serviceType;
    private Branch branch;
    private AppointmentTimeSlot timeSlot;
    private AppointmentSlot appointmentSlot;
    private LocalDate appointmentDate;

    @BeforeEach
    void setUp() {
        appointmentDate = LocalDate.of(2026, 10, 27);

        // Create mock service type
        serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName("Test Service");
        serviceType.setDocumentRequirements(Collections.emptyList());

        // Create mock branch
        branch = new Branch();
        branch.setId(4L);
        branch.setCode("1041A");

        // Create mock time slot
        timeSlot = new AppointmentTimeSlot();
        timeSlot.setId(26L);
        timeSlot.setStartTime(LocalTime.of(10, 0));
        timeSlot.setEndTime(LocalTime.of(10, 30));
        timeSlot.setCapacity(5L);

        // Create mock appointment slot
        appointmentSlot = new AppointmentSlot(branch, appointmentDate, timeSlot);
        appointmentSlot.setId(1L);
        appointmentSlot.setBookedCount(0);

        // Create request
        request = new CreateAppointmentRequest(
            "Dev",
            "Test",
            "dev@test.com",
            true,
            1L,
            "Test reason",
            4L,
            "2026-10-27",
            26L
        );
    }

    @Test
    void testCreateAppointmentSuccess() {
        // Arrange
        when(serviceTypeRepository.findById(1L)).thenReturn(Optional.of(serviceType));
        when(slotRepository.findByBranchIdAndDateAndTimeSlotId(4L, appointmentDate, 26L))
            .thenReturn(Optional.of(appointmentSlot));
        when(slotRepository.findByIdWithLock(1L)).thenReturn(Optional.of(appointmentSlot));
        when(slotService.isSlotValid(1L)).thenReturn(true);
        when(slotService.isSlotAvailable(1L)).thenReturn(true);
        when(referenceNumberGenerator.generate()).thenReturn("REF-123");
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));
        when(serviceReasonRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> {
            Appointment apt = i.getArgument(0);
            apt.setId(1L);
            return apt;
        });

        // Act
        appointmentService.createAppointment(request);

        // Assert
        verify(appointmentRepository).save(any(Appointment.class));
        verify(slotRepository).save(appointmentSlot);
        verify(notification).sendAppointmentConfirmation(any(Appointment.class), any(Customer.class));
    }

    @Test
    void testCreateAppointmentSlotNotAvailable() {
        // Arrange
        when(serviceTypeRepository.findById(1L)).thenReturn(Optional.of(serviceType));
        when(slotRepository.findByBranchIdAndDateAndTimeSlotId(4L, appointmentDate, 26L))
            .thenReturn(Optional.of(appointmentSlot));
        when(slotRepository.findByIdWithLock(1L)).thenReturn(Optional.of(appointmentSlot));
        when(slotService.isSlotValid(1L)).thenReturn(true);
        when(slotService.isSlotAvailable(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> appointmentService.createAppointment(request));
    }

    @Test
    void testCreateAppointmentSlotInPast() {
        // Arrange
        when(serviceTypeRepository.findById(1L)).thenReturn(Optional.of(serviceType));
        when(slotRepository.findByBranchIdAndDateAndTimeSlotId(4L, appointmentDate, 26L))
            .thenReturn(Optional.of(appointmentSlot));
        when(slotRepository.findByIdWithLock(1L)).thenReturn(Optional.of(appointmentSlot));
        when(slotService.isSlotValid(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> appointmentService.createAppointment(request));
    }
}
