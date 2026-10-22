package com.project.capitec.booking.dto.response;

public class CustomerResponse {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String cellNumber;
    private String customerType;
    private Boolean isCapitecUser;

    public CustomerResponse() {}

    public CustomerResponse(Long customerId, String firstName, String lastName, String cellNumber, String customerType, Boolean isCapitecUser) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellNumber = cellNumber;
        this.customerType = customerType;
        this.isCapitecUser = isCapitecUser;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCellNumber() { return cellNumber; }
    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public Boolean getIsCapitecUser() { return isCapitecUser; }
    public void setIsCapitecUser(Boolean isCapitecUser) { this.isCapitecUser = isCapitecUser; }
}
