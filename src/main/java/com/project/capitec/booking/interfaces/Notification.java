package com.project.capitec.booking.interfaces;

import com.project.capitec.booking.entity.Appointment;
import com.project.capitec.booking.entity.Customer;

public interface Notification {

    void sendAppointmentConfirmation(Appointment appointment, Customer customer);

    void sendAppointmentCancellation(Appointment appointment, Customer customer);

    String buildConfirmationMessage(Appointment appointment, Customer customer);

    String buildCancellationMessage(Appointment appointment, Customer customer);
}
