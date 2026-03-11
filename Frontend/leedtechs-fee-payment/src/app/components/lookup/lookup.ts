import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../../services/payment.service';
import { FormatService } from '../../services/format.service';
import { StudentResponse } from '../../models/payment.models';

@Component({
  selector: 'app-lookup',
  imports: [CommonModule, FormsModule],
  templateUrl: './lookup.html',
  styleUrl: './lookup.css',
})
export class LookupComponent {

  studentNumber = '';
  loading = false;
  student: StudentResponse | null = null;
  errorMsg = '';

  constructor(private paymentService: PaymentService, public fmt: FormatService) {}

  search(): void {
    if (!this.studentNumber.trim()) {
      this.errorMsg = 'Please enter a student number.';
      return;
    }
    this.loading = true;
    this.student = null;
    this.errorMsg = '';

    this.paymentService.getStudent(this.studentNumber.trim()).subscribe({
      next: (res) => { this.student = res; this.loading = false; },
      error: (err: HttpErrorResponse) => {
        this.errorMsg = err.error?.message || err.error?.error || JSON.stringify(err.error) || 'Student not found.';        this.loading = false;
      },
    });
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter') this.search();
  }
}
