package com.project.capitec.booking.dto.response;

public class ServiceReasonResponse {
    private Long reasonId;
    private String description;

    public ServiceReasonResponse() {}

    public ServiceReasonResponse(Long reasonId, String description) {
        this.reasonId = reasonId;
        this.description = description;
    }

    public Long getReasonId() { return reasonId; }
    public void setReasonId(Long reasonId) { this.reasonId = reasonId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
