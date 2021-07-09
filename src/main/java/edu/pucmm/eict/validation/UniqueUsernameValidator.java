package edu.pucmm.eict.validation;

import edu.pucmm.eict.users.UserDao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private UserDao userDao;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        this.userDao = UserDao.getInstance();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && userDao.findByUsername(value).isEmpty();
    }
}