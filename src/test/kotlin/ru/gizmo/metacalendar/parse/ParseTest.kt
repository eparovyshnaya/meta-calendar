package ru.gizmo.metacalendar.parse

import org.junit.jupiter.api.Test
import ru.gizmo.metacalendar.DayOfMonth
import ru.gizmo.metacalendar.WeekdayInMonth
import ru.gizmo.metacalendar.dayMark
import ru.gizmo.metacalendar.period
import java.time.DayOfWeek
import java.time.Month

class ParseTest {

    @Test
    fun parsePeriod() {
        val origin = "с 1 января по 8 января"
        val period = period(origin) ?: error("Origin [$origin] failed to be parsed to a period")
        assert(period.start == DayOfMonth(Month.JANUARY, 1)) { "Period originated in [$origin] must not start with ${period.start}" }
        assert(period.end == DayOfMonth(Month.JANUARY, 8)) { "Period originated in [$origin] must not end with ${period.end}" }
    }

    @Test
    fun parseDayOfWeekInMonth() {
        val origin = "последний вторник ноября"
        val mark = dayMark(origin) ?: error("Origin [$origin] failed to be parsed to a day mark")
        assert(mark == WeekdayInMonth(Month.NOVEMBER, -1, DayOfWeek.TUESDAY))
    }

    @Test
    fun parseDayOfMonth() {
        val origin = "11 января"
        val mark = dayMark(origin) ?: error("Origin [$origin] failed to be parsed to a day mark")
        assert(mark == DayOfMonth(Month.JANUARY, 11))
    }
}
