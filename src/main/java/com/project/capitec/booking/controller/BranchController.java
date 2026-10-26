package com.project.capitec.booking.controller;

import com.project.capitec.booking.dto.response.AppointmentSlotResponse;
import com.project.capitec.booking.dto.response.BranchResponse;
import com.project.capitec.booking.service.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Tag(name = "Branches", description = "APIs for managing branches")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllActiveBranches() {
        List<BranchResponse> branches = branchService.getAllActiveBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{branchId}/slots")
    public ResponseEntity<List<AppointmentSlotResponse>> getAvailableSlots(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AppointmentSlotResponse> slots = branchService.getAvailableSlots(branchId, date);
        return ResponseEntity.ok(slots);
    }
}
