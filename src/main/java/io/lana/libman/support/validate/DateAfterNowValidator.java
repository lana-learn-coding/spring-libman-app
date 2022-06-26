package io.lana.libman.support.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateAfterNowValidator implements ConstraintValidator<DateAfterNow, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return LocalDate.now().isBefore(value) || LocalDate.now().equals(value);
    }
}
