package edu.pucmm.eict.urlshortener.restapi.responses;

import java.util.List;

public class ApiError {
    private String title;
    private String message;
    private List<ApiSubError> subErrors;

    public ApiError(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public ApiError(String title, String message, List<ApiSubError> subErrors) {
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

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors;
    }
}