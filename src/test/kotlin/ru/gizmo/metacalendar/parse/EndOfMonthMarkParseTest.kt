package ru.gizmo.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.gizmo.metacalendar.*
import java.time.DayOfWeek
import java.time.Month

class EndOfMonthMarkParseTest {

    @Test
    fun endOfMonthParsed() =
            assert(ParsedDayMark("конец июня").mark() == LastDayOfMonth(Month.JUNE))

    // todo: test parse failures
}
