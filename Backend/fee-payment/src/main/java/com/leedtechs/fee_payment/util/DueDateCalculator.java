package com.leedtechs.fee_payment.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DueDateCalculator {

    private static final int DUE_DATE_DAYS = 90;

    /**
     * Calculates next due date as 90 days from paymentDate.
     * If it falls on Saturday or Sunday, moves to the following Monday.
     */
    public static LocalDate calculate(LocalDate paymentDate) {
        LocalDate dueDate = paymentDate.plusDays(DUE_DATE_DAYS);

        if (dueDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            dueDate = dueDate.plusDays(2);
        } else if (dueDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dueDate = dueDate.plusDays(1);
        }

        return dueDate;
    }
}

