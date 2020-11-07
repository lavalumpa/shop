package com.shopstuff.shop.delivery;

import com.shopstuff.shop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String city;

    private String street;

    private Integer number;

    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDate estimatedDate;

    private LocalDateTime deliveredOn;


}
