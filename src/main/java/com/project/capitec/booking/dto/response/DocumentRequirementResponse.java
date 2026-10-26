package com.project.capitec.booking.dto.response;

public class DocumentRequirementResponse {

    private Long id;
    private String documentName;
    private String description;

    public DocumentRequirementResponse() {}

    public DocumentRequirementResponse(Long id, String documentName, String description) {
        this.id = id;
        this.documentName = documentName;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
