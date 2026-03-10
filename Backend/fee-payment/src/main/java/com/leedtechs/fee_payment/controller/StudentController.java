package com.leedtechs.fee_payment.controller;

import com.leedtechs.fee_payment.dto.StudentRequest;
import com.leedtechs.fee_payment.dto.StudentResponse;
import com.leedtechs.fee_payment.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.create(request));
    }
    @GetMapping("/{studentNumber}")
    public ResponseEntity<StudentResponse> getStudent(
            @PathVariable String studentNumber) {
        return ResponseEntity.ok(studentService.getStudent(studentNumber));
    }
}
