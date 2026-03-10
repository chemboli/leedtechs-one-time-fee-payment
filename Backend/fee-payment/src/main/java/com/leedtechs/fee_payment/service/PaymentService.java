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

        // Fetch student account
        StudentAccount account = studentRepo
                .findById(request.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found: " + request.getStudentNumber()));

        BigDecimal previousBalance = account.getBalance();

        // Determine payment date (use provided or default to today)
        LocalDate paymentDate = request.getPaymentDate() != null
                ? request.getPaymentDate()
                : LocalDate.now();

        // Calculate incentive
        BigDecimal rate = IncentiveCalculator.getRate(request.getPaymentAmount());
        BigDecimal incentive = request.getPaymentAmount().multiply(rate);

        // Calculate new balance (floor at zero)
        BigDecimal totalDeduction = request.getPaymentAmount().add(incentive);
        BigDecimal newBalance = previousBalance.subtract(totalDeduction);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }

        // Calculate next due date (only if balance remains)
        LocalDate nextDueDate = newBalance.compareTo(BigDecimal.ZERO) > 0
                ? DueDateCalculator.calculate(paymentDate)
                : null;

        // Update account
        account.setBalance(newBalance);
        account.setNextDueDate(nextDueDate);
        studentRepo.save(account);

        // Save payment record
        FeePayment payment = new FeePayment();
        payment.setStudentNumber(request.getStudentNumber());
        payment.setPaymentAmount(request.getPaymentAmount());
        payment.setIncentiveRate(rate);
        payment.setIncentiveAmount(incentive);
        payment.setPaymentDate(paymentDate);
        paymentRepo.save(payment);

        // Build response
        OneTimePaymentResponse response = new OneTimePaymentResponse();
        response.setStudentNumber(request.getStudentNumber());
        response.setPreviousBalance(previousBalance);
        response.setPaymentAmount(request.getPaymentAmount());
        response.setIncentiveRate(rate);
        response.setIncentiveAmount(incentive);
        response.setNewBalance(newBalance);
        response.setNextPaymentDueDate(nextDueDate);

        return response;
    }
}
