package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.cleverclover.metacalendar.*
import java.time.Month

class DayOfMonthMarkParseTest {

    @Test
    fun parsed() =
            assert(ParsedDayMark("11 января").mark() == DayOfMonth(Month.JANUARY, 11))

    @Test
    fun incorrectFormatFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("хрю-хрю хрю-хрю-хрю").mark() }
    }

    @Test
    fun outOfBoundDayFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("33 января").mark() }
    }

    @Test
    fun unknownMonthFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("33 хрюкабря").mark() }
    }
}
