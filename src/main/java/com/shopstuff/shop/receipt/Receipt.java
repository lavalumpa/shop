package com.shopstuff.shop.receipt;

import com.shopstuff.shop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name= "userId")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "receipt", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<ReceiptItem> receiptItems=new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    private int totalPrice;

}
