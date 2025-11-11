package com.mgdev.om_paye.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.mgdev.om_paye.validators.impl.ValidAmountValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidAmount {

    String message() default "Le montant doit être positif et supérieur à 100 FCFA.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}