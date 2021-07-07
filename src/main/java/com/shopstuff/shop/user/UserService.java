package com.shopstuff.shop.user;


import com.shopstuff.shop.cart.CartService;
import com.shopstuff.shop.exceptions.UserEmailDuplicateException;
import com.shopstuff.shop.exceptions.UserNameDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartService cartService;

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional
    public User saveCustomer(User user) {
        if (userRepository.existsByName(user.getName())) throw new UserNameDuplicateException();
        if (userRepository.existsByEmail(user.getEmail())) throw new UserEmailDuplicateException();
        user.addRole(Role.CUSTOMER);
        var savedUser = userRepository.save(user);
        cartService.createCart(savedUser);
        return savedUser;
    }

    @Transactional
    public User saveWorker(User user){
        if (userRepository.existsByName(user.getName())) throw new UserNameDuplicateException();
        if (userRepository.existsByEmail(user.getEmail())) throw new UserEmailDuplicateException();
        return userRepository.save(user);
    }

    public boolean correctUser(String username, int id) {
        return userRepository.findById(id)
                .map(User::getName)
                .filter(x -> x.equals(username))
                .isPresent();
    }


}
