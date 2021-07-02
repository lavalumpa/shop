package com.shopstuff.shop.cart.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueCartItemIdsValidator.class)
@Documented
public @interface UniqueCartItemIds {
    String message() default "Cart ids have to be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
