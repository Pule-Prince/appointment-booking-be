package com.project.capitec.booking.dto.response;

public class BranchResponse {
    private Long branchId;
    private String code;
    private AddressResponse address;
    private boolean active;

    public BranchResponse() {}

    public BranchResponse(Long branchId, String code, AddressResponse address, boolean active) {
        this.branchId = branchId;
        this.code = code;
        this.address = address;
        this.active = active;
    }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public AddressResponse getAddress() { return address; }
    public void setAddress(AddressResponse address) { this.address = address; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
