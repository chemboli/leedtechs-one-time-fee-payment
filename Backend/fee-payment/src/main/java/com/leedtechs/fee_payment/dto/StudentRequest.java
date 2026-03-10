package com.leedtechs.fee_payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class StudentRequest {

    @NotBlank(message = "Student number is required")
    private String studentNumber;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Balance is required")
    @Positive(message = "Balance must be greater than zero")
    private BigDecimal balance;

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
