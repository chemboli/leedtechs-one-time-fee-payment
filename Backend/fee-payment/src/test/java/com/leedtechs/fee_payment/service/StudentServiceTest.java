package com.leedtechs.fee_payment.service;

import com.leedtechs.fee_payment.dto.StudentRequest;
import com.leedtechs.fee_payment.dto.StudentResponse;
import com.leedtechs.fee_payment.entity.StudentAccount;
import com.leedtechs.fee_payment.repository.StudentAccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentAccountRepository studentRepo;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private StudentRequest buildRequest(String number, String name, String balance) {
        StudentRequest req = new StudentRequest();
        req.setStudentNumber(number);
        req.setFullName(name);
        req.setBalance(new BigDecimal(balance));
        return req;
    }

    private StudentAccount buildAccount(String number, String name, String balance) {
        StudentAccount account = new StudentAccount();
        account.setStudentNumber(number);
        account.setFullName(name);
        account.setBalance(new BigDecimal(balance));
        return account;
    }


    @Test
    void createStudent_success() {

        StudentRequest request = buildRequest("S1", "Chemboli Roy", "500000");

        when(studentRepo.existsById("S1")).thenReturn(false);
        when(studentRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        StudentResponse response = studentService.create(request);

        assertEquals("S1", response.getStudentNumber());
        assertEquals("Chemboli Roy", response.getFullName());
        assertEquals(new BigDecimal("500000"), response.getBalance());

        verify(studentRepo).save(any(StudentAccount.class));
    }

    @Test
    void createStudent_studentAlreadyExists_throwsException() {

        StudentRequest request = buildRequest("S1", "Senase Yoh", "500000");

        when(studentRepo.existsById("S1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> studentService.create(request));

        verify(studentRepo, never()).save(any());
    }


    @Test
    void getStudent_success() {

        StudentAccount account = buildAccount("S2", "Chemboli Roy", "300000");

        when(studentRepo.findById("S2")).thenReturn(Optional.of(account));

        StudentResponse response = studentService.getStudent("S2");

        assertEquals("S2", response.getStudentNumber());
        assertEquals("Chemboli Roy", response.getFullName());
        assertEquals(new BigDecimal("300000"), response.getBalance());
    }

    @Test
    void getStudent_notFound_throwsException() {

        when(studentRepo.findById("INVALID"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> studentService.getStudent("INVALID"));
    }
}