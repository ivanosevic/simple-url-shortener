package edu.pucmm.eict.restapi.common;

import edu.pucmm.eict.restapi.apiresponses.SubError;
import edu.pucmm.eict.restapi.apiresponses.ValidationError;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class MyValidator {
    private static MyValidator instance;
    private final Validator validator;

    private MyValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static MyValidator getInstance() {
        if (instance == null) {
            instance = new MyValidator();
        }
        return instance;
    }

    public <T> List<SubError> validate(T object) {
        var violations = validator.validate(object);
        List<SubError> errors = new ArrayList<>();
        for(var v : violations) {
            String reason = v.getMessage();
            String field = v.getPropertyPath().toString();
            Object rejectedValue = v.getInvalidValue();
            ValidationError error = new ValidationError(field, rejectedValue, reason);
            errors.add(error);
        }
        return errors;
    }
}
