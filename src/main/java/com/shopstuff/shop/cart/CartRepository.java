package com.shopstuff.shop.cart;


import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Integer> {
}
