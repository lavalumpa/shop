package com.shopstuff.shop.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ItemDTO {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int id;
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
