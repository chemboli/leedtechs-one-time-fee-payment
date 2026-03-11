import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentRequest, PaymentResponse, StudentRequest, StudentResponse } from '../models/payment.models';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly API = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  processPayment(body: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${this.API}/one-time-fee-payment`, body);
  }

  getStudent(studentNumber: string): Observable<StudentResponse> {
    return this.http.get<StudentResponse>(`${this.API}/students/${studentNumber}`);
  }

  registerStudent(body: StudentRequest): Observable<StudentResponse> {
    return this.http.post<StudentResponse>(`${this.API}/students`, body);
  }
}
