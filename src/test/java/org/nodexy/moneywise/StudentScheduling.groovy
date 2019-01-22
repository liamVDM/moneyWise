package org.nodexy.moneywise

import junit.framework.TestCase

import java.time.LocalDate

public class StudentScheduling extends TestCase {
    def students = [:]
    def maxHrs = 88

    StudentScheduling() {
        1.upto(12) { n-> students[n] = 0 }
    }

    void test() {
        f(getDays())
    }

    def f(LocalDate first, LocalDate last) {
        forDays(first, last) { day->
            println "Day is: $day"
        }
    }

    private static getDays() {
        def date = LocalDate.now()
        [DateUtils.getFirstDayOfMonth(date), DateUtils.getLastDayOfMonth(date)]
    }

    static def forDays(LocalDate first, LocalDate last, Closure action) {
        for (LocalDate day = first; !day.isAfter(last); day = day.plusDays(1)) {
            action(day)
        }
    }
}
