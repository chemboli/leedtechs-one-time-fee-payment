import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../../services/payment.service';
import { FormatService } from '../../services/format.service';
import { StudentResponse } from '../../models/payment.models';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent {

  studentNumber = '';
  fullName = '';
  balance: number | null = null;
  loading = false;
  result: StudentResponse | null = null;
  errorMsg = '';

  constructor(private paymentService: PaymentService, public fmt: FormatService) {}

  submit(): void {
    if (!this.studentNumber.trim()) { this.errorMsg = 'Student number is required.'; return; }
    if (!this.fullName.trim())      { this.errorMsg = 'Full name is required.'; return; }
    if (!this.balance || this.balance <= 0) { this.errorMsg = 'Balance must be greater than zero.'; return; }

    this.loading = true;
    this.result = null;
    this.errorMsg = '';

    this.paymentService.registerStudent({
      studentNumber: this.studentNumber.trim(),
      fullName: this.fullName.trim(),
      balance: this.balance,
    }).subscribe({
      next: (res) => {
        this.result = res;
        this.loading = false;
        this.studentNumber = '';
        this.fullName = '';
        this.balance = null;
      },
      error: (err: HttpErrorResponse) => {
        this.errorMsg = err.error?.message || 'Registration failed. Student may already exist.';
        this.loading = false;
      },
    });
  }
}
