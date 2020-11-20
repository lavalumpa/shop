package com.shopstuff.shop.delivery.validator;

import com.shopstuff.shop.delivery.DeliveryDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDeliveryRequestValidator implements ConstraintValidator<ValidDeliveryRequest, DeliveryDTO> {

    @Override
    public void initialize(ValidDeliveryRequest validDeliveryRequest) {
    }


    @Override
    public boolean isValid(DeliveryDTO value, ConstraintValidatorContext context) {
        if (value==null|| value.getDeliveryRequested()==null){
            return false;
        }
        if ( value.isDeliveryRequested()) {
            return value.getAddress()!=null && value.infoProvided();
        } else {
            return true;
        }
    }
}
