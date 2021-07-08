package com.shopstuff.shop.user;


import com.shopstuff.shop.exceptions.UserEmailDuplicateException;
import com.shopstuff.shop.exceptions.UserNameDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or isAnonymous()")
    public ResponseEntity<UserDTO> addCustomer(@Valid @RequestBody UserDTO userDTO) {
        var encodedPassword= passwordEncoder.encode(userDTO.getPassword());
        var user = User.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .password(encodedPassword)
                .build();
        var savedUser = userService.saveCustomer(user);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).body(UserDTO.toDto(savedUser));
    }

    @PostMapping("/worker")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addWorker(@Valid @RequestBody UserWorkerDTO userWorkerDTO){
        var encodedPassword = passwordEncoder.encode(userWorkerDTO.getPassword());
        var user=User.builder()
                .email(userWorkerDTO.getEmail())
                .name(userWorkerDTO.getName())
                .password(encodedPassword)
                .roles(userWorkerDTO.getRoles())
                .build();
        var savedUser = userService.saveWorker(user);
        return ResponseEntity.created(URI.create(("/user?"+user.getId()))).body(UserDTO.toDto(savedUser));
    }

    @ExceptionHandler(UserEmailDuplicateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with given email already exists")
    public void emailExists() {
    }

    @ExceptionHandler(UserNameDuplicateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with given name already exists")
    public void nameExists() {
    }

}
