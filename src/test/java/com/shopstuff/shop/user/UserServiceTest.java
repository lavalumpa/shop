package com.shopstuff.shop.user;


import com.shopstuff.shop.cart.Cart;


import com.shopstuff.shop.cart.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void testingUserAndCartCreationWithSaveUserMethod() {
        var user = User.builder().name("Steve").email("steve705@yahoo.com").password("4az5j@98gbmawq").build();
        userService.saveUser(user);
        verify(userRepository).save(userCaptor.capture());
        verify(cartService).createCart(eq(user));
        assertEquals(user,userCaptor.getValue());
    }

}
