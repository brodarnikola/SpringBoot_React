package com.example.polls.task;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class MonthUtils {

    public static Date getFirstDayOfMonth() {

        LocalDate firstDayOfLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        return Date.from(firstDayOfLastMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
    }

    public static Date getLastDayOfMonth() {

        LocalDate firstDayOfLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfLastMonth = firstDayOfLastMonth.with(TemporalAdjusters.lastDayOfMonth());

        return Date.from(lastDayOfLastMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
    }
}
