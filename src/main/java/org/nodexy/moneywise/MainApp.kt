package org.nodexy.moneywise

import org.nodexy.moneywise.DateUtils.*
import java.time.LocalDate

import java.time.DayOfWeek
import java.time.LocalTime
import kotlin.text.StringBuilder

/**
 * Created by phoenix on 21/12/16.
 */
object MainApp {
    private val options = mapOf(
            Pair("using the car", doubleArrayOf(64.0)),
            Pair("via Fourways", doubleArrayOf(15.0, 15.0, 12.0)),
            Pair("via Douglas Dale", doubleArrayOf(15.0, 17.0))
    )
    @JvmStatic fun main(args: Array<String>) {
        taxiFareCalc()
    }

    fun nextFriday(): LocalDate {
        var cur = LocalDate.now()
        while (cur.dayOfWeek != DayOfWeek.FRIDAY) {
            cur = cur.plusDays(1)
        }
        return cur
    }

    internal fun taxiFareCalc() {
        /* return costs*/

        val now = LocalDate.now()

        val daysLeft = weekdaysUntil(now, getLastDayOfMonth(now)) - currDayFactor()

        println("Today's date: ${now.dayOfWeek.toString().capitalize()}, $now")
        println("Week's stats:")
        val daysToFriday = nextFriday().dayOfMonth - now.dayOfMonth + (1-currDayFactor())
        println("\tDays left for the week: $daysToFriday")
        println("\tFares for this week:")

        options.forEach { t, u ->
            println("\t\tR ${calculateWeeklyStats(sigma(u) * 2).format(2)} - $t")
        }

        println("Month stats:")
        println("\tNumber of days till month end: $daysLeft")
        val weeks_rem = daysLeft.ceil() / 5
        val days_rem = daysLeft.ceil() % 5
        val sb = StringBuilder("\tNumber of work days remaining: ").append(daysLeft)
        if (weeks_rem > 0) {
            sb.append(" => $weeks_rem weeks")
            if (days_rem > 0)
                sb.append(", $days_rem days")
        }

        println(sb.toString())

        println("Transport fare still due for this month (" + now.month + "):")
        options.forEach { t, u ->
            println("\t\tR ${calculateFareRemaining(sigma(u) * 2).format(2)} - $t")
        }

        println("****************************************************************")
        LongRange(0, 2).onEach {i->
            println(monthlyAnalysis(now.plusMonths(i)))
        }
        println("****************************************************************")
    }

    internal fun monthlyAnalysis(date: LocalDate): String {
        val msg = StringBuilder()
        msg.append("Transport cost for whole month (${date.month}):")
        msg.append("\n\tNr of weekdays: ${weekdaysUntil(date.withDayOfMonth(1), getLastDayOfMonth(date))}")
        options.forEach { t, u ->
            msg.append("\n\t\tR ${calculateFare(sigma(u) * 2, date.withDayOfMonth(1), getLastDayOfMonth(date)).format(2)} - $t")
        }
        return msg.toString()
    }

    internal fun calculateWeeklyStats(dailyFare: Double): Double {
        val today = LocalDate.now()
        return calculateFare(dailyFare, today, nextFriday())
    }

    internal fun calculateFareRemaining(dailyFare: Double): Double {
        val today = LocalDate.now()
        return calculateFare(dailyFare, today, getLastDayOfMonth(today))
    }

    internal fun calculateFare(dailyFare: Double, firstDay: LocalDate, lastDay: LocalDate): Double {
        val nrOfWeekdays = weekdaysUntil(firstDay, lastDay.plusDays(1)) // we need an extra day, because the called method gives Delta days
        return (nrOfWeekdays - currDayFactor()) * dailyFare
    }

    internal fun sigma(array: DoubleArray): Double {
        return array.asList().reduceRight {x,y-> x+y}
    }
    fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
    fun Double.ceil() = if (this.toInt().toDouble() == this) this.toInt() else this.toInt()+1

    fun currDayFactor(): Double {
        val now = LocalDate.now()
        return if (isWeekday(now.dayOfWeek)) {
            val time = LocalTime.now()
            if (time.isAfter(LocalTime.of(20,0))) 1.0
            else if (time.isAfter(LocalTime.of(8,0,0)))
                0.5
            else
                0.0
        } else 0.0
    }
}
