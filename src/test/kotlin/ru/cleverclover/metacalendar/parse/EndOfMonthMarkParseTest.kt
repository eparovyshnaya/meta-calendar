package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.cleverclover.metacalendar.*
import java.time.Month

class EndOfMonthMarkParseTest {

    @Test
    fun parsed() =
            assert(ParsedDayMark("конец июня").mark() == LastDayOfMonth(Month.JUNE))

    @Test
    fun incorrectFormatFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("хрю-хрю-хрю").mark() }
    }

    @Test
    fun unknownMonthFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("конец хрюкабря").mark() }
    }

    @Test
    fun unknownEndTitleFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("END-OF ноябрь").mark() }
    }
}
