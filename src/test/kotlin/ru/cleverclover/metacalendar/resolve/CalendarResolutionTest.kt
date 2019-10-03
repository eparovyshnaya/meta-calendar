package ru.cleverclover.metacalendar.resolve

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.DayOfMonth
import ru.cleverclover.metacalendar.MetaCalendar
import ru.cleverclover.metacalendar.Period
import java.time.*

class CalendarResolutionTest() : ResolutionTest() {

    @Test
    fun crossYearAndPlainPeriods() {
        val (year, zone) = Pair(2019, ZoneId.systemDefault())
        assert(MetaCalendar()
                .apply {
                    addPeriod(Period(DayOfMonth(Month.JANUARY, 1), DayOfMonth(Month.JANUARY, 21)))
                    addPeriod(Period(DayOfMonth(Month.NOVEMBER, 21), DayOfMonth(Month.JANUARY, 12)))
                }.resolve(year)
                ==
                setOf(
                        Pair(start(2019, Month.JANUARY, 1, zone),
                                end(2019, Month.JANUARY, 21, zone)),
                        Pair(start(2019, Month.NOVEMBER, 21, zone),
                                end(2020, Month.JANUARY, 12, zone)),
                        Pair(start(2018, Month.NOVEMBER, 21, zone),
                                end(2019, Month.JANUARY, 12, zone))
                ))
    }

    private fun start(year: Int, month: Month, day: Int, zone: ZoneId) =
            ZonedDateTime.of(LocalDate.of(year, month, day), startingHour, zone)

    private fun end(year: Int, month: Month, day: Int, zone: ZoneId) =
            ZonedDateTime.of(LocalDate.of(year, month, day), endingHour, zone)
}