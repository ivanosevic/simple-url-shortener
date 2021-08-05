package edu.pucmm.eict.restapi.apiresponses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private String title;
    private String message;
    private List<SubError> subErrors;

    public ApiError(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public ApiError(String title, String message, List<SubError> subErrors) {
        this.title = title;
        this.message = message;
        this.subErrors = subErrors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<SubError> subErrors) {
        this.subErrors = subErrors;
    }
}
