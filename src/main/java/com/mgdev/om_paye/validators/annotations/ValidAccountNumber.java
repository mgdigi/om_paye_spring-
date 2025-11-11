package com.mgdev.om_paye.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.mgdev.om_paye.validators.impl.ValidAccountNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidAccountNumber {

    String message() default "Le numéro de compte doit commencer par 221 et contenir 12 chiffres.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}