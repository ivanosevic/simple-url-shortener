package edu.pucmm.eict.urlshortener.restapi.responses;

public class ApiValidationError {
    private String field;
    private String reason;
    private Object rejectedValue;

    public ApiValidationError(String field, Object rejectedValue, String reason) {
        super();
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.reason = reason;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
