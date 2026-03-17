package com.leedtechs.fee_payment.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DueDateCalculatorTest {

    @Test
    void weekday_dueDate_noAdjustment() {
        LocalDate result = DueDateCalculator.calculate(LocalDate.of(2026, 3, 14));
        assertEquals(LocalDate.of(2026, 6, 12), result);
    }

    @Test
    void saturday_dueDate_movesToMonday() {
        LocalDate result = DueDateCalculator.calculate(LocalDate.of(2026, 4, 5));
        assertEquals(LocalDate.of(2026, 7, 6), result);
    }

    @Test
    void sunday_dueDate_movesToMonday() {
        LocalDate result = DueDateCalculator.calculate(LocalDate.of(2026, 1, 4));
        assertEquals(LocalDate.of(2026, 4, 6), result);
    }
}