package com.hospital.cleaning.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDateTime> {

    @Override
    public void initialize(FutureDate constraintAnnotation) {
        // You can leave this empty if you have no special initialization
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull separately for null check
        }
        return value.isAfter(LocalDateTime.now());
    }
}
