package com.leedtechs.fee_payment.util;

import java.math.BigDecimal;

public class IncentiveCalculator {

    private static final BigDecimal TIER_1_MAX = new BigDecimal("100000");
    private static final BigDecimal TIER_2_MAX = new BigDecimal("500000");

    private static final BigDecimal RATE_1 = new BigDecimal("0.01"); // 1%
    private static final BigDecimal RATE_2 = new BigDecimal("0.03"); // 3%
    private static final BigDecimal RATE_3 = new BigDecimal("0.05"); // 5%

    /**
     * Returns the incentive rate based on payment amount:
     * 0 < x < 100K  → 1%
     * 100K ≤ x < 500K → 3%
     * x ≥ 500K       → 5%
     */
    public static BigDecimal getRate(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(TIER_1_MAX) < 0) {
            return RATE_1;
        } else if (paymentAmount.compareTo(TIER_2_MAX) < 0) {
            return RATE_2;
        } else {
            return RATE_3;
        }
    }
}
