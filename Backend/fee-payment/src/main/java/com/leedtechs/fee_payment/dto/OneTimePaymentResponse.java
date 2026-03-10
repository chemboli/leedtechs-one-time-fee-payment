package com.leedtechs.fee_payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OneTimePaymentResponse {

    private String studentNumber;
    private BigDecimal previousBalance;
    private BigDecimal paymentAmount;
    private BigDecimal incentiveRate;
    private BigDecimal incentiveAmount;
    private BigDecimal newBalance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextPaymentDueDate;

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public BigDecimal getPreviousBalance() { return previousBalance; }
    public void setPreviousBalance(BigDecimal previousBalance) { this.previousBalance = previousBalance; }

    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

    public BigDecimal getIncentiveRate() { return incentiveRate; }
    public void setIncentiveRate(BigDecimal incentiveRate) { this.incentiveRate = incentiveRate; }

    public BigDecimal getIncentiveAmount() { return incentiveAmount; }
    public void setIncentiveAmount(BigDecimal incentiveAmount) { this.incentiveAmount = incentiveAmount; }

    public BigDecimal getNewBalance() { return newBalance; }
    public void setNewBalance(BigDecimal newBalance) { this.newBalance = newBalance; }

    public LocalDate getNextPaymentDueDate() { return nextPaymentDueDate; }
    public void setNextPaymentDueDate(LocalDate nextPaymentDueDate) { this.nextPaymentDueDate = nextPaymentDueDate; }
}

