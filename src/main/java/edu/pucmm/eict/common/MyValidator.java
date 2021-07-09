package edu.pucmm.eict.common;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public <T> Map<String, List<String>> validate(T object) {
        var violations = validator.validate(object);
        var errors = new HashMap<String, List<String>>();
        for(var v : violations) {
            String key = v.getPropertyPath().toString();
            String value = v.getMessage();
            errors.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        return errors;
    }
}
