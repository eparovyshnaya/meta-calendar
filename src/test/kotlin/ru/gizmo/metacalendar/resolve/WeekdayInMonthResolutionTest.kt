package ru.gizmo.metacalendar.resolve

import org.junit.jupiter.api.Test
import ru.gizmo.metacalendar.WeekdayInMonth
import java.time.*

/**
 * Here we bind *the second* - *monday* - *of august*
 * to the precise date on a year of, say, 2019.
 * What date will [the second monday of august] have in 2019?
 */
// todo: test and describe failures

class WeekdayInMonthResolutionTest : ResolutionTest() {
    /**
     * August in 2019 starts after Monday,
     * so we expect the first partial week to be skipped
     * */
    @Test
    fun monthStartsAfterTheDay() {
        val mark = WeekdayInMonth(Month.AUGUST, 2, DayOfWeek.MONDAY)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 12)
        testResolution(mark, expectedDate)
    }

    /**
     * August in 2019 starts exactly with Thursday,
     * so we do not expect the first partial week to be skipped
     * */
    @Test
    fun monthStartsWithTheDay() {
        val mark = WeekdayInMonth(Month.AUGUST, 2, DayOfWeek.THURSDAY)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 8)
        testResolution(mark, expectedDate)
    }

    /**
     * August in 2019 starts before Friday,
     * so we do not expect the first partial week to be skipped.
     *
     * Check end-of-the-day resolution also.
     * */
    @Test
    fun monthStartsBeforeTheDay() {
        val mark = WeekdayInMonth(Month.AUGUST, 2, DayOfWeek.FRIDAY)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 9)
        testResolution(mark, expectedDate)
    }

}
