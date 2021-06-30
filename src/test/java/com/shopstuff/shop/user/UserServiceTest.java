package com.shopstuff.shop.user;


import com.shopstuff.shop.cart.CartService;
import com.shopstuff.shop.exceptions.UserEmailDuplicateException;
import com.shopstuff.shop.exceptions.UserNameDuplicateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private UserRepository userRepository;


    @Test
    public void testingUserAndCartCreationWithSaveUserMethod() {
        var user = createSteve();
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        userService.saveCustomer(user);
        verify(cartService).createCart(eq(user));
    }

    @Test
    public void testingUserWithNameExistsAlready(){
        var user=createSteve();
        when(userRepository.existsByName(user.getName())).thenReturn(true);
        assertThrows(UserNameDuplicateException.class,()->userService.saveCustomer(user));
    }

    @Test
    public void testingUserWithEmailExistsAlready(){
        var user=createSteve();
        when(userRepository.existsByName(user.getName())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        assertThrows(UserEmailDuplicateException.class,()->userService.saveCustomer(user));
    }

    public User createSteve(){
        return User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
    }

}
