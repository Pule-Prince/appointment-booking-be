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

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(customer.getCellNumber() + "@test.local"); // For MailHog testing
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            logger.info("Appointment confirmation email sent to {} for reference {}",
                customer.getCellNumber(), appointment.getReferenceNumber());
        } catch (Exception e) {
            logger.error("Failed to send appointment confirmation email", e);
        }
    }

    public void sendAppointmentCancellation(Appointment appointment, Customer customer) {

        try {
            String subject = "Appointment Cancelled - Reference: " + appointment.getReferenceNumber();

            String body = buildCancellationEmail(appointment, customer);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(customer.getCellNumber() + "@test.local"); // For MailHog testing
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            logger.info("Appointment cancellation email sent to {} for reference {}",
                customer.getCellNumber(), appointment.getReferenceNumber());
        } catch (Exception e) {
            logger.error("Failed to send appointment cancellation email", e);
        }
    }

    private String buildConfirmationEmail(Appointment appointment, Customer customer) {
        return String.format(
            "Dear %s %s,\n\n" +
            "Your appointment has been confirmed.\n\n" +
            "Appointment Details:\n" +
            "Reference Number: %s\n" +
            "Service: %s\n" +
            "Branch: %s\n" +
            "Date/Time: %s to %s\n" +
            "Customer Type: %s\n\n" +
            "Please keep your reference number for future enquiries.\n\n" +
            "Best regards,\n" +
            "Capitec Bank",
            customer.getFirstName(),
            customer.getLastName(),
            appointment.getReferenceNumber(),
            appointment.getServiceType().getName(),
            appointment.getSlot().getBranch().getCode(),
            appointment.getSlot().getStartTime(),
            appointment.getSlot().getEndTime(),
            customer.getCustomerType()
        );
    }

    private String buildCancellationEmail(Appointment appointment, Customer customer) {
        return String.format(
            "Dear %s %s,\n\n" +
            "Your appointment has been cancelled.\n\n" +
            "Cancelled Appointment Details:\n" +
            "Reference Number: %s\n" +
            "Service: %s\n" +
            "Branch: %s\n" +
            "Date/Time: %s to %s\n\n" +
            "If you need to reschedule, please book a new appointment online or visit your nearest branch.\n\n" +
            "Best regards,\n" +
            "Capitec Bank",
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
