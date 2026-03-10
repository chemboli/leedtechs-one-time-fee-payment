import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  OneTimePaymentRequest,
  OneTimePaymentResponse,
  StudentRequest,
  StudentResponse,
} from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  processPayment(request: OneTimePaymentRequest): Observable<OneTimePaymentResponse> {
    return this.http.post<OneTimePaymentResponse>(
      `${this.baseUrl}/one-time-fee-payment`,
      request
    );
  }

  getStudent(studentNumber: string): Observable<StudentResponse> {
    return this.http.get<StudentResponse>(
      `${this.baseUrl}/students/${encodeURIComponent(studentNumber)}`
    );
  }

  createStudent(request: StudentRequest): Observable<StudentResponse> {
    return this.http.post<StudentResponse>(`${this.baseUrl}/students`, request);
  }
}
