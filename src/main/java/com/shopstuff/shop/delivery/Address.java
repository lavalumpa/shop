package com.shopstuff.shop.delivery;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {

    private String city;
    private String street;
    private Integer number;

}