package org.nodexy.moneywise;

import java.time.LocalDate;

import static org.nodexy.moneywise.DateUtils.getLastDayOfMonth;
import static org.nodexy.moneywise.DateUtils.weekdaysUntil;

/**
 * Created by phoenix on 21/12/16.
 */
public class MainApp {
    private static final double[]
            fare_taxi_1way_viaFourways = {15,14,11},
            fare_taxi_1way_viaDouglas = {15,16};
    public static void main(String[] args) {
        taxiFareCalc();
    }
    static void taxiFareCalc() {
        double dailyFareViaFourways = sigma(fare_taxi_1way_viaFourways)*2; // return cost
        double dailyFareViaDouglas = sigma(fare_taxi_1way_viaDouglas)*2; // return cost

        LocalDate now = LocalDate.now();
        System.out.println("Month stats:");
        System.out.println("\tToday's date: "+now);
        System.out.println("\tNumber of days till month end: "+(DateUtils.getLastDayOfMonth(now).getDayOfMonth()-now.getDayOfMonth()));
        int rem = weekdaysUntil(now,getLastDayOfMonth(now));
        System.out.println("\tNumber of work days remaining: "+rem+" => "+rem/5+" weeks + "+rem%5+" days");
        
        System.out.println("Transport fare still due for this month ("+now.getMonth()+"):");
        System.out.println(String.format("\tR %1.2f - via Fourways", calculateFareRemaining(dailyFareViaFourways)));
        System.out.println(String.format("\tR %1.2f - via Douglas", calculateFareRemaining(dailyFareViaDouglas)));
        System.out.println("Transport cost for whole month ("+now.getMonth()+"):");
        System.out.println(String.format("\tR %1.2f - via Fourways", calculateFare(dailyFareViaFourways, now.withDayOfMonth(1),getLastDayOfMonth(now))));
        System.out.println(String.format("\tR %1.2f - via Douglas", calculateFare(dailyFareViaDouglas, now.withDayOfMonth(1),getLastDayOfMonth(now))));

        LocalDate nxtMon = now.plusMonths(1);
        System.out.println("Transport cost for whole month ("+nxtMon.getMonth()+"):");
        System.out.println(String.format("\tR %1.2f - via Fourways", calculateFare(dailyFareViaFourways, nxtMon.withDayOfMonth(1),getLastDayOfMonth(nxtMon))));
        System.out.println(String.format("\tR %1.2f - via Douglas", calculateFare(dailyFareViaDouglas, nxtMon.withDayOfMonth(1),getLastDayOfMonth(nxtMon))));
    }
    static double calculateFareRemaining(double dailyFare) {
        LocalDate today = LocalDate.now();
        return calculateFare(dailyFare,today,getLastDayOfMonth(today));
    }

    static double calculateFare(double dailyFare, LocalDate firstDay, LocalDate lastDay) {
        int nrOfWeekdays = weekdaysUntil(firstDay,lastDay.plusDays(1)); // we need an extra day, because the called method gives Delta days
        return nrOfWeekdays*dailyFare;
    }

    static double sigma(double[] array) {
        double sum = 0;
        for (double e : array) sum += e;
        return sum;
    }
}
