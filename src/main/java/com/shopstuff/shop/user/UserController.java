package com.shopstuff.shop.user;


import com.shopstuff.shop.exceptions.UserEmailDuplicateException;
import com.shopstuff.shop.exceptions.NameDuplicateException;
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
                .password(userService.encode(userDTO.getPassword()))
                .build();
        var savedUser = userService.saveCustomer(user);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).body(UserDTO.toDto(savedUser));
    }

    @PostMapping("/worker")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addWorker(@Valid @RequestBody UserWorkerDTO userWorkerDTO){
        var user=User.builder()
                .email(userWorkerDTO.getEmail())
                .name(userWorkerDTO.getName())
                .password(userService.encode(userWorkerDTO.getPassword()))
                .roles(userWorkerDTO.getRoles())
                .build();
        var savedUser = userService.saveWorker(user);
        return ResponseEntity.created(URI.create(("/user?"+user.getId()))).body(UserDTO.toDto(savedUser));
    }

    @ExceptionHandler(UserEmailDuplicateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with given email already exists")
    public void emailExists() {
    }

    @ExceptionHandler(NameDuplicateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with given name already exists")
    public void nameExists() {
    }

}
