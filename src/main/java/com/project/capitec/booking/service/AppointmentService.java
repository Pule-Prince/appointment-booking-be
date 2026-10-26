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
import com.project.capitec.booking.entity.AppointmentTimeSlot;
import com.project.capitec.booking.entity.Branch;
import com.project.capitec.booking.entity.Customer;
import com.project.capitec.booking.entity.ServiceReason;
import com.project.capitec.booking.entity.ServiceType;
import com.project.capitec.booking.interfaces.Notification;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.AppointmentSlotRepository;
import com.project.capitec.booking.repository.AppointmentTimeSlotRepository;
import com.project.capitec.booking.repository.BranchRepository;
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

    private final AppointmentTimeSlotRepository timeSlotRepository;

    private final BranchRepository branchRepository;

    private final ReferenceNumberGenerator referenceNumberGenerator;

    private final SlotService slotService;

    private final BranchService branchService;

    private final Notification notification;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, ServiceTypeRepository serviceTypeRepository,
                              ServiceReasonRepository serviceReasonRepository, ReferenceNumberGenerator referenceNumberGenerator, AppointmentSlotRepository slotRepository,
                              AppointmentTimeSlotRepository timeSlotRepository, BranchRepository branchRepository,
                              SlotService slotService, BranchService branchService, Notification notification) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceReasonRepository = serviceReasonRepository;
        this.referenceNumberGenerator = referenceNumberGenerator;
        this.slotRepository = slotRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.branchRepository = branchRepository;
        this.slotService = slotService;
        this.branchService = branchService;
        this.notification = notification;
    }

    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        // Validate service type
        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId()).orElse(null);

        // Parse appointment date
        LocalDate appointmentDate = LocalDate.parse(request.getAppointmentDate());

        // Get or create AppointmentSlot
        AppointmentSlot slot = slotRepository.findByBranchIdAndDateAndTimeSlotId(
                request.getBranchId(), appointmentDate, request.getTimeSlotId())
            .orElseGet(() -> {
                AppointmentTimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                    .orElseThrow(() -> new IllegalArgumentException("Time slot not found"));

                Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

                AppointmentSlot newSlot = new AppointmentSlot(branch, appointmentDate, timeSlot);
                return slotRepository.save(newSlot);
            });

        // Lock and check capacity
        slot = slotRepository.findByIdWithLock(slot.getId()).orElse(null);

        if (!slotService.isSlotValid(slot.getId())) {
            throw new IllegalStateException("Appointment slot is in the past or invalid");
        }

        if (!slotService.isSlotAvailable(slot.getId())) {
            throw new IllegalStateException("Appointment slot has no available capacity");
        }

        // Create or fetch customer
        Customer customer = new Customer(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getIsCapitecUser()
        );
        customer = customerRepository.save(customer);

        // Create service reason from user input
        ServiceReason serviceReason = new ServiceReason(serviceType, request.getServiceReason());
        serviceReason = serviceReasonRepository.save(serviceReason);

        // Generate unique reference number
        String referenceNumber = referenceNumberGenerator.generate();

        // Create appointment
        Appointment appointment = new Appointment(
            referenceNumber,
            customer,
            serviceType,
            serviceReason,
            slot,
            CONFIRMED.name()
        );

        appointment = appointmentRepository.save(appointment);

        // Increment booked_count on the slot
        slot.setBookedCount(slot.getBookedCount() + 1);
        slotRepository.save(slot);

        // Send confirmation email
        notification.sendAppointmentConfirmation(appointment, customer);

        // 10. Convert to response
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

        if (!appointment.getStatus().equals(CANCELLED.name())) {
            appointment.setStatus(CANCELLED.name());
            appointmentRepository.save(appointment);
            notification.sendAppointmentCancellation(appointment, appointment.getCustomer());
        }
        logger.info("Appointment cancelled: {}", referenceNumber);
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
            branchService.getBranchResponseById(appointment.getSlot().getBranch().getId()),
            appointment.getSlot().getSlotStartTime(),
            appointment.getSlot().getSlotEndTime(),
            appointment.getSlot().getTimeSlot().getCapacity(),
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
