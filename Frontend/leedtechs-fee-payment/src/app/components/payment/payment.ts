import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../../services/payment.service';
import { FormatService } from '../../services/format.service';
import { PaymentResponse } from '../../models/payment.models';

// inside @Component:
imports: [CommonModule, FormsModule],

// inside the class:
  studentNumber = '';
paymentAmount: number | null = null;
paymentDate = '';
loading = false;
result: PaymentResponse | null = null;
errorMsg = '';

constructor(private paymentService: PaymentService, public fmt: FormatService) {}

get activeTier(): number {
  if (!this.paymentAmount || this.paymentAmount <= 0) return 0;
  if (this.paymentAmount < 100_000) return 1;
  if (this.paymentAmount < 500_000) return 2;
  return 3;
}

get incentivePreview(): number {
  if (!this.paymentAmount) return 0;
  return this.paymentAmount * this.fmt.getIncentiveTier(this.paymentAmount).rate;
}

submit(): void {
  if (!this.studentNumber.trim() || !this.paymentAmount) {
  this.errorMsg = 'Student number and amount are required.';
  return;
}
this.loading = true;
this.result = null;
this.errorMsg = '';

this.paymentService.processPayment({
  studentNumber: this.studentNumber.trim(),
  paymentAmount: this.paymentAmount,
  ...(this.paymentDate ? { paymentDate: this.paymentDate } : {}),
}).subscribe({
  next: (res) => { this.result = res; this.loading = false; },
  error: (err: HttpErrorResponse) => {
    this.errorMsg = err.error?.message || 'Something went wrong.';
    this.loading = false;
  },
});
}
