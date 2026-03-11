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

export interface PaymentRequest {
  studentNumber: string;
  paymentAmount: number;
  paymentDate?: string;
}

export interface PaymentResponse {
  studentNumber: string;
  previousBalance: number;
  paymentAmount: number;
  incentiveRate: number;
  incentiveAmount: number;
  newBalance: number;
  nextPaymentDueDate: string | null;
}
