package com.project.capitec.booking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CancelAppointmentRequest {

    @NotBlank(message = "Reference number is required")
    private String referenceNumber;

    public CancelAppointmentRequest() {}

    public CancelAppointmentRequest(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
