package com.shopstuff.shop.item;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ItemDTO {

    private Integer id;
    @NotBlank(message = "Can't be blank")
    private String name;
    @Positive(message = "Has to be higher than 0")
    private Integer price;

    public static ItemDTO toDto(Item item){
        return ItemDTO.builder()
                .id(item.getId())
                .price(item.getPrice())
                .name(item.getName())
                .build();
    }


}
