package com.leedtechs.fee_payment.controller;

import com.leedtechs.fee_payment.dto.OneTimePaymentRequest;
import com.leedtechs.fee_payment.dto.OneTimePaymentResponse;
import com.leedtechs.fee_payment.service.PaymentService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/one-time-fee-payment")
    public ResponseEntity<OneTimePaymentResponse> processPayment(
            @Valid @RequestBody OneTimePaymentRequest request) {
        return ResponseEntity.ok(paymentService.process(request));
    }
}

