package com.shopstuff.shop.user;



import com.shopstuff.shop.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;

    @Transactional
    public User saveUser(User user){
        userRepository.save(user);
        return shoppingCartService.createShoppingCart(user).getUser();
    }




}
