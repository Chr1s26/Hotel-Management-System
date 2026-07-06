package com.project.HotelManagementSystem.dto.checkout;

import com.project.HotelManagementSystem.entity.constants.CurrencyType;

public class CheckoutResponse {
    public Long bookingId;
    public String paymentIntentId;
    public String clientSecret;
    public long amountMinorUnits;
    public CurrencyType currency;

    public CheckoutResponse(Long bookingId, String paymentIntentId, String clientSecret,
                            long amountMinorUnits, CurrencyType currency) {
        this.bookingId = bookingId;
        this.paymentIntentId = paymentIntentId;
        this.clientSecret = clientSecret;
        this.amountMinorUnits = amountMinorUnits;
        this.currency = currency;
    }
}
