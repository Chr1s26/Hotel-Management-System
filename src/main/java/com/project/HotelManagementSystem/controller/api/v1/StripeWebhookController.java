package com.project.HotelManagementSystem.controller.api.v1;

import com.project.HotelManagementSystem.service.CheckoutService;
import com.project.HotelManagementSystem.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Stripe webhook. This — not the checkout response — is what finalizes a booking.
 * Must be permitted without JWT (Stripe calls it server-to-server; the signature is the auth).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stripe")
public class StripeWebhookController {

    private final StripeService stripeService;
    private final CheckoutService checkoutService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handle(@RequestBody String payload,
                                         @RequestHeader("Stripe-Signature") String signature) {
        Event event;
        try {
            event = stripeService.constructEvent(payload, signature);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        String intentId = extractPaymentIntentId(event);
        if (intentId == null) return ResponseEntity.ok("ignored");

        switch (event.getType()) {
            case "payment_intent.succeeded"      -> checkoutService.finalizeSuccessfulPayment(intentId);
            case "payment_intent.payment_failed" -> checkoutService.markPaymentFailed(intentId);
            default -> { /* ignore other event types */ }
        }
        return ResponseEntity.ok("ok"); // always 200 so Stripe stops retrying
    }

    private String extractPaymentIntentId(Event event) {
        Optional<StripeObject> obj = event.getDataObjectDeserializer().getObject();
        if (obj.isPresent() && obj.get() instanceof PaymentIntent pi) return pi.getId();
        return null;
    }
}
