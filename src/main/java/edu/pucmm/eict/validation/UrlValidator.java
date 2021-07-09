package edu.pucmm.eict.validation;

import edu.pucmm.eict.common.UrlHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<Url, String> {

    private UrlHelper urlHelper;

    @Override
    public void initialize(Url constraintAnnotation) {
        this.urlHelper = UrlHelper.getInstance();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && urlHelper.isValid(value);
    }
}