package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.cleverclover.metacalendar.*
import java.time.Month

class PeriodParseTest {

    @Test
    fun parsed() {
        val origin = "с 1 января по 8 февраля"
        val period = PeriodFromRangeDefinition(origin).bounds().period()
        assert(period.start == DayOfMonth(Month.JANUARY, 1)) {
            "Period originated in [$origin] must not start with ${period.start}"
        }
        assert(period.end == DayOfMonth(Month.FEBRUARY, 8)) {
            "Period originated in [$origin] must not end with ${period.end}"
        }
    }

    @Test
    fun incorrectRangeDefinitionFails() {
        val origin = "от 1 января до 8 января"
        assertThrows<MetaCalendarParseException> {
            PeriodFromRangeDefinition(origin).bounds().period()
        }
    }

    @Test
    fun incorrectDayMarkFails() {
        assertThrows<MetaCalendarParseException> {
            PeriodFromRangeDefinition("с 32 марта по 8 июль").bounds().period()
        }
    }

    @Test
    fun incorrectStartDefinitionFails() {
        assertThrows<MetaCalendarParseException> {
            PeriodFromBoundDefinitions(null, "8 июля").bounds().period()
        }
    }

    @Test
    fun incorrectEndDefinitionFails() {
        assertThrows<MetaCalendarParseException> {
            PeriodFromBoundDefinitions("21 сентября", null).bounds().period()
        }
    }
}
