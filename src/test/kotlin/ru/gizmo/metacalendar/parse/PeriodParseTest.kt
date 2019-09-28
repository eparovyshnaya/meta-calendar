package ru.gizmo.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.gizmo.metacalendar.DayOfMonth
import ru.gizmo.metacalendar.MetaCalendarParseException
import ru.gizmo.metacalendar.ParsedPeriod
import java.time.Month

class PeriodParseTest {

    @Test
    fun periodParsed() {
        val origin = "с 1 января по 8 января"
        val period = ParsedPeriod(origin).period()
        assert(period.start == DayOfMonth(Month.JANUARY, 1)) {
            "Period originated in [$origin] must not start with ${period.start}"
        }
        assert(period.end == DayOfMonth(Month.JANUARY, 8)) {
            "Period originated in [$origin] must not end with ${period.end}"
        }
    }

    @Test
    fun periodParsingFailsOnContract() {
        val origin = "от 1 января до 8 января"
        assertThrows<MetaCalendarParseException> {
            ParsedPeriod(origin).period()
        }
    }

    @Test
    fun periodParsingFailsOnDayMark() {
        val origin = "c 32 января по 8 февраля"
        assertThrows<MetaCalendarParseException> {
            ParsedPeriod(origin).period()
        }
    }
}
