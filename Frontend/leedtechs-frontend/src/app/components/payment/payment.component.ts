import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../../services/payment.service';
import { ToastService } from '../../services/toast.service';
import { FormatService } from '../../services/format.service';
import { OneTimePaymentResponse } from '../../models/api.models';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css',
})
export class PaymentComponent {
  studentNumber = '';
  paymentAmount: number | null = null;
  paymentDate = '';

  loading = signal(false);
  result = signal<OneTimePaymentResponse | null>(null);
  errorMessage = signal<string | null>(null);

  constructor(
    private paymentService: PaymentService,
    private toast: ToastService,
    public fmt: FormatService,
  ) {}

  get incentiveInfo() {
    if (!this.paymentAmount || this.paymentAmount <= 0) return null;
    const info = this.fmt.incentiveRate(this.paymentAmount);
    return {
      ...info,
      amount: this.paymentAmount * info.multiplier,
    };
  }

  submit(): void {
    if (!this.studentNumber.trim() || !this.paymentAmount) {
      this.toast.show('Please fill in student number and payment amount.', 'error');
      return;
    }

    this.loading.set(true);
    this.result.set(null);
    this.errorMessage.set(null);

    const request = {
      studentNumber: this.studentNumber.trim(),
      paymentAmount: this.paymentAmount,
      ...(this.paymentDate ? { paymentDate: this.paymentDate } : {}),
    };

    this.paymentService.processPayment(request).subscribe({
      next: (res) => {
        this.result.set(res);
        this.loading.set(false);
        this.toast.show('Payment processed successfully!');
      },
      error: (err: HttpErrorResponse) => {
        const msg = err.error?.message || err.error?.error || 'Payment failed. Please try again.';
        this.errorMessage.set(msg);
        this.loading.set(false);
        this.toast.show('Payment failed.', 'error');
      },
    });
  }
}
