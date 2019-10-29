/*******************************************************************************
 * Copyright (c) 2019 CleverClover
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT which is available at
 * https://spdx.org/licenses/MIT.html#licenseText
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *     CleverClover - initial API and implementation
 *******************************************************************************
 */
package ru.cleverclover.metacalendar

import java.time.DayOfWeek
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Model for a point on a [MetaCalendar].
 *
 * Extend this one if none of existing implementations fits your needs.
 *
 * The only thing it should do - be able to [resolve] oneself
 * */
abstract class DayMark {
    abstract val monthNo: Month
    abstract val note: Any?
    /**
     * Resolve the mark to a precise year and time zone: find out exact _date_
     * */
    open fun resolve(year: Int, zone: ZoneId, startOfADay: Boolean = true) = resolvedMark(year, zone, startOfADay).date()

    internal abstract fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean): MarkResolved
}

/**
 * Like *21-st of October*
 * */
data class DayOfMonth(override val monthNo: Month,
                      val dayNo: Int,
                      override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            DayOfMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "$dayNo of ${monthNo.name}"
}

/**
 * Like *the second Monday in August*
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

/**
 * Like *the last Wednesday in June*
 * */
data class LastWeekdayInMonth(override val monthNo: Month,
                              val weekday: DayOfWeek,
                              override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            LastWeekdayInMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "The last ${weekday.name} in ${monthNo.name}"
}

/**
 * Like *end of May*, meaning the last day in the month.
 *
 * For February it means 29-th for leap years and 28-th for the rest of 'em
 * */
data class LastDayOfMonth(override val monthNo: Month,
                          override val note: Any? = null) : DayMark() {
    override fun resolvedMark(year: Int, zone: ZoneId, startOfADay: Boolean) =
            LastDayOfMonthResolved(this, year, zone, startOfADay)

    override fun toString() = "The last day of ${monthNo.name}"
}

/**
 * Pair of [DayMark]s, base [MetaCalendar] unit.
 *
 * As we do not specify *time* for a [DayMark], we use
 *   - *LocalTime.of(0, 0, 0, 0)* for the first day mark and
 *   - *LocalTime.of(23, 59, 59, 999)* for the end-mark
 *
 *   Thus, any period
 *     - contains at least one full day (minus one nanosecond): 07.12 (start of the day) - 07.12 (end of the day)
 *     - cannot contain more than a year (minus one nanosecond): 07.12 (start of the day) - 06.12 (end of the day)
 *
 * */
data class Period(var start: DayMark, var end: DayMark, val note: Any? = null) {
    /**
     * Resolve a period means mostly resolving both it's ends to precise periods.
     *
     * If a period is crosses a year,
     * resolution produces two periods, overlapping with the year partially.
     *
     *  For instance, let's resolve (10.01 - 01.01) to year of 2019. There will be
     *   - 10.01.2018 - 01.01.2019
     *   - 10.01.2019 - 01.01.2020
     *
     *  as the both are valuable for the year of 2019.
     * */
    fun resolve(year: Int, zone: ZoneId) = PeriodResolved(this, year, zone).periods()
}

/**
 * Is a simple aggregator for [Period]s, which are not ordered here.
 *
 * Provides bulk [resolve] for all periods on fell swoop.
 * */
class MetaCalendar(periods: Collection<Period> = setOf()) {
    private val periods = mutableSetOf<Period>()

    init {
        this.periods.addAll(periods)
    }

    fun addPeriod(period: Period): Unit = run { periods.add(period) }
    fun removePeriod(period: Period): Unit = run { periods.remove(period) }
    fun periods(): Set<Period> = periods
    fun size() = periods.size

    /**
     * Lazy bulk resolution for all contained periods for the [year] and [zone]
     * @return [ResolvedCalendar] instance
     * */
    fun resolve(year: Int, zone: ZoneId = ZoneId.systemDefault()) = ResolvedCalendar(this, year, zone)

    /**
     * Lazy bulk resolution for all contained periods in all listed [years] and the [zone] in one fell swoop.
     * @return [ResolvedCalendar] instance
     * */
    fun resolve(years: Set<Int>, zone: ZoneId = ZoneId.systemDefault()) = ResolvedCalendar(this, years, zone)
}