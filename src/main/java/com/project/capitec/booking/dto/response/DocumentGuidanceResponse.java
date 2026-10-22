package com.project.capitec.booking.dto.response;

public class DocumentGuidanceResponse {
    private String documentName;
    private boolean required;
    private String explanation;

    public DocumentGuidanceResponse() {}

    public DocumentGuidanceResponse(String documentName, boolean required, String explanation) {
        this.documentName = documentName;
        this.required = required;
        this.explanation = explanation;
    }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
