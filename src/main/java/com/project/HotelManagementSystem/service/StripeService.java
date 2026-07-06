package com.project.HotelManagementSystem.service;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Thin wrapper over the Stripe API. Keys come from env/config (never committed):
 *   stripe.secret-key, stripe.webhook-secret
 */
@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /** amountMinorUnits = cents for USD, satang for THB (both x100). currency = "usd" | "thb". */
    public PaymentIntent createPaymentIntent(long amountMinorUnits, String currency,
                                             Map<String, String> metadata) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountMinorUnits)
                .setCurrency(currency.toLowerCase())
                .putAllMetadata(metadata)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true).build())
                .build();
        return PaymentIntent.create(params);
    }

    /** Full refund of a settled PaymentIntent — used when a booking loses the room race. */
    public Refund refundPaymentIntent(String paymentIntentId) throws StripeException {
        return Refund.create(RefundCreateParams.builder()
                .setPaymentIntent(paymentIntentId).build());
    }

    /** Verifies the Stripe-Signature header. Throws if the payload was tampered with. */
    public Event constructEvent(String payload, String sigHeader) throws SignatureVerificationException {
        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }
}
