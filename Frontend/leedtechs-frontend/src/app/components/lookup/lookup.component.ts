import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentService } from '../../services/payment.service';
import { ToastService } from '../../services/toast.service';
import { FormatService } from '../../services/format.service';
import { StudentResponse } from '../../models/api.models';

@Component({
  selector: 'app-lookup',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lookup.component.html',
  styleUrl: './lookup.component.css',
})
export class LookupComponent {
  studentNumber = '';
  loading = signal(false);
  student = signal<StudentResponse | null>(null);
  errorMessage = signal<string | null>(null);

  constructor(
    private paymentService: PaymentService,
    private toast: ToastService,
    public fmt: FormatService,
  ) {}

  lookup(): void {
    if (!this.studentNumber.trim()) {
      this.toast.show('Enter a student number.', 'error');
      return;
    }

    this.loading.set(true);
    this.student.set(null);
    this.errorMessage.set(null);

    this.paymentService.getStudent(this.studentNumber.trim()).subscribe({
      next: (res) => {
        this.student.set(res);
        this.loading.set(false);
        this.toast.show('Student found.');
      },
      error: (err: HttpErrorResponse) => {
        const msg = err.error?.message || 'Student not found.';
        this.errorMessage.set(msg);
        this.loading.set(false);
        this.toast.show('Student not found.', 'error');
      },
    });
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter') this.lookup();
  }
}
