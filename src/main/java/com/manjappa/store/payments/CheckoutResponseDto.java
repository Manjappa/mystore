package com.manjappa.store.payments;

import lombok.Data;

@Data
public class CheckoutResponseDto {
    private Long orderId;
    private  String checkoutUrl;
    public CheckoutResponseDto(Long orderId, String checkoutUrl) {
        this.orderId = orderId;
        this.checkoutUrl = checkoutUrl;
    }
}
