export interface StudentRequest {
  studentNumber: string;
  fullName: string;
  balance: number;
}

export interface StudentResponse {
  studentNumber: string;
  fullName: string;
  balance: number;
  nextDueDate: string | null;
}

export interface OneTimePaymentRequest {
  studentNumber: string;
  paymentAmount: number;
  paymentDate?: string; // yyyy-MM-dd
}

export interface OneTimePaymentResponse {
  studentNumber: string;
  previousBalance: number;
  paymentAmount: number;
  incentiveRate: number;
  incentiveAmount: number;
  newBalance: number;
  nextPaymentDueDate: string | null;
}

export interface ApiError {
  message?: string;
  error?: string;
  [key: string]: unknown;
}
