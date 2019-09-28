package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.*
import java.time.Month

class DayOfMonthMarkParseTest {

    @Test
    fun dayOfMonthParsed() =
            assert(ParsedDayMark("11 января").mark() == DayOfMonth(Month.JANUARY, 11))

    // todo: test parse failures
}
