package com.shopstuff.shop.user.validator;

import com.shopstuff.shop.user.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class ValidWorkerValidator implements ConstraintValidator<ValidWorker, Set<Role>> {

    @Override
    public void initialize(ValidWorker validWorker) {
    }


    @Override
    public boolean isValid(Set<Role> value, ConstraintValidatorContext context) {
        return !value.contains(Role.ADMIN) && !value.contains(Role.CUSTOMER);
    }
}
