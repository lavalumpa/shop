package com.shopstuff.shop.user;



import com.shopstuff.shop.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartService cartService;

    public Optional<User> findById(int id){
        return userRepository.findById(id);
    }

    @Transactional
    public User saveUser(User user){
        var savedUser = userRepository.save(user);
        cartService.createCart(savedUser);
        return savedUser;
    }




}
