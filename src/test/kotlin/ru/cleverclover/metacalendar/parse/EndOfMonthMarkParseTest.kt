package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.cleverclover.metacalendar.*
import java.time.Month

class EndOfMonthMarkParseTest {

    @Test
    fun regular() =
            assert(ParsedDayMark("конец июня").mark() == LastDayOfMonth(Month.JUNE))

    @Test
    fun february() =
            assert(ParsedDayMark("28 (29) февраля").mark() == LastDayOfMonth(Month.FEBRUARY))

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
