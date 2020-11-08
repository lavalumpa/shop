package com.shopstuff.shop.receipt;

import com.shopstuff.shop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name= "userId")
    private User user;

    @OneToMany(mappedBy = "receipt", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @Builder.Default
    private List<ReceiptItem> receiptItems=new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @Positive(message ="Total price must be greater than 0")
    private int totalPrice;

    public void addReceiptItem(ReceiptItem receiptItem){
        receiptItems.add(receiptItem);
        receiptItem.setReceipt(this);
    }



}
