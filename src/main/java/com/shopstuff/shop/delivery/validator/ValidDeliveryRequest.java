package com.shopstuff.shop.delivery.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidDeliveryRequestValidator.class})
@Documented
public @interface ValidDeliveryRequest {

    String message() default "Delivery has to be false or true with Address";

    Class<?>[]groups() default { };

    Class<? extends Payload>[] payload() default { };
}
