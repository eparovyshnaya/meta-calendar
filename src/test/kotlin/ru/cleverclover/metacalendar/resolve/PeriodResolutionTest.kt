package ru.cleverclover.metacalendar.resolve

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.DayOfMonth
import ru.cleverclover.metacalendar.Period
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

class PeriodResolutionTest : ResolutionTest() {
    @Test
    fun crossYear() {
        // given
        val from = DayOfMonth(Month.AUGUST, 2)
        val to = DayOfMonth(Month.JANUARY, 8)
        val period = Period(from, to)
        val year = 2019
        val zone = ZoneId.systemDefault()

        // when
        val resolved = period.resolve(year, zone)

        // then
        assert(resolved.size == 2) { "Cross-year period $period should be resolved twice, but we have $resolved" }
        val expectation = setOf(
                Pair(
                        ZonedDateTime.of(LocalDate.of(year - 1, Month.AUGUST, 2), startingHour, zone),
                        ZonedDateTime.of(LocalDate.of(year, Month.JANUARY, 8), endingHour, zone)),
                Pair(
                        ZonedDateTime.of(LocalDate.of(year, Month.AUGUST, 2), startingHour, zone),
                        ZonedDateTime.of(LocalDate.of(year + 1, Month.JANUARY, 8), endingHour, zone)))
        assert(resolved == expectation) {
            """
               Period $period is resolved incorrectly to year $year and zone $zone:
                 expected:
                 $expectation
                 actual:
                 $resolved"""
        }
    }

    @Test
    fun plainPeriod() {
        // given
        val from = DayOfMonth(Month.JANUARY, 8)
        val to = DayOfMonth(Month.AUGUST, 2)
        val period = Period(from, to)
        val year = 2019
        val zone = ZoneId.systemDefault()

        // when
        val resolved = period.resolve(year, zone)

        // then
        assert(resolved.size == 1) { "The period $period does not cross the year, should be resolved one, but we have $resolved" }
        val expectation = setOf(Pair(
                ZonedDateTime.of(LocalDate.of(year, Month.JANUARY, 8), startingHour, zone),
                ZonedDateTime.of(LocalDate.of(year, Month.AUGUST, 2), endingHour, zone)))
        assert(resolved == expectation) {
            """
               Period $period is resolved incorrectly to year $year and zone $zone:
                 expected:
                 $expectation
                 actual:
                 $resolved"""
        }
    }
}