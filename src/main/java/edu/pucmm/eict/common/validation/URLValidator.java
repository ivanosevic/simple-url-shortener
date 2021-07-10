package edu.pucmm.eict.common.validation;

import edu.pucmm.eict.common.URLHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class URLValidator implements ConstraintValidator<URL, String> {

    private URLHelper urlHelper;

    @Override
    public void initialize(URL constraintAnnotation) {
        this.urlHelper = URLHelper.getInstance();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && urlHelper.isValid(value);
    }
}