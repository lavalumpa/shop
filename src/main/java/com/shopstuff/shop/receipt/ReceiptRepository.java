package com.shopstuff.shop.receipt;

import com.shopstuff.shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    List<Receipt> findByUser(User user);

    List<Receipt> findAllByCreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
}
