package com.shopstuff.shop.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class ReceiptDTO {
    private int id;
    @JsonProperty("items")
    private List<ReceiptItemDTO>  receiptItemDTOS;
    private int totalPrice;
    private LocalDateTime purchasedAt;
    @JsonProperty("purchasedBy")
    private int userId;

    public static ReceiptDTO toDTO(Receipt receipt){
        var receiptDTO= ReceiptDTO.builder()
                .id(receipt.getId())
                .purchasedAt(LocalDateTime.parse(receipt.getCreatedAt().toString()))
                .totalPrice(receipt.getTotalPrice())
                .userId(receipt.getUser().getId())
                .build();
        receiptDTO.setReceiptItemDTOS(receipt.getReceiptItems()
                .stream()
                .map(ReceiptItemDTO::toDTO)
                .collect(Collectors.toList()));
        return receiptDTO;
    }
}
