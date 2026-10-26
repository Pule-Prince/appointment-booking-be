package com.project.capitec.booking.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentResponse {
    private Long appointmentId;
    private String referenceNumber;
    private CustomerResponse customer;
    private ServiceTypeResponse serviceType;
    private ServiceReasonResponse serviceReason;
    private AppointmentSlotResponse slot;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DocumentRequirementResponse> requiredDocuments;

    public AppointmentResponse() {}

    public AppointmentResponse(Long appointmentId, String referenceNumber, CustomerResponse customer,
                              ServiceTypeResponse serviceType, ServiceReasonResponse serviceReason,
                              AppointmentSlotResponse slot, String status, LocalDateTime createdAt,
                              LocalDateTime updatedAt, List<DocumentRequirementResponse> requiredDocuments) {
        this.appointmentId = appointmentId;
        this.referenceNumber = referenceNumber;
        this.customer = customer;
        this.serviceType = serviceType;
        this.serviceReason = serviceReason;
        this.slot = slot;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.requiredDocuments = requiredDocuments;
    }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public CustomerResponse getCustomer() { return customer; }
    public void setCustomer(CustomerResponse customer) { this.customer = customer; }
    public ServiceTypeResponse getServiceType() { return serviceType; }
    public void setServiceType(ServiceTypeResponse serviceType) { this.serviceType = serviceType; }
    public ServiceReasonResponse getServiceReason() { return serviceReason; }
    public void setServiceReason(ServiceReasonResponse serviceReason) { this.serviceReason = serviceReason; }
    public AppointmentSlotResponse getSlot() { return slot; }
    public void setSlot(AppointmentSlotResponse slot) { this.slot = slot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<DocumentRequirementResponse> getRequiredDocuments() { return requiredDocuments; }
    public void setRequiredDocuments(List<DocumentRequirementResponse> requiredDocuments) { this.requiredDocuments = requiredDocuments; }
}
