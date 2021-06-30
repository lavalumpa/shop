package com.shopstuff.shop.user;


import com.shopstuff.shop.exceptions.UserEmailDuplicateException;
import com.shopstuff.shop.exceptions.UserNameDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or isAnonymous()")
    public ResponseEntity<UserDTO> addCustomer(@Valid @RequestBody UserDTO userDTO) {
        var user = User.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .build();
        var savedUser = userService.saveCustomer(user);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).body(UserDTO.toDto(savedUser));
    }


    @ExceptionHandler(UserEmailDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String emailExists(){
        return "User with given email already exists";
    }

    @ExceptionHandler(UserNameDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nameExists(){
        return "User with given name already exists";
    }

}
