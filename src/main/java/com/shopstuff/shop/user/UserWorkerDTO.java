package com.shopstuff.shop.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopstuff.shop.user.validator.DoesNotContainAdmin;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserWorkerDTO {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer id;
    @Size(min = 1, max = 50, message = "Name must be 1 to 50 characters long")
    private String name;
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 8, message = "Password must have at least 8 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min=1, message = "Has to include at least 1 Role")
    @DoesNotContainAdmin
    private Set<Role> roles;

    public boolean containsRole(Role role){
        return roles.contains(role);
    }
}
