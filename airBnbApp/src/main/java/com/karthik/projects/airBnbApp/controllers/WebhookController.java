package com.karthik.projects.airBnbApp.controllers;

import com.karthik.projects.airBnbApp.services.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook.stripe}")
    private String endPointSecret;

    @PostMapping("/payment")
    public ResponseEntity<Void> capturePayments(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
        try {
            Event event = Webhook.constructEvent(payload,sigHeader,endPointSecret);
            bookingService.capturePayments(event);
            return ResponseEntity.noContent().build();
        }catch (SignatureVerificationException e){
            throw new RuntimeException(e);
        }
    }
}
