package com.shopstuff.shop.user;


import com.shopstuff.shop.exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        userService.saveUser(user);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).body(user);
    }

}
