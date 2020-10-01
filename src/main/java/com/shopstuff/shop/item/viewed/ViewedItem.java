package com.shopstuff.shop.item.viewed;


import com.shopstuff.shop.item.Item;
import com.shopstuff.shop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ViewedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name= "userId")
    @NotNull(message = "User is required")
    private User user;

    @OneToOne
    @JoinColumn (name= "itemId")
    @NotNull(message = "Item is required")
    private Item item;

    @LastModifiedDate
    private LocalDateTime lastViewed;
}
