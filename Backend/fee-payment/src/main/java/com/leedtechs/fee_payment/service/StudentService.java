package com.leedtechs.fee_payment.service;

import com.leedtechs.fee_payment.dto.StudentRequest;
import com.leedtechs.fee_payment.dto.StudentResponse;
import com.leedtechs.fee_payment.entity.StudentAccount;
import com.leedtechs.fee_payment.repository.StudentAccountRepository;

import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentAccountRepository studentRepo;

    public StudentService(StudentAccountRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public StudentResponse create(StudentRequest request) {

        if (studentRepo.existsById(request.getStudentNumber())) {
            throw new IllegalArgumentException(
                    "Student already exists: " + request.getStudentNumber());
        }

        StudentAccount account = new StudentAccount();
        account.setStudentNumber(request.getStudentNumber());
        account.setFullName(request.getFullName());
        account.setBalance(request.getBalance());
        studentRepo.save(account);

        StudentResponse response = new StudentResponse();
        response.setStudentNumber(account.getStudentNumber());
        response.setFullName(account.getFullName());
        response.setBalance(account.getBalance());
        response.setNextDueDate(account.getNextDueDate());

        return response;
    }
    public StudentResponse getStudent(String studentNumber) {

        StudentAccount account = studentRepo
                .findById(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found: " + studentNumber));

        StudentResponse response = new StudentResponse();
        response.setStudentNumber(account.getStudentNumber());
        response.setFullName(account.getFullName());
        response.setBalance(account.getBalance());
        response.setNextDueDate(account.getNextDueDate());

        return response;
    }
}
