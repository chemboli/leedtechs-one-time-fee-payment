package com.leedtechs.fee_payment.service;

import com.leedtechs.fee_payment.dto.OneTimePaymentRequest;
import com.leedtechs.fee_payment.dto.OneTimePaymentResponse;
import com.leedtechs.fee_payment.entity.FeePayment;
import com.leedtechs.fee_payment.entity.StudentAccount;
import com.leedtechs.fee_payment.repository.FeePaymentRepository;
import com.leedtechs.fee_payment.repository.StudentAccountRepository;
import com.leedtechs.fee_payment.util.DueDateCalculator;
import com.leedtechs.fee_payment.util.IncentiveCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class PaymentService {

    private final StudentAccountRepository studentRepo;
    private final FeePaymentRepository paymentRepo;

    public PaymentService(StudentAccountRepository studentRepo,
                          FeePaymentRepository paymentRepo) {
        this.studentRepo = studentRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public OneTimePaymentResponse process(OneTimePaymentRequest request) {

        validateRequest(request);

        StudentAccount account = studentRepo
                .findById(request.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found: " + request.getStudentNumber()));

        BigDecimal previousBalance = account.getBalance();
        BigDecimal requestedAmount = request.getPaymentAmount();

        LocalDate paymentDate = (request.getPaymentDate() != null)
                ? request.getPaymentDate()
                : LocalDate.now();

        // OVERPAYMENT CHECK
        if (requestedAmount.compareTo(previousBalance) > 0) {
            throw new IllegalArgumentException(
                    "Payment exceeds remaining balance. Balance: " + previousBalance);
        }

        BigDecimal rate = IncentiveCalculator.getRate(requestedAmount);
        BigDecimal actualPayment;
        BigDecimal incentiveAmount;

        // FULL PAYMENT CASE
        if (requestedAmount.compareTo(previousBalance) == 0) {

            // Recalculate payment so payment + incentive = balance
            actualPayment = previousBalance.divide(
                    BigDecimal.ONE.add(rate), 2, RoundingMode.HALF_UP);

            incentiveAmount = previousBalance.subtract(actualPayment);

        } else {
            //PARTIAL PAYMENT
            actualPayment = requestedAmount;

            incentiveAmount = actualPayment
                    .multiply(rate)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal newBalance = previousBalance.subtract(
                actualPayment.add(incentiveAmount));

        // Safety clamp
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }

        LocalDate nextDueDate = (newBalance.compareTo(BigDecimal.ZERO) > 0)
                ? DueDateCalculator.calculate(paymentDate)
                : null;

        // UPDATE ACCOUNT
        account.setBalance(newBalance);
        account.setNextDueDate(nextDueDate);
        studentRepo.save(account);

        // SAVE PAYMENT
        FeePayment payment = new FeePayment();
        payment.setStudentNumber(request.getStudentNumber());
        payment.setPaymentAmount(actualPayment);
        payment.setIncentiveRate(rate);
        payment.setIncentiveAmount(incentiveAmount);
        payment.setPaymentDate(paymentDate);
        paymentRepo.save(payment);

        // BUILD RESPONSE
        return buildResponse(
                request.getStudentNumber(),
                previousBalance,
                actualPayment,
                rate,
                incentiveAmount,
                newBalance,
                nextDueDate
        );
    }


    // Helper Methods

    private void validateRequest(OneTimePaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.getStudentNumber() == null || request.getStudentNumber().isBlank()) {
            throw new IllegalArgumentException("Student number is required");
        }

        if (request.getPaymentAmount() == null
                || request.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }

    private OneTimePaymentResponse buildResponse(
            String studentNumber,
            BigDecimal previousBalance,
            BigDecimal paymentAmount,
            BigDecimal rate,
            BigDecimal incentiveAmount,
            BigDecimal newBalance,
            LocalDate nextDueDate
    ) {
        OneTimePaymentResponse response = new OneTimePaymentResponse();
        response.setStudentNumber(studentNumber);
        response.setPreviousBalance(previousBalance);
        response.setPaymentAmount(paymentAmount);
        response.setIncentiveRate(rate);
        response.setIncentiveAmount(incentiveAmount);
        response.setNewBalance(newBalance);
        response.setNextPaymentDueDate(nextDueDate);
        return response;
    }
}