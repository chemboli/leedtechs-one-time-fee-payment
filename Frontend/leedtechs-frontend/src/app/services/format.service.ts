import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class FormatService {
  currency(value: number | null | undefined): string {
    if (value == null) return '—';
    return new Intl.NumberFormat('en-NG', {
      style: 'currency',
      currency: 'NGN',
      maximumFractionDigits: 2,
    }).format(value);
  }

  date(value: string | null | undefined): string {
    if (!value) return '—';
    return new Date(value).toLocaleDateString('en-GB', {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
    });
  }

  incentiveRate(amount: number): { rate: string; label: string; multiplier: number } {
    if (amount < 100_000) return { rate: '1%', label: 'Tier 1', multiplier: 0.01 };
    if (amount < 500_000) return { rate: '3%', label: 'Tier 2', multiplier: 0.03 };
    return { rate: '5%', label: 'Tier 3', multiplier: 0.05 };
  }
}
