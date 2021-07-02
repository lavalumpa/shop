package com.shopstuff.shop.cart.validator;

import com.shopstuff.shop.cart.CartItemDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

public class UniqueCartItemIdsValidator implements ConstraintValidator<UniqueCartItemIds, List<CartItemDTO>> {

    @Override
    public void initialize(UniqueCartItemIds uniqueCartItemIds){}


    @Override
    public boolean isValid(List<CartItemDTO> cartItemDTOList, ConstraintValidatorContext context) {
        var set=new HashSet<Integer>();
        for (CartItemDTO cartItemDTO: cartItemDTOList){
            if (!set.add(cartItemDTO.getItemId())){
                return false;
            }
        }
        return true;
    }
}
