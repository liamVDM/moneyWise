package org.nodexy.moneywise;


import java.time.DayOfWeek;
import java.time.LocalDate;

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
}
