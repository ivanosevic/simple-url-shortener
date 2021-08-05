package edu.pucmm.eict.restapi.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = URLValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface URL {

    String message() default "Unable to shorten that link. It is not a valid url.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}