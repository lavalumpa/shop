package com.shopstuff.shop.user.validator;

import com.shopstuff.shop.delivery.validator.ValidDeliveryRequest;
import com.shopstuff.shop.user.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class DoesNotContainAdminValidator implements ConstraintValidator<DoesNotContainAdmin,Set<Role>> {

    @Override
    public void initialize(DoesNotContainAdmin doesNotContainAdmin) {
    }


    @Override
    public boolean isValid(Set<Role> value, ConstraintValidatorContext context) {
        return !value.contains(Role.ADMIN);
    }
}
