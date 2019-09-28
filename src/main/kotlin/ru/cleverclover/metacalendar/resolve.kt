package ru.cleverclover.metacalendar

import java.time.*
import java.time.temporal.ChronoField

open class MetaCalendarResolveException(message: String, cause: Throwable? = null) : Exception(message, cause)

internal sealed class MarkResolved {
    abstract fun date(): ZonedDateTime

    protected fun dayBeacon(startOfADay: Boolean): LocalTime =
            if (startOfADay) LocalTime.of(0, 0, 0, 0)
            else LocalTime.of(23, 59, 59, 999)
}

internal class WeekdayInMonthResolved(
        private val mark: WeekdayInMonth,
        private val year: Int,
        private val zone: ZoneId,
        private val startOfADay: Boolean) : MarkResolved() {

    override fun date(): ZonedDateTime {
        var time = ZonedDateTime.of(
                LocalDate.of(year, mark.monthNo, 1),
                dayBeacon(startOfADay),
                zone)
        time = time.with(ChronoField.DAY_OF_WEEK, mark.weekday.value.toLong())
        var weeksToJumpOver = mark.weekNoInMonth - 1
        if (time.get(ChronoField.MONTH_OF_YEAR) < mark.monthNo.value) {
            weeksToJumpOver++
        }
        time = time.plusDays(7L * weeksToJumpOver)
        return time
    }
}

internal class LastWeekdayInMonthResolved(
        private val mark: LastWeekdayInMonth,
        private val year: Int,
        private val zone: ZoneId,
        private val startOfADay: Boolean) : MarkResolved() {

    override fun date(): ZonedDateTime {
        var time = ZonedDateTime.of(firstDayOfNextMonth(mark, year), dayBeacon(startOfADay), zone)
        do {
            time = time.minusDays(1)
        } while (time.dayOfWeek != mark.weekday)
        return time
    }

}

internal class DayOfMonthResolved(private val mark: DayOfMonth,
                                  private val year: Int,
                                  private val zone: ZoneId,
                                  private val startOfADay: Boolean) : MarkResolved() {
    override fun date(): ZonedDateTime = ZonedDateTime.of(
            LocalDate.of(year, mark.monthNo, mark.dayNo),
            dayBeacon(startOfADay),
            zone)
}

internal class LastDayOfMonthResolved(private val mark: LastDayOfMonth,
                                      private val year: Int,
                                      private val zone: ZoneId,
                                      private val startOfADay: Boolean) : MarkResolved() {
    override fun date(): ZonedDateTime = ZonedDateTime.of(
            firstDayOfNextMonth(mark, year).minusDays(1),
            dayBeacon(startOfADay),
            zone)
}

private fun firstDayOfNextMonth(mark: DayMark, year: Int): LocalDate {
    val endOfYear = mark.monthNo == Month.DECEMBER
    val yearIncrement = if (endOfYear) 1 else 0
    val nextMonth = if (endOfYear) Month.JANUARY else mark.monthNo + 1
    return LocalDate.of(year + yearIncrement, nextMonth, 1)
}

/**
 * If a meta-period crosses a start-of-a-year, then it appears twice in the final resolution.
 *
 * Resolved period starts with the first day's first second and ends with the last second of the last day.
 * */
internal class PeriodResolved(private val period: Period,
                              private val year: Int,
                              private val zone: ZoneId) {
    fun period(): Set<Pair<ZonedDateTime, ZonedDateTime>> = with(period) {
        val crossYearPeriod = start.monthNo > end.monthNo
        if (crossYearPeriod) {
            setOf(
                    Pair(
                            start.resolve(year - 1, zone),
                            end.resolve(year, zone, false)),
                    Pair(
                            start.resolve(year, zone),
                            end.resolve(year + 1, zone, false)))
        } else {
            setOf(
                    Pair(
                            start.resolve(year, zone),
                            end.resolve(year, zone, false)))

        }
    }
}