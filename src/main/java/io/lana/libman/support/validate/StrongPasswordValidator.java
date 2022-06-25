package io.lana.libman.support.validate;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    private int minLength;

    private int maxLength;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(password)) return true;
        if (password.length() < minLength || password.length() > maxLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password length must be " + minLength + " to " + maxLength + " character.")
                    .addConstraintViolation();
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords must contain a minimum of 1 lower case letter [a-z]")
                    .addConstraintViolation();
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords must contain a minimum of 1 upper case letter [A-Z]")
                    .addConstraintViolation();
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords must contain a minimum of 1 numeric character [0-9]")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }


    public void initialize(StrongPassword strongPassword) {
        this.maxLength = strongPassword.maxLength();
        this.minLength = strongPassword.minLength();
    }
}
