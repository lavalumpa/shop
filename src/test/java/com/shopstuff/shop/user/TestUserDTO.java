package com.shopstuff.shop.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestUserDTO {
    private int id;
    private String name;
    private String email;
    private String password;
}
