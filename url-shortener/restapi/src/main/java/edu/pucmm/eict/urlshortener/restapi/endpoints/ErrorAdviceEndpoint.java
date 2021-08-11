package edu.pucmm.eict.urlshortener.restapi.endpoints;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import edu.pucmm.eict.urlshortener.restapi.responses.ApiError;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

import javax.persistence.EntityNotFoundException;

public class ErrorAdviceEndpoint extends BaseEndpoint {

    public ErrorAdviceEndpoint(Javalin app) {
        super(app);
    }

    private void handleJsonBadFormat(MismatchedInputException ex, Context ctx) {
        ApiError apiError = new ApiError("Bad JsonFormat", "Please, check that you are sending the correct JSON Format. For more information, consult the API Documentation.");
        ctx.status(HttpStatus.BAD_REQUEST_400).json(apiError);
    }

    private void handleInvalidFormatException(InvalidFormatException ex, Context ctx) {
        ApiError apiError = new ApiError("Bad JSON", "Please, check that you are sending a correct JSON format.");
        ctx.status(HttpStatus.BAD_REQUEST_400).json(apiError);
    }

    private void handleJsonMappingException(JsonMappingException ex, Context ctx) {
        ApiError apiError = new ApiError("Bad JSON", "Error while mapping the sent JSON. Please, check if the JSON doesn't contains any errors.");
        ctx.status(HttpStatus.BAD_REQUEST_400).json(apiError);
    }

    private void handleEntityNotFoundException(EntityNotFoundException ex, Context ctx) {
        ApiError apiError = new ApiError("Not found", ex.getMessage());
        ctx.status(HttpStatus.NOT_FOUND_404).json(apiError);
    }

    private void handleInternalServerError(Context ctx) {
        ApiError apiError = new ApiError("Internal Server Error", "Please contact with the side administrator");
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR_500).json(apiError);
    }

    @Override
    public void applyEndpoints() {
        app.exception(MismatchedInputException.class, this::handleJsonBadFormat);
        app.exception(InvalidFormatException.class, this::handleInvalidFormatException);
        app.exception(JsonMappingException.class, this::handleJsonMappingException);
        app.exception(EntityNotFoundException.class, this::handleEntityNotFoundException);
        app.error(HttpStatus.INTERNAL_SERVER_ERROR_500, this::handleInternalServerError);
    }
}
