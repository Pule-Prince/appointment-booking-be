package com.project.capitec.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name = "cell_number", nullable = false, length = 10)
    @NotBlank(message = "Cell number is required")
    @Size(max = 10, message = "Cell number cannot exceed 10 characters")
    private String cellNumber;

    @Column(name = "customer_type", nullable = false, length = 50)
    @NotBlank(message = "Customer type is required")
    private String customerType;

    @Column(name = "is_capitec_user", nullable = false)
    private Boolean isCapitecUser = false;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    public Customer() {}

    public Customer(String firstName, String lastName, String cellNumber, String customerType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellNumber = cellNumber;
        this.customerType = customerType;
        this.isCapitecUser = false;
    }

    public Customer(String firstName, String lastName, String cellNumber, String customerType, Boolean isCapitecUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellNumber = cellNumber;
        this.customerType = customerType;
        this.isCapitecUser = isCapitecUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Boolean getIsCapitecUser() {
        return isCapitecUser;
    }

    public void setIsCapitecUser(Boolean isCapitecUser) {
        this.isCapitecUser = isCapitecUser;
    }
}


