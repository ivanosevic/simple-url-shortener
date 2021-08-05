package edu.pucmm.eict.restapi.endpoints;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import edu.pucmm.eict.restapi.apiresponses.ApiError;
import edu.pucmm.eict.restapi.endpoints.BaseEndPoint;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpStatus;

public class ErrorControllerAdvice extends BaseEndPoint {
    public ErrorControllerAdvice(Javalin app) {
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

    @Override
    public void applyRoutes() {
        app.exception(MismatchedInputException.class, this::handleJsonBadFormat);
        app.exception(InvalidFormatException.class, this::handleInvalidFormatException);
        app.exception(JsonMappingException.class, this::handleJsonMappingException);
    }
}
