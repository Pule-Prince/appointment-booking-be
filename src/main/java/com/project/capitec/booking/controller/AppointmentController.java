package com.project.capitec.booking.controller;

import com.project.capitec.booking.dto.request.CreateAppointmentRequest;
import com.project.capitec.booking.dto.response.AppointmentResponse;
import com.project.capitec.booking.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.DELETE})
@Tag(name = "Appointments", description = "APIs for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{referenceNumber}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable String referenceNumber) {
        AppointmentResponse response = appointmentService.getAppointmentByReference(referenceNumber);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{referenceNumber}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable String referenceNumber) {
        appointmentService.cancelAppointment(referenceNumber);
        return ResponseEntity.noContent().build();
    }
}
