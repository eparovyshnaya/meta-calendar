package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.LastWeekdayInMonth
import ru.cleverclover.metacalendar.ParsedDayMark
import java.time.DayOfWeek
import java.time.Month

class LastWeekdayInMonthMarkParseTest {

    @Test
    fun parsed() =
            assert(ParsedDayMark("последний вторник ноября").mark() ==
                    LastWeekdayInMonth(Month.NOVEMBER, DayOfWeek.TUESDAY))

    // todo: test parse failures
}