package ru.gizmo.metacalendar.parse

import org.junit.jupiter.api.Test
import ru.gizmo.metacalendar.LastWeekdayInMonth
import ru.gizmo.metacalendar.ParsedDayMark
import java.time.DayOfWeek
import java.time.Month

class LastWeekdayInMonthMarkParseTest {

    @Test
    fun parsed() =
            assert(ParsedDayMark("последний вторник ноября").mark() ==
                    LastWeekdayInMonth(Month.NOVEMBER, DayOfWeek.TUESDAY))

    // todo: test parse failures
}