package com.leedtechs.fee_payment.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IncentiveCalculatorTest {

    @Test
    void tier1_under100K_applies1Percent() {
        BigDecimal rate = IncentiveCalculator.getRate(new BigDecimal("99999.99"));
        assertEquals(new BigDecimal("0.01"), rate);
    }

    @Test
    void tier2_exactly100K_applies3Percent() {
        BigDecimal rate = IncentiveCalculator.getRate(new BigDecimal("100000"));
        assertEquals(new BigDecimal("0.03"), rate);
    }

    @Test
    void tier2_between100Kand500K_applies3Percent() {
        BigDecimal rate = IncentiveCalculator.getRate(new BigDecimal("300000"));
        assertEquals(new BigDecimal("0.03"), rate);
    }

    @Test
    void tier3_exactly500K_applies5Percent() {
        BigDecimal rate = IncentiveCalculator.getRate(new BigDecimal("500000"));
        assertEquals(new BigDecimal("0.05"), rate);
    }

    @Test
    void tier3_above500K_applies5Percent() {
        BigDecimal rate = IncentiveCalculator.getRate(new BigDecimal("900000"));
        assertEquals(new BigDecimal("0.05"), rate);
    }
}