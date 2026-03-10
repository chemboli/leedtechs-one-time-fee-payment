package com.leedtechs.fee_payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StudentResponse {

    private String studentNumber;
    private String fullName;
    private BigDecimal balance;
    private LocalDate nextDueDate;

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDate getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(LocalDate nextDueDate) { this.nextDueDate = nextDueDate; }
}
