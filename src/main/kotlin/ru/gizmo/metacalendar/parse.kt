package ru.gizmo.metacalendar

import java.time.DayOfWeek
import java.time.Month

open class MetaCalendarParseException(message: String, cause: Throwable? = null) : Exception(message, cause)

class ParsedPeriod(private val origin: String) {

    fun period(): Period {
        val matcher = periodDefinition.matchEntire(origin)
                ?: throw MetaCalendarParseException("no period definition")
        return Period(
                ParsedDayMark(matcher.groups[1]!!.value).mark(),
                ParsedDayMark(matcher.groups[2]!!.value).mark())
    }
}

class ParsedDayMark(private val origin: String) {
    fun mark(): DayMark {
        dayMarkParsers.forEach { parser ->
            parser(origin)?.let { return it }
        }
        throw MetaCalendarParseException("format of day mark $origin is not supported")
    }
}


// todo: get rid of all this ugly STATIC stuff

private val dayMarkParsers = listOf( // todo: open it for extension
        { origin: String ->
            dayOfMonthMarkDefinition.matchEntire(origin)?.let {
                DayOfMonth(
                        monthNo = findConstant(it.groupValues[2], monthNameToEnum) as Month,
                        dayNo = it.groupValues[1].toInt())
            }
        },
        { origin: String ->
            lastWeekdayInMonthMarkDefinition.matchEntire(origin)?.let {
                LastWeekdayInMonth(
                        monthNo = findConstant(it.groupValues[2], monthNameToEnum) as Month,
                        weekday = findConstant(it.groupValues[1], dayOfWeekToEnum) as DayOfWeek)
            }
        },
        { origin: String ->
            dayOfWeekMarkDefinition.matchEntire(origin)?.let {
                WeekdayInMonth(
                        monthNo = findConstant(it.groupValues[3], monthNameToEnum) as Month,
                        weekday = findConstant(it.groupValues[2], dayOfWeekToEnum) as DayOfWeek,
                        weekNoInMonth = findConstant(it.groupValues[1], weekTextToNumber) as Int)
            }

        },
        { origin: String ->
            lastDayInMonthMarkDefinitionFeb.matchEntire(origin)?.let {
                LastDayOfMonth(monthNo = findConstant(it.groupValues[2], monthNameToEnum) as Month)
            }
        }
)

private fun findConstant(name: String, constants: Map<String, *>) = constants.asSequence().first { name.startsWith(it.key) }.value

private val monthNameToEnum = mapOf(
        "янв" to Month.JANUARY,
        "февр" to Month.FEBRUARY,
        "мар" to Month.MARCH,
        "апр" to Month.APRIL,
        "мая" to Month.MAY,
        "июн" to Month.JUNE,
        "июл" to Month.JULY,
        "авг" to Month.AUGUST,
        "сен" to Month.SEPTEMBER,
        "окт" to Month.OCTOBER,
        "нояб" to Month.NOVEMBER,
        "дек" to Month.DECEMBER)

private val dayOfWeekToEnum = mapOf(
        "пон" to DayOfWeek.MONDAY,
        "вт" to DayOfWeek.TUESDAY,
        "ср" to DayOfWeek.WEDNESDAY,
        "чет" to DayOfWeek.THURSDAY,
        "пят" to DayOfWeek.FRIDAY,
        "суб" to DayOfWeek.SATURDAY,
        "вос" to DayOfWeek.SUNDAY)

private val weekTextToNumber = mapOf(
        "пер" to 1,
        "втор" to 2,
        "трет" to 3,
        "чет" to 4)

//todo: get those from maps keysets
private const val groupMonth = "(янв.*|февр.*|март.*|апр.*|мая|июн.*|июл.*|авг.*|сен.*|окт.*|ноя.*|дек.*)"
private const val groupWeekday = "(пон.*|вт.*|ср.*|чет.*|пят.*|суб.*|вос.*)"

private val periodDefinition = "\\s*со?\\s+(.+)\\s+по\\s+(.+)\\s*".toRegex()
private val lastDayInMonthMarkDefinitionFeb = "\\s*(кон.*\\s+$groupMonth)\\s*".toRegex()
private val dayOfMonthMarkDefinition = "\\s*(\\d{1,2})\\s+$groupMonth\\s*".toRegex()
private val dayOfWeekMarkDefinition = "\\s*(пер.*|втор.*|трет.*|чет.*)\\s+$groupWeekday\\s+$groupMonth\\s*".toRegex()
private val lastWeekdayInMonthMarkDefinition = "\\s*послед.*\\s+$groupWeekday\\s+$groupMonth\\s*".toRegex()