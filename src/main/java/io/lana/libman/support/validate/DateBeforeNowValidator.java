package io.lana.libman.support.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateBeforeNowValidator implements ConstraintValidator<DateBeforeNow, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return LocalDate.now().isAfter(value);
    }
}
