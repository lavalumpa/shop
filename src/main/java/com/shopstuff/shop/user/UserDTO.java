package com.shopstuff.shop.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer id;
    @Size(min = 1, max = 50, message = "Name must be 1 to 50 characters long")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 8, message = "Password must have at least 8 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public static UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
