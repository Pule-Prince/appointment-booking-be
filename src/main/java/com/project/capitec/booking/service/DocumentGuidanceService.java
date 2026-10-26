package com.project.capitec.booking.service;

import com.project.capitec.booking.dto.response.DocumentGuidanceResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentGuidanceService {

    private final Map<String, List<DocumentGuidanceResponse>> documentGuidance = new HashMap<>();

    public DocumentGuidanceService() {
        initializeDocumentGuidance();
    }

    private void initializeDocumentGuidance() {
        // Insure → Life Cover → Claim → Accidental death
        documentGuidance.put("INSURE_LIFE_CLAIM_ACCIDENTAL", List.of(
            new DocumentGuidanceResponse("Deceased's death certificate", true, "Official death certificate showing cause of death"),
            new DocumentGuidanceResponse("Claimant's South African ID", true, "Valid SA ID of the person making the claim"),
            new DocumentGuidanceResponse("Police report", true, "Police report for accidental or unnatural death")
        ));

        // Insure → Life Cover → Claim → Natural death
        documentGuidance.put("INSURE_LIFE_CLAIM_NATURAL", List.of(
            new DocumentGuidanceResponse("Deceased's death certificate", true, "Official death certificate"),
            new DocumentGuidanceResponse("Claimant's South African ID", true, "Valid SA ID of the person making the claim")
        ));

        // Insure → Funeral Cover → Claim
        documentGuidance.put("INSURE_FUNERAL_CLAIM", List.of(
            new DocumentGuidanceResponse("Deceased's death certificate", true, "Official death certificate"),
            new DocumentGuidanceResponse("Client South African ID", true, "Valid SA ID of the person making the claim"),
            new DocumentGuidanceResponse("BI Form", true, "Copy of the funeral policy")
        ));

        // Credit → Personal Loan → Apply
        documentGuidance.put("CREDIT_LOAN_APPLY", List.of(
            new DocumentGuidanceResponse("South African ID", true, "Valid SA ID"),
            new DocumentGuidanceResponse("Latest payslip or proof of income", true, "Recent payslip or other acceptable proof of income"),
            new DocumentGuidanceResponse("Recent bank statements", false, "Bank statements covering last 3 months where required")
        ));

        // Credit → Home Loan → Apply
        documentGuidance.put("CREDIT_HOME_LOAN_APPLY", List.of(
            new DocumentGuidanceResponse("South African ID", true, "Valid SA ID"),
            new DocumentGuidanceResponse("Proof of income", true, "Latest payslip or proof of income"),
            new DocumentGuidanceResponse("Recent bank statements", true, "Bank statements covering recent period"),
            new DocumentGuidanceResponse("Property details or offer to purchase", false, "Property details or offer to purchase if available")
        ));

        // Save → Savings Account → Open account
        documentGuidance.put("SAVE_ACCOUNT_OPEN", List.of(
            new DocumentGuidanceResponse("South African ID", true, "Valid SA ID"),
            new DocumentGuidanceResponse("Proof of residential address", false, "Proof of residential address where required")
        ));

        // Transact → Account services → Open Query
        documentGuidance.put("TRANSACT_QUERY_OPEN", List.of());

        // Business Inquiry → Business account services
        documentGuidance.put("BUSINESS_ACCOUNT_SERVICES", List.of(
            new DocumentGuidanceResponse("South African ID of authorised representative", true, "Valid SA ID of the authorised signatory"),
            new DocumentGuidanceResponse("Business registration documents", true, "Business registration documentation"),
            new DocumentGuidanceResponse("Proof of business address", false, "Proof of business address where required"),
            new DocumentGuidanceResponse("Company or ownership documentation", true, "Relevant company or ownership documentation")
        ));
    }

    public List<DocumentGuidanceResponse> getGuidance(Long serviceTypeId, Long serviceReasonId) {
        String key = buildKey(serviceTypeId, serviceReasonId);
        return documentGuidance.getOrDefault(key, new ArrayList<>());
    }

    public List<DocumentGuidanceResponse> getGuidanceForServiceType(Long serviceTypeId) {
        // Return guidance for service type without specific reason
        List<DocumentGuidanceResponse> allGuidance = new ArrayList<>();
        for (Map.Entry<String, List<DocumentGuidanceResponse>> entry : documentGuidance.entrySet()) {
            if (entry.getKey().contains(serviceTypeId.toString())) {
                allGuidance.addAll(entry.getValue());
            }
        }
        return allGuidance;
    }

    private String buildKey(Long serviceTypeId, Long serviceReasonId) {
        return serviceTypeId + "_" + (serviceReasonId != null ? serviceReasonId : "DEFAULT");
    }
}
