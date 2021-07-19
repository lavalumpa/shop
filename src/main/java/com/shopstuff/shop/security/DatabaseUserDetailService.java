package com.shopstuff.shop.security;

import com.shopstuff.shop.exceptions.NotFoundException;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class DatabaseUserDetailService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByName(username).orElseThrow(NotFoundException::new);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRoles()
                        .stream()
                        .map(Enum::toString)
                        .toArray(String[]::new))
                .build();
    }


}
