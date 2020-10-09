package com.shopstuff.shop.receipt.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReportItemDto {
    @JsonProperty("id")
    private int itemId;
    private int quantity;

    public static ReportItemDto toDto(ReportItem reportItem){
        var reportItemDto=new ReportItemDto();
        reportItemDto.itemId=reportItem.getItem().getId();
        reportItemDto.quantity=reportItem.getQuantity();
        return reportItemDto;
    }
}
