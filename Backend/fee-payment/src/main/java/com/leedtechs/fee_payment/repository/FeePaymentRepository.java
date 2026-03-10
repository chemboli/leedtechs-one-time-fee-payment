package com.leedtechs.fee_payment.repository;

import com.leedtechs.fee_payment.entity.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {
}