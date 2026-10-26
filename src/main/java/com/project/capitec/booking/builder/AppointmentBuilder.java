package com.project.capitec.booking.builder;

import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.AppointmentSlot;
import com.project.capitec.booking.entity.Customer;
import com.project.capitec.booking.entity.ServiceReason;
import com.project.capitec.booking.entity.ServiceType;
import com.project.capitec.booking.repository.AppointmentRepository;
import com.project.capitec.booking.repository.CustomerRepository;
import com.project.capitec.booking.repository.ServiceReasonRepository;
import com.project.capitec.booking.service.ReferenceNumberGenerator;

import static com.project.capitec.booking.enums.AppointmentStatus.CONFIRMED;

public class AppointmentBuilder {
    private final CustomerRepository customerRepository;
    private final ServiceReasonRepository serviceReasonRepository;
    private final AppointmentRepository appointmentRepository;
    private final ReferenceNumberGenerator referenceNumberGenerator;

    private String firstName;
    private String lastName;
    private String email;
    private Boolean isCapitecUser;
    private ServiceType serviceType;
    private String serviceReasonDescription;
    private AppointmentSlot slot;

    public AppointmentBuilder(
            CustomerRepository customerRepository,
            ServiceReasonRepository serviceReasonRepository,
            AppointmentRepository appointmentRepository,
            ReferenceNumberGenerator referenceNumberGenerator) {
        this.customerRepository = customerRepository;
        this.serviceReasonRepository = serviceReasonRepository;
        this.appointmentRepository = appointmentRepository;
        this.referenceNumberGenerator = referenceNumberGenerator;
    }

    public AppointmentBuilder withCustomerDetails(String firstName, String lastName, String email, Boolean isCapitecUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isCapitecUser = isCapitecUser;
        return this;
    }

    public AppointmentBuilder withServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public AppointmentBuilder withServiceReason(String description) {
        this.serviceReasonDescription = description;
        return this;
    }

    public AppointmentBuilder withSlot(AppointmentSlot slot) {
        this.slot = slot;
        return this;
    }

    public Appointment build() {
        // Create or fetch customer
        Customer customer = new Customer(firstName, lastName, email, isCapitecUser);
        customer = customerRepository.save(customer);

        // Create service reason from user input
        ServiceReason serviceReason = new ServiceReason(serviceType, serviceReasonDescription);
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

        return appointmentRepository.save(appointment);
    }
}
