package com.project.capitec.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateAppointmentRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotNull(message = "isCapitecUser flag is required")
    private Boolean isCapitecUser;

    @NotNull(message = "Service type ID is required")
    private Long serviceTypeId;

    @NotBlank(message = "Service reason is required")
    @Size(max = 200, message = "Service reason cannot exceed 200 characters")
    private String serviceReason;

    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Appointment date is required")
    private String appointmentDate;

    @NotNull(message = "Time slot ID is required")
    private Long timeSlotId;

    public CreateAppointmentRequest() {}

    public CreateAppointmentRequest(String firstName, String lastName, String email,
                                   Boolean isCapitecUser, Long serviceTypeId, String serviceReason,
                                   Long branchId, String appointmentDate, Long timeSlotId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isCapitecUser = isCapitecUser;
        this.serviceTypeId = serviceTypeId;
        this.serviceReason = serviceReason;
        this.branchId = branchId;
        this.appointmentDate = appointmentDate;
        this.timeSlotId = timeSlotId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceReason() {
        return serviceReason;
    }

    public void setServiceReason(String serviceReason) {
        this.serviceReason = serviceReason;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Boolean getIsCapitecUser() {
        return isCapitecUser;
    }

    public void setIsCapitecUser(Boolean isCapitecUser) {
        this.isCapitecUser = isCapitecUser;
    }
}
