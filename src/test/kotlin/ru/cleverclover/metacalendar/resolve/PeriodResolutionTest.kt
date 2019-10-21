package ru.cleverclover.metacalendar.resolve

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.cleverclover.metacalendar.DayOfMonth
import ru.cleverclover.metacalendar.Period
import ru.cleverclover.metacalendar.notedResolvedPeriod
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

class PeriodResolutionTest : ResolutionTest() {

    @ParameterizedTest(name = "same month: {0}")
    @ValueSource(booleans = [true, false])
    fun crossYearPeriodResolves(sameMonth: Boolean) {
        // given
        val startMonth = Month.AUGUST
        val endMonth = if (sameMonth) startMonth else Month.JANUARY
        val from = DayOfMonth(startMonth, 20)
        val to = DayOfMonth(endMonth, 18)
        val note = "Awesome time"
        val period = Period(from, to, note)
        val year = 2019
        val zone = ZoneId.systemDefault()

        // when
        val resolved = period.resolve(year, zone)

        // then
        assert(resolved.size == 2) { "Cross-year periods $period should be resolved twice, but we have $resolved" }
        val expectation = setOf(
                Pair(
                        ZonedDateTime.of(LocalDate.of(year - 1, startMonth, from.dayNo), startingHour, zone),
                        ZonedDateTime.of(LocalDate.of(year, endMonth, to.dayNo), endingHour, zone))
                        .notedResolvedPeriod(note),
                Pair(
                        ZonedDateTime.of(LocalDate.of(year, startMonth, from.dayNo), startingHour, zone),
                        ZonedDateTime.of(LocalDate.of(year + 1, endMonth, to.dayNo), endingHour, zone))
                        .notedResolvedPeriod(note))
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
    fun plainPeriodResolves() {
        // given
        val from = DayOfMonth(Month.JANUARY, 8)
        val to = DayOfMonth(Month.AUGUST, 2)
        val period = Period(from, to)
        val year = 2019
        val zone = ZoneId.systemDefault()

        // when
        val resolved = period.resolve(year, zone)

        // then
        assert(resolved.size == 1) { "The periods $period does not cross the year, should be resolved one, but we have $resolved" }
        val expectation = setOf(
                Pair(
                        ZonedDateTime.of(LocalDate.of(year, Month.JANUARY, 8), startingHour, zone),
                        ZonedDateTime.of(LocalDate.of(year, Month.AUGUST, 2), endingHour, zone))
                        .notedResolvedPeriod())
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
    fun backwardPeriodConstructionFails() {
        assertThrows<IllegalArgumentException> {
            Pair(
                    ZonedDateTime.of(LocalDate.of(2019, Month.JANUARY, 1), startingHour, ZoneId.systemDefault()),
                    ZonedDateTime.of(LocalDate.of(2018, Month.JANUARY, 1), endingHour, ZoneId.systemDefault()))
                    .notedResolvedPeriod()
        }
    }
}