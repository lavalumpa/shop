package com.shopstuff.shop.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReceiptItemDTO {
    @JsonProperty("id")
    private int itemId;
    private int price;
    private int quantity;

    public static ReceiptItemDTO toDTO(ReceiptItem receiptItem){
        return ReceiptItemDTO.builder()
                .itemId(receiptItem.getItem().getId())
                .price(receiptItem.getItem().getPrice())
                .quantity(receiptItem.getQuantity())
                .build();
    }
}
