package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.request.CreateAppointmentRequest;
import com.project.capitec.booking.dto.response.AppointmentResponse;
import com.project.capitec.booking.dto.response.AppointmentSlotResponse;
import com.project.capitec.booking.dto.response.CustomerResponse;
import com.project.capitec.booking.dto.response.DocumentRequirementResponse;
import com.project.capitec.booking.dto.response.ServiceReasonResponse;
import com.project.capitec.booking.dto.response.ServiceTypeResponse;
import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.Customer;
import com.project.capitec.booking.entity.ServiceReason;
import com.project.capitec.booking.entity.ServiceType;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import com.project.capitec.booking.repository.CustomerRepository;
import com.project.capitec.booking.repository.ServiceReasonRepository;
import com.project.capitec.booking.repository.ServiceTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.capitec.booking.enums.AppointmentStatus.CANCELLED;
import static com.project.capitec.booking.enums.AppointmentStatus.CONFIRMED;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;

    private final CustomerRepository customerRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final ServiceReasonRepository serviceReasonRepository;

    private final AppointmentSlotRepository slotRepository;

    private final ReferenceNumberGenerator referenceNumberGenerator;

    private final SlotService slotService;

    private final BranchService branchService;

    private final EmailService emailService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, ServiceTypeRepository serviceTypeRepository,
                              ServiceReasonRepository serviceReasonRepository, ReferenceNumberGenerator referenceNumberGenerator, AppointmentSlotRepository slotRepository,
                              SlotService slotService, BranchService branchService, EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceReasonRepository = serviceReasonRepository;
        this.referenceNumberGenerator = referenceNumberGenerator;
        this.slotRepository = slotRepository;
        this.slotService = slotService;
        this.branchService = branchService;
        this.emailService = emailService;
    }

    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        // 1. Validate service type
        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId())
            .orElseThrow(() -> new IllegalArgumentException("Service type not found"));

        // 2. Validate slot with pessimistic locking
        AppointmentSlot slot = slotRepository.findByIdWithLock(request.getSlotId())
            .orElseThrow(() -> new IllegalArgumentException("Appointment slot not found"));

        // 3. Validate slot is active, in the future, and has capacity
        if (!slotService.isSlotValid(request.getSlotId())) {
            throw new IllegalStateException("Appointment slot is in the past or invalid");
        }

        if (!slotService.isSlotAvailable(request.getSlotId())) {
            throw new IllegalStateException("Appointment slot has no available capacity");
        }

        // 4. Create or fetch customer
        Customer customer = new Customer(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getIsCapitecUser()
        );
        customer = customerRepository.save(customer);

        // 5. Create service reason from user input
        ServiceReason serviceReason = new ServiceReason(serviceType, request.getServiceReason());
        serviceReason = serviceReasonRepository.save(serviceReason);

        // 6. Generate unique reference number
        String referenceNumber = referenceNumberGenerator.generate();

        // 7. Create appointment
        Appointment appointment = new Appointment(
            referenceNumber,
            customer,
            serviceType,
            serviceReason,
            slot,
            CONFIRMED.name()
        );

        appointment = appointmentRepository.save(appointment);

        // 8. Send confirmation email
        emailService.sendAppointmentConfirmation(appointment, customer);

        // 9. Convert to response
        return mapToAppointmentResponse(appointment);
    }

    public AppointmentResponse getAppointmentByReference(String referenceNumber) {
        Appointment appointment = appointmentRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        return mapToAppointmentResponse(appointment);
    }

    public List<AppointmentResponse> getAppointmentsByDate(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return appointmentRepository.findAppointmentsByDate(date).stream()
            .map(this::mapToAppointmentResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void cancelAppointment(String referenceNumber) {
        Appointment appointment = appointmentRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (appointment.getStatus().equals(CANCELLED.name())) {
            throw new IllegalStateException("Appointment is already cancelled");
        }

        appointment.setStatus(CANCELLED.name());
        appointmentRepository.save(appointment);

        // Send cancellation email
        emailService.sendAppointmentCancellation(appointment, appointment.getCustomer());

        logger.info("Appointment cancelled: {}", referenceNumber);
    }

    public List<AppointmentResponse> getCustomerAppointments(Long customerId) {
        return appointmentRepository.findByCustomerId(customerId).stream()
            .map(this::mapToAppointmentResponse)
            .collect(Collectors.toList());
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        CustomerResponse customerResponse = new CustomerResponse(
            appointment.getCustomer().getId(),
            appointment.getCustomer().getFirstName(),
            appointment.getCustomer().getLastName(),
            appointment.getCustomer().getEmail(),
            appointment.getCustomer().getIsCapitecUser()
        );

        ServiceTypeResponse serviceTypeResponse = new ServiceTypeResponse(
            appointment.getServiceType().getId(),
            appointment.getServiceType().getName(),
            null
        );

        ServiceReasonResponse serviceReasonResponse = null;
        if (appointment.getServiceReason() != null) {
            serviceReasonResponse = new ServiceReasonResponse(
                appointment.getServiceReason().getId(),
                appointment.getServiceReason().getDescription()
            );
        }

        AppointmentSlotResponse slotResponse = new AppointmentSlotResponse(
            appointment.getSlot().getId(),
            branchService.getBranchById(appointment.getSlot().getBranch().getId()),
            appointment.getSlot().getStartTime(),
            appointment.getSlot().getEndTime(),
            appointment.getSlot().getCapacity(),
            slotService.getBookedCount(appointment.getSlot().getId())
        );

        List<DocumentRequirementResponse> requiredDocuments = appointment.getServiceType()
            .getDocumentRequirements().stream()
            .map(doc -> new DocumentRequirementResponse(doc.getId(), doc.getDocumentName(), doc.getDescription()))
            .collect(Collectors.toList());

        return new AppointmentResponse(
            appointment.getId(),
            appointment.getReferenceNumber(),
            customerResponse,
            serviceTypeResponse,
            serviceReasonResponse,
            slotResponse,
            appointment.getStatus(),
            appointment.getCreatedAt(),
            appointment.getUpdatedAt(),
            requiredDocuments
        );
    }
}
