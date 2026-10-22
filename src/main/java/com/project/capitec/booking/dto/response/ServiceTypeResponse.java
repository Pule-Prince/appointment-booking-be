package com.project.capitec.booking.dto.response;

import java.util.List;

public class ServiceTypeResponse {
    private Long typeId;
    private String name;
    private List<ServiceReasonResponse> reasons;

    public ServiceTypeResponse() {}

    public ServiceTypeResponse(Long typeId, String name, List<ServiceReasonResponse> reasons) {
        this.typeId = typeId;
        this.name = name;
        this.reasons = reasons;
    }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ServiceReasonResponse> getReasons() { return reasons; }
    public void setReasons(List<ServiceReasonResponse> reasons) { this.reasons = reasons; }
}
