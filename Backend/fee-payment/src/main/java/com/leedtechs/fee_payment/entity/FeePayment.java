package com.leedtechs.fee_payment.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_payments")
public class FeePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_number", nullable = false)
    private String studentNumber;

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "incentive_rate", nullable = false)
    private BigDecimal incentiveRate;

    @Column(name = "incentive_amount", nullable = false)
    private BigDecimal incentiveAmount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    public Long getId() { return id; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

    public BigDecimal getIncentiveRate() { return incentiveRate; }
    public void setIncentiveRate(BigDecimal incentiveRate) { this.incentiveRate = incentiveRate; }

    public BigDecimal getIncentiveAmount() { return incentiveAmount; }
    public void setIncentiveAmount(BigDecimal incentiveAmount) { this.incentiveAmount = incentiveAmount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
