package com.shopstuff.shop.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

   public User adduser(User user){
        return userRepository.save(user);
    }



}
