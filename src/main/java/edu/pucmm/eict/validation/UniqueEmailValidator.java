package edu.pucmm.eict.validation;

import edu.pucmm.eict.users.UserDao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private UserDao userDao;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        this.userDao = UserDao.getInstance();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && userDao.findByEmail(value).isEmpty();
    }
}