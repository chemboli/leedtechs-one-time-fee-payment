package com.leedtechs.fee_payment.repository;

import com.leedtechs.fee_payment.entity.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, String> {
}

