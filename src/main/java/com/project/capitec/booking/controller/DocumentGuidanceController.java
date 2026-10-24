package com.project.capitec.booking.controller;

import com.project.capitec.booking.dto.response.DocumentGuidanceResponse;
import com.project.capitec.booking.service.DocumentGuidanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-types")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.DELETE})
public class DocumentGuidanceController {

    private final DocumentGuidanceService documentGuidanceService;

    @Autowired
    public DocumentGuidanceController(DocumentGuidanceService documentGuidanceService) {
        this.documentGuidanceService = documentGuidanceService;
    }

    @GetMapping("/{serviceTypeId}/guidance")
    public ResponseEntity<List<DocumentGuidanceResponse>> getDocumentGuidance(
            @PathVariable Long serviceTypeId,
            @RequestParam(required = false) Long serviceReasonId) {
        List<DocumentGuidanceResponse> guidance = documentGuidanceService.getGuidance(serviceTypeId, serviceReasonId);
        return ResponseEntity.ok(guidance);
    }
}
