package com.project.capitec.booking.service;

import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from:noreply@capitec.co.za}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentConfirmation(Appointment appointment, Customer customer) {

        try {
            String subject = "Appointment Confirmation - Reference: " + appointment.getReferenceNumber();
            String body = buildConfirmationEmail(appointment, customer);
            SimpleMailMessage message = emailMessage(customer, subject, body);
            mailSender.send(message);
            logger.info("Appointment confirmation email sent to {} for reference {}",
                customer.getEmail(), appointment.getReferenceNumber());
        } catch (Exception e) {
            logger.error("Failed to send appointment confirmation email", e);
        }
    }

    public void sendAppointmentCancellation(Appointment appointment, Customer customer) {

        try {
            String subject = "Appointment Cancelled - Reference: " + appointment.getReferenceNumber();
            String body = buildCancellationEmail(appointment, customer);
            SimpleMailMessage message = emailMessage(customer, subject, body);
            mailSender.send(message);
            logger.info("Appointment cancellation email sent to {} for reference {}",
                customer.getEmail(), appointment.getReferenceNumber());
        } catch (Exception e) {
            logger.error("Failed to send appointment cancellation email", e);
        }
    }

    private SimpleMailMessage emailMessage(Customer customer, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(customer.getEmail() + "@test.local");
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    private String buildConfirmationEmail(Appointment appointment, Customer customer) {
        return String.format(
            """
            Dear %s %s,
            
            Your appointment has been confirmed.
            
            Appointment Details:
            Reference Number: %s
            Service: %s
            Branch: %s
            Date/Time: %s to %s
            Please keep your reference number for future enquiries.
            
            Best regards,
            Capitec Bank""",
            customer.getFirstName(),
            customer.getLastName(),
            appointment.getReferenceNumber(),
            appointment.getServiceType().getName(),
            appointment.getSlot().getBranch().getCode(),
            appointment.getSlot().getStartTime(),
            appointment.getSlot().getEndTime()
        );
    }

    private String buildCancellationEmail(Appointment appointment, Customer customer) {
        return String.format(
            """
            Dear %s %s,
            
            Your appointment has been cancelled.
            
            Cancelled Appointment Details:
            Reference Number: %s
            Service: %s
            Branch: %s
            Date/Time: %s to %s
            
            If you need to reschedule, please book a new appointment online or visit your nearest branch.
            
            Best regards,
            Capitec Bank""",
            customer.getFirstName(),
            customer.getLastName(),
            appointment.getReferenceNumber(),
            appointment.getServiceType().getName(),
            appointment.getSlot().getBranch().getCode(),
            appointment.getSlot().getStartTime(),
            appointment.getSlot().getEndTime()
        );
    }
}
