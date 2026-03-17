package com.leedtechs.fee_payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leedtechs.fee_payment.dto.OneTimePaymentRequest;
import com.leedtechs.fee_payment.dto.OneTimePaymentResponse;
import com.leedtechs.fee_payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Written by Chemboli Roy
@ExtendWith(SpringExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentController = new PaymentController(paymentService);

        // Initialize ObjectMapper with JavaTimeModule for LocalDate
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Include GlobalExceptionHandler so errors return JSON
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void processPayment_success() throws Exception {
        OneTimePaymentRequest request = new OneTimePaymentRequest();
        request.setStudentNumber("A1");
        request.setPaymentAmount(new BigDecimal("100000"));
        request.setPaymentDate(LocalDate.of(2026, 3, 14));

        OneTimePaymentResponse response = new OneTimePaymentResponse();
        response.setPreviousBalance(new BigDecimal("800000"));
        response.setNewBalance(new BigDecimal("697000.00"));
        response.setIncentiveAmount(new BigDecimal("3000.00"));
        response.setIncentiveRate(new BigDecimal("0.03"));
        response.setNextPaymentDueDate(LocalDate.of(2026, 6, 12));

        when(paymentService.process(any(OneTimePaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/one-time-fee-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previousBalance").value(800000))
                .andExpect(jsonPath("$.newBalance").value(697000.00))
                .andExpect(jsonPath("$.incentiveAmount").value(3000.00))
                .andExpect(jsonPath("$.incentiveRate").value(0.03))
                .andExpect(jsonPath("$.nextPaymentDueDate").value("2026-06-12"));

        verify(paymentService).process(any(OneTimePaymentRequest.class));
    }

    @Test
    void processPayment_studentNotFound_throwsException() throws Exception {
        OneTimePaymentRequest request = new OneTimePaymentRequest();
        request.setStudentNumber("INVALID");
        request.setPaymentAmount(new BigDecimal("1000"));

        when(paymentService.process(any(OneTimePaymentRequest.class)))
                .thenThrow(new IllegalArgumentException("Student not found: INVALID"));

        mockMvc.perform(post("/one-time-fee-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Student not found: INVALID"));

        verify(paymentService).process(any(OneTimePaymentRequest.class));
    }

    @Test
    void processPayment_nullPaymentDate_usesToday() throws Exception {
        OneTimePaymentRequest request = new OneTimePaymentRequest();
        request.setStudentNumber("A2");
        request.setPaymentAmount(new BigDecimal("5000"));
        // paymentDate intentionally null

        OneTimePaymentResponse response = new OneTimePaymentResponse();
        response.setPreviousBalance(new BigDecimal("500000"));
        response.setNewBalance(new BigDecimal("495000"));
        response.setIncentiveAmount(new BigDecimal("0"));
        response.setIncentiveRate(new BigDecimal("0.00"));
        response.setNextPaymentDueDate(LocalDate.now());

        when(paymentService.process(any(OneTimePaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/one-time-fee-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newBalance").value(495000))
                .andExpect(jsonPath("$.nextPaymentDueDate").value(LocalDate.now().toString()));

        verify(paymentService).process(any(OneTimePaymentRequest.class));
    }
}