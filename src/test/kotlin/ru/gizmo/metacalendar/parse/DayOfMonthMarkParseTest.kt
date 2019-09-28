package ru.gizmo.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.gizmo.metacalendar.*
import java.time.DayOfWeek
import java.time.Month

class DayOfMonthMarkParseTest {

    @Test
    fun dayOfMonthParsed() =
            assert(ParsedDayMark("11 января").mark() == DayOfMonth(Month.JANUARY, 11))

    // todo: test parse failures
}
