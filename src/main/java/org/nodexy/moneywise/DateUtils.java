package org.nodexy.moneywise;


import java.time.*;
import java.util.Date;

/**
 * Created by phoenix on 21/12/16.
 */
public class DateUtils {
    public static int weekdaysUntil(LocalDate startDate, LocalDate endDate) {
        assert (startDate!=null && endDate!=null);
        int count = 0;
        for (LocalDate day = startDate; day.isBefore(endDate); day = day.plusDays(1)) {
            if (isWeekday(day.getDayOfWeek()))
                count++;
        }
        return count;
    }

    public static LocalDate getLastDayOfMonth(LocalDate date) {
        assert (date!=null);
        int dayOfMonth = date.getDayOfMonth();
        int len = date.lengthOfMonth();
        LocalDate lastDay = date.plusDays(len-dayOfMonth);
        return lastDay;
    }

    public static boolean isWeekday(DayOfWeek day) {
        assert day!=null;
        return day.getValue() < DayOfWeek.SATURDAY.getValue();
    }

    /**
     * sets the time to midnight of a given LocalDate.
     * Uses Central African Time by default
     * @param ld
     * @return
     */
    static Date localDate2Date(LocalDate ld, ZoneOffset zoneOffset) {
        return localDateTime2Date(LocalDateTime.of(ld, LocalTime.of(0,0,0)), zoneOffset);
    }
    static Date localDate2Date(LocalDate ld) {
        return localDate2Date(ld, ZoneOffset.of("+0200")); // +0200 -> CAT
    }
    /**
     * Uses Central African Time by default.
     * @param ldt
     * @return
     */
    static Date localDateTime2Date(LocalDateTime ldt, ZoneOffset zoneOffset) {
        return Date.from(ldt.toInstant(zoneOffset));
    }
    static Date localDateTime2Date(LocalDateTime ldt) {
        return localDateTime2Date(ldt, ZoneOffset.of("+0200")); // +0200 -> CAT
    }
}
