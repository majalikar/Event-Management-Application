package com.DesignMyNight.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenResponse {
    private String paymentMethodId;

    public TokenResponse(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}

