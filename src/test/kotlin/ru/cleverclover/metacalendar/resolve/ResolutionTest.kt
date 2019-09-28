package ru.cleverclover.metacalendar.resolve

import ru.cleverclover.metacalendar.DayMark
import java.time.*

abstract class ResolutionTest {
    protected val startingHour = LocalTime.of(0, 0, 0, 0)
    protected val endingHour = LocalTime.of(23, 59, 59, 999)

    // todo: mark.toString should return original source, if any, and fields otherwise
    protected fun testResolution(mark: DayMark, expected: LocalDate) {
        // given
        val zone = ZoneId.systemDefault()

        // when
        val resolved = mark.resolve(expected.year, zone, true)

        // then
        val expectation = ZonedDateTime.of(expected, startingHour, zone)
        assert(expectation == resolved)
        {
            """$mark, is resolved incorrectly to year ${expected.year} and zone $zone:
                    expected: $expected
                      actual: $resolved"""
        }
    }
}