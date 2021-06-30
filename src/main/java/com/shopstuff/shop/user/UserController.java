package com.shopstuff.shop.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
