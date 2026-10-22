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

    @NotBlank(message = "Cell number is required")
    @Size(max = 10, message = "Cell number cannot exceed 10 characters")
    private String cellNumber;

    @NotBlank(message = "Customer type is required")
    @Size(max = 50, message = "Customer type cannot exceed 50 characters")
    private String customerType;

    @NotNull(message = "isCapitecUser flag is required")
    private Boolean isCapitecUser;

    @NotNull(message = "Service type ID is required")
    private Long serviceTypeId;

    private Long serviceReasonId;

    @NotNull(message = "Appointment slot ID is required")
    private Long slotId;

    public CreateAppointmentRequest() {}

    public CreateAppointmentRequest(String firstName, String lastName, String cellNumber,
                                   String customerType, Boolean isCapitecUser, Long serviceTypeId, Long serviceReasonId,
                                   Long slotId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellNumber = cellNumber;
        this.customerType = customerType;
        this.isCapitecUser = isCapitecUser;
        this.serviceTypeId = serviceTypeId;
        this.serviceReasonId = serviceReasonId;
        this.slotId = slotId;
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

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Long getServiceReasonId() {
        return serviceReasonId;
    }

    public void setServiceReasonId(Long serviceReasonId) {
        this.serviceReasonId = serviceReasonId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Boolean getIsCapitecUser() {
        return isCapitecUser;
    }

    public void setIsCapitecUser(Boolean isCapitecUser) {
        this.isCapitecUser = isCapitecUser;
    }
}
