package edu.pucmm.eict.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UrlValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Url {

    String message() default "Unable to shorten that link. It is not a valid url.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}