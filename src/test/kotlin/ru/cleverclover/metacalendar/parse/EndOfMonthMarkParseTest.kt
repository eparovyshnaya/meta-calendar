package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.*
import java.time.Month

class EndOfMonthMarkParseTest {

    @Test
    fun endOfMonthParsed() =
            assert(ParsedDayMark("конец июня").mark() == LastDayOfMonth(Month.JUNE))

    // todo: test parse failures
}
