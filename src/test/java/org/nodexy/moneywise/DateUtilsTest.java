package org.nodexy.moneywise;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAmount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.nodexy.moneywise.DateUtils.getLastDayOfMonth;
import static org.nodexy.moneywise.DateUtils.isWeekday;
import static org.nodexy.moneywise.DateUtils.weekdaysUntil;

/**
 * Created by phoenix on 21/12/16.
 */
public class DateUtilsTest {
    private static final double[]
            fare_taxi_1way_viaFourways = {15,14,11},
            fare_taxi_1way_viaDouglas = {15,16};
    // @Ignore
    @Test public void testCalculator() {
        double dailyFare = sigma(fare_taxi_1way_viaFourways)*2; // going and returning
        LocalDate now = LocalDate.now();
        System.out.println("Month stats:");
        System.out.println("\tToday's date: "+now);
        System.out.println("\tNumber of days till month end: "+(DateUtils.getLastDayOfMonth(now).getDayOfMonth()-now.getDayOfMonth()));
        int rem = weekdaysUntil(now,getLastDayOfMonth(now));
        System.out.println("\tNumber of work days remaining: "+rem+" => "+rem/5+" weeks + "+rem%5+" days");
        System.out.println("Transport fare still due for this month ("+now.getMonth()+"):");
        System.out.println(String.format("\tR %1.2f - via Fourways", calculateFareRemaining(sigma(fare_taxi_1way_viaFourways)*2)));
        System.out.println(String.format("\tR %1.2f - via Douglas", calculateFareRemaining(sigma(fare_taxi_1way_viaDouglas)*2)));
        System.out.println("Transport cost for whole month ("+now.getMonth()+"):");
        System.out.println(String.format("\tR %1.2f - via Fourways", calculateFare(sigma(fare_taxi_1way_viaFourways)*2, now.withDayOfMonth(1),getLastDayOfMonth(now))));
        System.out.println(String.format("\tR %1.2f - via Douglas", calculateFare(sigma(fare_taxi_1way_viaDouglas)*2, now.withDayOfMonth(1),getLastDayOfMonth(now))));
    }
    @Ignore
    @Test public void testCalculatorStatic() {
        double dailyFare = sigma(fare_taxi_1way_viaFourways)*2; // going and returning
        LocalDate date = LocalDate.of(2017,Month.JANUARY,1);
        System.out.println(String.format("Transport amount for whole month (%s)",date.getMonth().toString()+" "+date.getYear()));
        System.out.println(String.format("\tR %1.2f (via Fourways)",calculateFare(dailyFare,date,getLastDayOfMonth(date))));
        System.out.println(String.format("\tR %1.2f (via Douglas)",calculateFare(2*sigma(fare_taxi_1way_viaDouglas),date,getLastDayOfMonth(date))));
    }
    @Ignore
    @Test public void test() {
        LocalDate date = LocalDate.of(2016, Month.DECEMBER,30);
        System.out.println(date);
        System.out.println(date.plusMonths(3));
    }
    @Ignore
    @Test public void testBytes() throws IOException {
        String msg = "Hello, World!";
        byte[] bytes = msg.getBytes();
        System.out.println("message text  = "+ msg);
        System.out.println("Mapping:");
        for (int i = 0; i < msg.length(); ++i) {
            System.out.println(String.format("\t%s->%d",msg.charAt(i),bytes[i]));
        }
        String filename = "message-data";
        File fileAscii = new File(filename+".ascii"), fileBin = new File(filename+".bin");
        System.out.println("Directory : "+fileAscii.getAbsolutePath());
        IOUtils.write(bytes,new FileOutputStream(fileAscii));

        byte[] bins = {72,101,108,108,111, 44,32, 0x05,0x39, 0x33};
        FileOutputStream outf = new FileOutputStream(fileBin);
        outf.write(bins);

        outf.close();
    }
    @Test public void testGetLastDayOfMonth() {
        LocalDate[] dates = {LocalDate.now(), LocalDate.of(2016,Month.DECEMBER,1), LocalDate.of(2016,Month.FEBRUARY,1), LocalDate.of(2016,Month.FEBRUARY,28), LocalDate.of(2016,Month.FEBRUARY,29)};
        for (LocalDate date : dates) {
            LocalDate nxt = getLastDayOfMonth(date);
            assertEquals(nxt.getYear(),date.getYear());
            assertEquals(nxt.getMonth(), date.getMonth());
            Assert.assertNotEquals(nxt.plusDays(1).getMonth(), date.getMonth());
        }
    }
    @Test public void testIsWeekday() {
        assertTrue(isWeekday(DayOfWeek.MONDAY));
        assertTrue(isWeekday(DayOfWeek.TUESDAY));
        assertTrue(isWeekday(DayOfWeek.WEDNESDAY));
        assertTrue(isWeekday(DayOfWeek.THURSDAY));
        assertTrue(isWeekday(DayOfWeek.FRIDAY));
        assertFalse(isWeekday(DayOfWeek.SATURDAY));
        assertFalse(isWeekday(DayOfWeek.SUNDAY));
    }
    @Test public void testWeekdaysUntil() {
        assertEquals(5,weekdaysUntil(LocalDate.now(),LocalDate.now().plusDays(7)));
        assertEquals(8,weekdaysUntil(LocalDate.of(2016, Month.DECEMBER,21), LocalDate.of(2017, Month.JANUARY,1)));
    }
    
    double calculateFareRemaining(double dailyFare) {
        LocalDate today = LocalDate.now();
        return calculateFare(dailyFare,today,getLastDayOfMonth(today));
    }

    double calculateFare(double dailyFare, LocalDate firstDay, LocalDate lastDay) {
        int nrOfWeekdays = weekdaysUntil(firstDay,lastDay.plusDays(1)); // we need an extra day, because the called method gives Delta days
        return nrOfWeekdays*dailyFare;
    }

    double sigma(double[] array) {
        double sum = 0;
        for (double e : array) sum += e;
        return sum;
    }
}
