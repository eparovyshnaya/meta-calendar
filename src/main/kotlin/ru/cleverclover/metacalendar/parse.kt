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

/**
 * The parsing for a natural language string to a [MetaCalendar]'s [Period]
 *
 * Sample input is *с конца февраля по третий вторник августа*
 * */
class PeriodFromRangeDefinition(private val origin: String) {

    fun bounds(): Pair<DayMark, DayMark> {
        // todo: There are 5 reg-exes in the file. Find a way to cash 'em without loading the code.
        val periodDefinition = "\\s*со?\\s+(.+)\\s+по\\s+(.+)\\s*".toRegex()
        val matcher = periodDefinition.matchEntire(origin)
                ?: throw MetaCalendarParseException("no periods definition in $origin")
        return PeriodFromBoundDefinitions(matcher.groups[1]?.value, matcher.groups[2]?.value).bounds()
    }
}

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
    private val dayOfMonthMarkDefinition = "\\s*(\\d{1,2})\\s+$groupMonth\\s*".toRegex()
    override fun mark(): DayMark? =
            dayOfMonthMarkDefinition.matchEntire(origin)?.let {
                val month = MonthResolved(it.groupValues[2]).month()
                val day = it.groupValues[1].toInt()
                if (day in 1..EndOfMonth(month).lastDay()) {
                    DayOfMonth(monthNo = month, dayNo = day)
                } else {
                    throw MetaCalendarParseException(
                            "$origin represents invalid day-of-month mark: day $day should stay in range [1, ${EndOfMonth(month)}]")
                }
            }
}

private class LastWeekdayInMonthFromString(origin: String) : DayMarkFromString(origin) {
    private val lastWeekdayInMonthMarkDefinition = "\\s*послед.*\\s+$groupWeekday\\s+$groupMonth\\s*".toRegex()
    override fun mark(): DayMark? =
            lastWeekdayInMonthMarkDefinition.matchEntire(origin)?.let {
                LastWeekdayInMonth(
                        monthNo = MonthResolved(it.groupValues[2]).month(),
                        weekday = WeekdayResolved(it.groupValues[1]).datOfWeek())
            }
}

private class LastDayInMonthFromString(origin: String) : DayMarkFromString(origin) {
    private val lastDayInMonthMarkDefinitionFeb = "\\s*(кон.*\\s+$groupMonth)\\s*".toRegex()
    override fun mark(): DayMark? =
            lastDayInMonthMarkDefinitionFeb.matchEntire(origin)?.let {
                LastDayOfMonth(monthNo = MonthResolved(it.groupValues[2]).month())
            }
}

private class WeekdayInMonthFromString(origin: String) : DayMarkFromString(origin) {
    private val dayOfWeekMarkDefinition = "\\s*(пер.*|втор.*|трет.*|чет.*)\\s+$groupWeekday\\s+$groupMonth\\s*".toRegex()
    override fun mark(): DayMark? =
            dayOfWeekMarkDefinition.matchEntire(origin)?.let {
                WeekdayInMonth(
                        monthNo = MonthResolved(it.groupValues[3]).month(),
                        weekday = WeekdayResolved(it.groupValues[2]).datOfWeek(),
                        weekNoInMonth = WeekNoResolved(it.groupValues[1]).weekNoInMonth())
            }
}

internal class MonthResolved(val name: String) {
    fun month(): Month = when (name.take(3)) {
        "янв" -> Month.JANUARY
        "фев" -> Month.FEBRUARY
        "мар" -> Month.MARCH
        "апр" -> Month.APRIL
        "мая" -> Month.MAY
        "июн" -> Month.JUNE
        "июл" -> Month.JULY
        "авг" -> Month.AUGUST
        "сен" -> Month.SEPTEMBER
        "окт" -> Month.OCTOBER
        "ноя" -> Month.NOVEMBER
        "дек" -> Month.DECEMBER
        else -> throw MetaCalendarParseException("Unknown month $name")
    }
}

internal class WeekdayResolved(val name: String) {
    fun datOfWeek(): DayOfWeek = when (name.take(3)) {
        "пон" -> DayOfWeek.MONDAY
        "вто" -> DayOfWeek.TUESDAY
        "сре" -> DayOfWeek.WEDNESDAY
        "чет" -> DayOfWeek.THURSDAY
        "пят" -> DayOfWeek.FRIDAY
        "суб" -> DayOfWeek.SATURDAY
        "вос" -> DayOfWeek.SUNDAY
        else -> throw MetaCalendarParseException("Unknown day of week $name")
    }
}

internal class WeekNoResolved(val name: String) {
    fun weekNoInMonth(): Int = when (name.take(4)) {
        "перв" -> 1
        "втор" -> 2
        "трет" -> 3
        "четв" -> 4
        else -> throw MetaCalendarParseException("Unknown no of week $name")
    }
}

// todo: this one is i10n-independent, move it out of here at any chance
internal class EndOfMonth(val month: Month) {
    fun lastDay(): Int = when (month) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> 29
        Month.MARCH -> 31
        Month.APRIL -> 30
        Month.MAY -> 31
        Month.JUNE -> 30
        Month.JULY -> 31
        Month.AUGUST -> 31
        Month.SEPTEMBER -> 30
        Month.OCTOBER -> 31
        Month.NOVEMBER -> 30
        Month.DECEMBER -> 31
    }
}

// todo: Used to compose reg-exes. Should be somehow defeated.
private const val groupMonth = "(янв.*|февр.*|март.*|апр.*|мая|июн.*|июл.*|авг.*|сен.*|окт.*|ноя.*|дек.*)"
private const val groupWeekday = "(пон.*|вт.*|ср.*|чет.*|пят.*|суб.*|вос.*)"
