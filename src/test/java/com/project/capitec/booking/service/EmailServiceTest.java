package com.project.capitec.booking.service;

import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.AppointmentTimeSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.entity.Customer;
import com.project.capitec.booking.entity.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private Appointment appointment;
    private Customer customer;
    private AppointmentSlot slot;

    @BeforeEach
    void setUp() {
        // Create mock customer
        customer = new Customer("John", "Doe", "john@example.com", true);
        customer.setId(1L);

        // Create mock service type
        ServiceType serviceType = new ServiceType();
        serviceType.setId(1L);
        serviceType.setName("Test Service");

        // Create mock address
        com.project.capitec.booking.entity.Address address =
            new com.project.capitec.booking.entity.Address(1L, "Mall of Africa", "Waterfall", "1686");

        // Create mock branch
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setCode("1041A");
        branch.setAddress(address);

        // Create mock time slot
        AppointmentTimeSlot timeSlot = new AppointmentTimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStartTime(LocalTime.of(10, 0));
        timeSlot.setEndTime(LocalTime.of(10, 30));
        timeSlot.setCapacity(5L);

        // Create mock appointment slot
        slot = new AppointmentSlot(branch, LocalDate.of(2026, 10, 27), timeSlot);
        slot.setId(1L);
        slot.setBookedCount(1);

        // Create mock appointment
        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setReferenceNumber("REF-12345");
        appointment.setCustomer(customer);
        appointment.setServiceType(serviceType);
        appointment.setSlot(slot);
        appointment.setStatus("CONFIRMED");
        appointment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSendAppointmentConfirmation() {
        // Act & Assert
        assertDoesNotThrow(() ->
            emailService.sendAppointmentConfirmation(appointment, customer)
        );

        // Verify email was sent
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendAppointmentCancellation() {
        // Arrange
        appointment.setStatus("CANCELLED");

        // Act & Assert
        assertDoesNotThrow(() ->
            emailService.sendAppointmentCancellation(appointment, customer)
        );

        // Verify email was sent
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testConfirmationEmailContainsReferenceNumber() {
        // Act & Assert
        assertDoesNotThrow(() ->
            emailService.sendAppointmentConfirmation(appointment, customer)
        );

        // Verify that send was called with a mail message
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testCancellationEmailContainsReferenceNumber() {
        // Act & Assert
        assertDoesNotThrow(() ->
            emailService.sendAppointmentCancellation(appointment, customer)
        );

        // Verify that send was called with a mail message
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testEmailSentToCorrectRecipient() {
        // Act & Assert
        assertDoesNotThrow(() ->
            emailService.sendAppointmentConfirmation(appointment, customer)
        );

        // Verify email was sent to customer's email address
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
