package com.leedtechs.fee_payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leedtechs.fee_payment.dto.StudentRequest;
import com.leedtechs.fee_payment.dto.StudentResponse;
import com.leedtechs.fee_payment.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Written by Chemboli Roy
@ExtendWith(SpringExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;
    private StudentController studentController;

    @Mock
    private StudentService studentService; // mocked service

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentController = new StudentController(studentService);

        // Include global exception handler so JSON errors are handled
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createStudent_success() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setStudentNumber("S1");
        request.setFullName("Chemboli Roy");
        request.setBalance(new BigDecimal("500000"));

        StudentResponse response = new StudentResponse();
        response.setStudentNumber("S1");
        response.setFullName("Chemboli Roy");
        response.setBalance(new BigDecimal("500000"));

        when(studentService.create(any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentNumber").value("S1"))
                .andExpect(jsonPath("$.fullName").value("Chemboli Roy"))
                .andExpect(jsonPath("$.balance").value(500000));

        verify(studentService).create(any(StudentRequest.class));
    }

    @Test
    void createStudent_alreadyExists_throwsException() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setStudentNumber("S1");
        request.setFullName("Chemboli Roy");
        request.setBalance(new BigDecimal("500000"));

        when(studentService.create(any(StudentRequest.class)))
                .thenThrow(new IllegalArgumentException("Student already exists: S1"));

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Student already exists: S1"));

        verify(studentService).create(any(StudentRequest.class));
    }

    @Test
    void getStudent_success() throws Exception {
        StudentResponse response = new StudentResponse();
        response.setStudentNumber("S2");
        response.setFullName("Chemboli Roy Jr");
        response.setBalance(new BigDecimal("300000"));

        when(studentService.getStudent("S2")).thenReturn(response);

        mockMvc.perform(get("/students/S2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentNumber").value("S2"))
                .andExpect(jsonPath("$.fullName").value("Chemboli Roy Jr"))
                .andExpect(jsonPath("$.balance").value(300000));

        verify(studentService).getStudent("S2");
    }

    @Test
    void getStudent_notFound_throwsException() throws Exception {
        when(studentService.getStudent("INVALID"))
                .thenThrow(new IllegalArgumentException("Student not found: INVALID"));

        mockMvc.perform(get("/students/INVALID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Student not found: INVALID"));

        verify(studentService).getStudent("INVALID");
    }
}