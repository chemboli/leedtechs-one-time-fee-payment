package com.leedtechs.fee_payment.service;

import com.leedtechs.fee_payment.dto.OneTimePaymentRequest;
import com.leedtechs.fee_payment.dto.OneTimePaymentResponse;
import com.leedtechs.fee_payment.entity.FeePayment;
import com.leedtechs.fee_payment.entity.StudentAccount;
import com.leedtechs.fee_payment.repository.FeePaymentRepository;
import com.leedtechs.fee_payment.repository.StudentAccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private StudentAccountRepository studentRepo;

    @Mock
    private FeePaymentRepository paymentRepo;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private StudentAccount buildAccount(String number, BigDecimal balance) {
        StudentAccount account = new StudentAccount();
        account.setStudentNumber(number);
        account.setBalance(balance);
        return account;
    }

    private OneTimePaymentRequest buildRequest(String studentNumber, String amount, String date) {
        OneTimePaymentRequest req = new OneTimePaymentRequest();
        req.setStudentNumber(studentNumber);
        req.setPaymentAmount(new BigDecimal(amount));
        req.setPaymentDate(LocalDate.parse(date));
        return req;
    }

    @Test
    void example1_paymentWithIncentive() {
        StudentAccount account = buildAccount("A1", new BigDecimal("800000"));
        when(studentRepo.findById("A1")).thenReturn(Optional.of(account));
        when(studentRepo.save(any())).thenReturn(account);
        when(paymentRepo.save(any())).thenReturn(new FeePayment());

        OneTimePaymentRequest req = buildRequest("A1", "100000", "2026-03-14");
        OneTimePaymentResponse res = paymentService.process(req);

        assertEquals(0, res.getPreviousBalance().compareTo(new BigDecimal("800000")));
        assertEquals(0, res.getIncentiveRate().compareTo(new BigDecimal("0.03")));
        assertEquals(0, res.getIncentiveAmount().compareTo(new BigDecimal("3000.00")));
        assertEquals(0, res.getNewBalance().compareTo(new BigDecimal("697000.00")));
        assertNotNull(res.getNextPaymentDueDate());
    }

    @Test
    void studentNotFound_throwsException() {
        when(studentRepo.findById("INVALID")).thenReturn(Optional.empty());
        OneTimePaymentRequest req = buildRequest("INVALID", "1000", "2026-03-10");

        assertThrows(IllegalArgumentException.class, () -> paymentService.process(req));
    }

    @Test
    void usesTodayWhenPaymentDateIsNull() {
        StudentAccount account = buildAccount("A5", new BigDecimal("500000"));
        when(studentRepo.findById("A5")).thenReturn(Optional.of(account));
        when(studentRepo.save(any())).thenReturn(account);
        when(paymentRepo.save(any())).thenReturn(new FeePayment());

        OneTimePaymentRequest req = new OneTimePaymentRequest();
        req.setStudentNumber("A5");
        req.setPaymentAmount(new BigDecimal("1000"));

        OneTimePaymentResponse res = paymentService.process(req);

        assertNotNull(res.getNextPaymentDueDate());
        assertTrue(res.getNewBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void fullPayment_adjustedByIncentive() {
        StudentAccount account = buildAccount("A6", new BigDecimal("50000"));

        BigDecimal originalBalance = account.getBalance(); // ✅ store before mutation

        when(studentRepo.findById("A6")).thenReturn(Optional.of(account));
        when(studentRepo.save(any())).thenReturn(account);
        when(paymentRepo.save(any())).thenReturn(new FeePayment());

        OneTimePaymentRequest req = new OneTimePaymentRequest();
        req.setStudentNumber("A6");
        req.setPaymentAmount(new BigDecimal("50000"));

        OneTimePaymentResponse res = paymentService.process(req);

        BigDecimal expectedRate = res.getIncentiveRate();
        BigDecimal expectedPayment = originalBalance
                .divide(BigDecimal.ONE.add(expectedRate), 2, RoundingMode.HALF_UP);
        BigDecimal expectedIncentive = originalBalance.subtract(expectedPayment);

        //  Use originalBalance instead of mutated account
        assertEquals(0, res.getPreviousBalance().compareTo(originalBalance));
        assertEquals(0, res.getPaymentAmount().compareTo(expectedPayment));
        assertEquals(0, res.getIncentiveAmount().compareTo(expectedIncentive));
        assertEquals(0, res.getNewBalance().compareTo(BigDecimal.ZERO));
    }
    @Test
    void overPayment_throwsException() {
        StudentAccount account = buildAccount("A7", new BigDecimal("800000"));
        when(studentRepo.findById("A7")).thenReturn(Optional.of(account));

        OneTimePaymentRequest req = new OneTimePaymentRequest();
        req.setStudentNumber("A7");
        req.setPaymentAmount(new BigDecimal("1000000")); // more than balance

        // Overpayment should throw exception
        assertThrows(IllegalArgumentException.class, () -> paymentService.process(req));
    }

    @Test
    void noDueDateWhenBalanceIsZero() {
        StudentAccount account = buildAccount("A4", new BigDecimal("1000"));
        when(studentRepo.findById("A4")).thenReturn(Optional.of(account));
        when(studentRepo.save(any())).thenReturn(account);
        when(paymentRepo.save(any())).thenReturn(new FeePayment());

        OneTimePaymentRequest req = buildRequest("A4", "1000", "2026-03-10");
        OneTimePaymentResponse res = paymentService.process(req);

        assertNull(res.getNextPaymentDueDate());
        assertEquals(0, res.getNewBalance().compareTo(BigDecimal.ZERO));
    }
}