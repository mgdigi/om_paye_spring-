package com.mgdev.om_paye.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.mgdev.om_paye.validators.impl.NotBlankValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface NotBlank {

    String message() default "{validation.notBlank}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}