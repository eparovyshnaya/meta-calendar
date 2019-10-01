package ru.cleverclover.metacalendar

import java.time.DayOfWeek
import java.time.Month
import java.time.ZoneId

abstract class DayMark {
    abstract val monthNo: Month
    abstract val note: Any?
    // todo: no resolution facilities should reside here in model, extract to separate interfaces
    open fun resolve(year: Int, zone: ZoneId, startOfADay: Boolean = true) = resolvedMark(year, zone, startOfADay).date()

    internal abstract fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean): MarkResolved
}

/**
 * Like
 * | 23-rd | of september |
 * */
data class DayOfMonth(override val monthNo: Month,
                      val dayNo: Int,
                      override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            DayOfMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "$dayNo of ${monthNo.name}"
}

/**
 * Like
 *
 * | the second | monday | of august |
 *
 * If the first week *of august* does not contain *monday*,
 * then such a tail-week is skipped as *monday*-less,
 * and *the second* does not start ticking.
 *
 * @param[monthNo] one of [java.time.Month] constants
 * @param[weekNoInMonth] should stay in the range {1, 2, 3, 4} to be resolvable. Convention is to use *-1* value for *the last*.
 * @param[weekday] one of [java.time.DayOfWeek] constants
 * */
data class WeekdayInMonth(override val monthNo: Month,
                          val weekNoInMonth: Int,
                          val weekday: DayOfWeek,
                          override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            WeekdayInMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "${weekday.name} #$weekNoInMonth in ${monthNo.name}"
}

data class LastWeekdayInMonth(override val monthNo: Month,
                              val weekday: DayOfWeek,
                              override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            LastWeekdayInMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "The last ${weekday.name} in ${monthNo.name}"
}

data class LastDayOfMonth(override val monthNo: Month,
                          override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            LastDayOfMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "The last day of ${monthNo.name}"
}

data class Period(var start: DayMark, var end: DayMark, val note: Any? = null) {
    fun resolve(year: Int, zone: ZoneId) = PeriodResolved(this, year, zone).periods()
}

class MetaCalendar {
    private val periods = mutableSetOf<Period>()

    fun addPeriod(period: Period): Unit = run { periods.add(period) }
    fun removePeriod(period: Period): Unit = run { periods.remove(period) }
    fun periods(): Set<Period> = periods
    fun size() = periods.size

    fun resolve(year: Int, zone: ZoneId) = ResolvedCalendar(this, year, zone).dates()
}


