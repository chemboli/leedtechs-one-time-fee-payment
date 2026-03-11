import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class FormatService {

  cfa(amount: number | null | undefined): string {
    if (amount == null) return '—';
    return new Intl.NumberFormat('fr-CM', {
      style: 'currency',
      currency: 'XAF',
      minimumFractionDigits: 0,
    }).format(amount);
  }

  date(value: string | null | undefined): string {
    if (!value) return '—';
    return new Date(value).toLocaleDateString('fr-CM', {
      day: 'numeric', month: 'long', year: 'numeric',
    });
  }

  getIncentiveTier(amount: number): { tier: string; rate: number; label: string } {
    if (amount < 100_000) return { tier: 'Tier 1', rate: 0.01, label: '1%' };
    if (amount < 500_000) return { tier: 'Tier 2', rate: 0.03, label: '3%' };
    return                       { tier: 'Tier 3', rate: 0.05, label: '5%' };
  }
}
