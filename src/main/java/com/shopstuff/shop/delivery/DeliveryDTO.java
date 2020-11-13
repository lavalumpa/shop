package com.shopstuff.shop.delivery;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    @JsonProperty("deliver")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean deliveryRequested;
    @JsonProperty("user_id")
    private int userId;
    private Address address;
    @JsonProperty("state")
    private DeliveryState deliveryState;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;
    @JsonProperty("estimated_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate estimatedDate;
    @JsonProperty("delivered_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime deliveredAt;


    public boolean infoProvided() {
        return this.getAddress() != null &&
                this.getAddress().getCity() != null &&
                this.getAddress().getStreet() != null &&
                this.getAddress().getNumber() != null;
    }

    public static DeliveryDTO toDTO(Delivery delivery) {
        var address = Address.builder()
                .city(delivery.getCity())
                .number(delivery.getNumber())
                .street(delivery.getStreet()).build();
        return DeliveryDTO.builder()
                .userId(delivery.getUser().getId())
                .address(address)
                .deliveryState(delivery.getDeliveryState())
                .estimatedDate(delivery.getEstimatedDate())
                .createdAt(delivery.getCreatedAt())
                .deliveredAt(delivery.getDeliveredOn()).build();
    }
}
