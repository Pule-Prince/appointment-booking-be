package com.project.capitec.booking.dto.response;

public class AddressResponse {
    private Long addressId;
    private String addressLine;
    private String suburb;
    private String postalCode;

    public AddressResponse() {}

    public AddressResponse(Long addressId, String addressLine, String suburb, String postalCode) {
        this.addressId = addressId;
        this.addressLine = addressLine;
        this.suburb = suburb;
        this.postalCode = postalCode;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
