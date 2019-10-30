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

/**
 * As parsing success highly depends on an input quality,
 * each atomic parse operation can potentially fail for, say, external reasons.
 *
 * In case of input-driven error there is no way we can proceed with this particular atom,
 * id est we do not tolerate the input issues, do not produce empty or default units, do not log, do not deduce and sophisticate.
 *
 * *We fail aloud.*
 *
 * Nevertheless, you can implement a tolerant reader for a calendar as a whole,
 * processing atomic failures one at a time,
 * if your domain contain a solution for the case.
 *
 * This domain-specific exception covers any parsing issue.
 * */
open class MetaCalendarParseException(message: String, cause: Throwable? = null) : Exception(message, cause)

private object Definitions {
    private const val groupMonth = "(янв.*|февр.*|март.*|апр.*|мая|июн.*|июл.*|авг.*|сен.*|окт.*|ноя.*|дек.*)"
    private const val groupWeekday = "(пон.*|вт.*|ср.*|чет.*|пят.*|суб.*|вос.*)"

    private val periodDefinition = Cashed("\\s*со?\\s+(.+)\\s+по\\s+(.+)\\s*") { it.toRegex() }
    private val dayOfMonthMarkDefinition = Cashed("\\s*(\\d{1,2})\\s+$groupMonth\\s*") { it.toRegex() }
    private val lastWeekdayInMonthMarkDefinition = Cashed("\\s*послед.*\\s+$groupWeekday\\s+$groupMonth\\s*") { it.toRegex() }
    private val lastDayInMonthMarkDefinition = Cashed("\\s*(кон.*\\s+$groupMonth)\\s*") { it.toRegex() }
    private val lastDayInFebMarkDefinition = Cashed("\\s*(28\\s*\\(\\s*29\\s*\\)\\s+(февр.*))|(кон.*\\s+$groupMonth)\\s*") { it.toRegex() }
    private val dayOfWeekMarkDefinition = Cashed("\\s*(пер.*|втор.*|трет.*|чет.*)\\s+$groupWeekday\\s+$groupMonth\\s*") { it.toRegex() }

    fun period() = periodDefinition.get()
    fun dayOfMonth() = dayOfMonthMarkDefinition.get()
    fun lastWeekdayInMonth() = lastWeekdayInMonthMarkDefinition.get()
    fun lastDayInMonth() = lastDayInMonthMarkDefinition.get()
    fun lastDayInFeb() = lastDayInFebMarkDefinition.get()
    fun dayOfWeek() = dayOfWeekMarkDefinition.get()
}

/**
 * The parsing for a natural language string to a [MetaCalendar]'s [Period],
 * defined for the whole period description.
 *
 * Sample input is *с конца февраля по третий вторник августа*
 * */
class PeriodFromRangeDefinition(private val origin: String) {
    fun bounds(): Pair<DayMark, DayMark> {
        val matcher = Definitions.period().matchEntire(origin)
                ?: throw MetaCalendarParseException("no periods definition in $origin")
        return PeriodFromBoundDefinitions(matcher.groups[1]?.value, matcher.groups[2]?.value).bounds()
    }
}

/**
 * The parsing for a natural language string to a [MetaCalendar]'s [Period],
 * defined for the boundary day marks descriptions.
 *
 * Sample input is {*конец февраля*, *третий вторник августа*}
 * */
class PeriodFromBoundDefinitions(private val startDefinition: String?,
                                 private val endDefinition: String?) {
    fun bounds(): Pair<DayMark, DayMark> {
        if (startDefinition == null) {
            throw MetaCalendarParseException("no period start definition")
        }
        if (endDefinition == null) {
            throw MetaCalendarParseException("no period start definition")
        }
        return Pair(
                ParsedDayMark(startDefinition).mark(),
                ParsedDayMark(endDefinition).mark())
    }
}

fun Pair<DayMark, DayMark>.period(note: Any? = null) = Period(first, second, note)

internal class ParsedDayMark(private val origin: String) {

    fun mark(): DayMark {
        parsers(origin).forEach { fromString ->
            fromString.mark()?.let { return it }
        }
        throw MetaCalendarParseException("format of day mark $origin is not supported")
    }

    private fun parsers(origin: String): Sequence<DayMarkFromString> = sequence {
        yield(DayOfMonthFromString(origin))
        yield(LastWeekdayInMonthFromString(origin))
        yield(LastDayInMonthFromString(origin))
        yield(WeekdayInMonthFromString(origin))
        yield(LastDayInFebFromString(origin))
    }
}

internal abstract class DayMarkFromString(protected val origin: String) {
    /**
     * Should not fail if a dedicated matcher does not fit
     * (we take such a parsing attempts one by one in a row to check if someone fits).
     *
     * Can fail in case of contract violation (validation).
     * */
    abstract fun mark(): DayMark?
}


private class DayOfMonthFromString(origin: String) : DayMarkFromString(origin) {

    override fun mark(): DayMark? =
            Definitions.dayOfMonth().matchEntire(origin)?.let {
                val month = MonthResolved.month(it.groupValues[2])
                val day = it.groupValues[1].toInt()
                if (day in 1..month.length(false)) {
                    DayOfMonth(monthNo = month, dayNo = day)
                } else {
                    throw MetaCalendarParseException(
                            "$origin represents invalid day-of-month mark: day $day should stay in range [1, ${month.length(false)}]")
                }
            }
}

private class LastWeekdayInMonthFromString(origin: String) : DayMarkFromString(origin) {
    override fun mark(): DayMark? =
            Definitions.lastWeekdayInMonth().matchEntire(origin)?.let {
                LastWeekdayInMonth(
                        monthNo = MonthResolved.month(it.groupValues[2]),
                        weekday = WeekdayResolved.datOfWeek(it.groupValues[1]))
            }
}

private class LastDayInMonthFromString(origin: String) : DayMarkFromString(origin) {
    override fun mark(): DayMark? =
            Definitions.lastDayInMonth().matchEntire(origin)?.let {
                LastDayOfMonth(monthNo = MonthResolved.month(it.groupValues[2]))
            }
}

private class LastDayInFebFromString(origin: String) : DayMarkFromString(origin) {
    override fun mark(): DayMark? =
            Definitions.lastDayInFeb().matchEntire(origin)?.let {
                LastDayOfMonth(monthNo = Month.FEBRUARY)
            }
}

private class WeekdayInMonthFromString(origin: String) : DayMarkFromString(origin) {
    override fun mark(): DayMark? =
            Definitions.dayOfWeek().matchEntire(origin)?.let {
                WeekdayInMonth(
                        monthNo = MonthResolved.month(it.groupValues[3]),
                        weekday = WeekdayResolved.datOfWeek(it.groupValues[2]),
                        weekNoInMonth = WeekNoResolved.weekNoInMonth(it.groupValues[1]))
            }
}

internal object MonthResolved {
    private val resolution = mapOf(
            "янв" to Month.JANUARY,
            "фев" to Month.FEBRUARY,
            "мар" to Month.MARCH,
            "апр" to Month.APRIL,
            "мая" to Month.MAY,
            "июн" to Month.JUNE,
            "июл" to Month.JULY,
            "авг" to Month.AUGUST,
            "сен" to Month.SEPTEMBER,
            "окт" to Month.OCTOBER,
            "ноя" to Month.NOVEMBER,
            "дек" to Month.DECEMBER)

    fun month(name: String) = resolution[name.take(3)] ?: throw MetaCalendarParseException("Unknown month $name")
}

internal object WeekdayResolved {
    private val resolution = mapOf(
            "пон" to DayOfWeek.MONDAY,
            "вто" to DayOfWeek.TUESDAY,
            "сре" to DayOfWeek.WEDNESDAY,
            "чет" to DayOfWeek.THURSDAY,
            "пят" to DayOfWeek.FRIDAY,
            "суб" to DayOfWeek.SATURDAY,
            "вос" to DayOfWeek.SUNDAY)

    fun datOfWeek(name: String) = resolution[name.take(3)]
            ?: throw MetaCalendarParseException("Unknown day of week $name")
}

internal object WeekNoResolved {
    private val resolution = mapOf(
            "перв" to 1,
            "втор" to 2,
            "трет" to 3,
            "четв" to 4)


    fun weekNoInMonth(name: String) = resolution[name.take(4)]
            ?: throw MetaCalendarParseException("Unknown no of week $name")
}