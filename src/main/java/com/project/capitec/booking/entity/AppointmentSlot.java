package com.project.capitec.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointment_slot")
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_slot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "time_slot_id", nullable = false)
    private AppointmentTimeSlot timeSlot;

    @Column(name = "slot_start_time", nullable = false)
    private LocalDateTime slotStartTime;

    @Column(name = "slot_end_time", nullable = false)
    private LocalDateTime slotEndTime;

    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount = 0;

    @OneToMany(mappedBy = "slot", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    public AppointmentSlot() {}

    public AppointmentSlot(Branch branch, LocalDate appointmentDate, AppointmentTimeSlot timeSlot) {
        this.branch = branch;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.slotStartTime = LocalDateTime.of(appointmentDate, timeSlot.getStartTime());
        this.slotEndTime = LocalDateTime.of(appointmentDate, timeSlot.getEndTime());
        this.bookedCount = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(AppointmentTimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public LocalDateTime getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(LocalDateTime slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public LocalDateTime getSlotEndTime() {
        return slotEndTime;
    }

    public void setSlotEndTime(LocalDateTime slotEndTime) {
        this.slotEndTime = slotEndTime;
    }

    public Integer getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(Integer bookedCount) {
        this.bookedCount = bookedCount;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
