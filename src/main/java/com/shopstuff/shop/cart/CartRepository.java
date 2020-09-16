package com.shopstuff.shop.cart;

import com.shopstuff.shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
}
