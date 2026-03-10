import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../../services/payment.service';
import { ToastService } from '../../services/toast.service';
import { FormatService } from '../../services/format.service';
import { StudentResponse } from '../../models/api.models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  studentNumber = '';
  fullName = '';
  balance: number | null = null;

  loading = signal(false);
  result = signal<StudentResponse | null>(null);
  errorMessage = signal<string | null>(null);

  constructor(
    private paymentService: PaymentService,
    private toast: ToastService,
    public fmt: FormatService,
  ) {}

  submit(): void {
    if (!this.studentNumber.trim() || !this.fullName.trim() || !this.balance) {
      this.toast.show('Please fill in all fields.', 'error');
      return;
    }

    this.loading.set(true);
    this.result.set(null);
    this.errorMessage.set(null);

    this.paymentService
      .createStudent({
        studentNumber: this.studentNumber.trim(),
        fullName: this.fullName.trim(),
        balance: this.balance,
      })
      .subscribe({
        next: (res) => {
          this.result.set(res);
          this.loading.set(false);
          this.toast.show('Student registered successfully!');
          this.studentNumber = '';
          this.fullName = '';
          this.balance = null;
        },
        error: (err: HttpErrorResponse) => {
          const msg = err.error?.message || 'Registration failed. Please try again.';
          this.errorMessage.set(msg);
          this.loading.set(false);
          this.toast.show('Registration failed.', 'error');
        },
      });
  }
}
