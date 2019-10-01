package ru.cleverclover.metacalendar.compose

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import ru.cleverclover.metacalendar.*
import java.time.DayOfWeek
import java.time.Month

class DslTest {

    @Test
    fun readDsl() = assertPeriodsAreOk(create(), setOf(
            Period(
                    WeekdayInMonth(Month.NOVEMBER, 3, DayOfWeek.WEDNESDAY),
                    DayOfMonth(Month.JANUARY, 1),
                    "dark time"),
            Period(
                    LastWeekdayInMonth(Month.APRIL, DayOfWeek.FRIDAY),
                    LastDayOfMonth(Month.MAY)),
            Period(
                    LastDayOfMonth(Month.OCTOBER),
                    LastDayOfMonth(Month.OCTOBER),
                    "Halloween!"))
    )

    private fun create() =
            calendar {
                period(
                        from = "третья среда ноября",
                        till = "1 января",
                        note = "dark time"
                )
                period(
                        from = "последняя пятница апреля",
                        till = "конец мая"
                )
                period(
                        from = "конец октября",
                        till = "конец октября",
                        note = "Halloween!"
                )
            }

    private fun assertPeriodsAreOk(calendar: MetaCalendar, expectedPeriods: Set<Period>) = with(calendar.periods()) {
        assertTrue(this == expectedPeriods)
        { "Periods do not meet expectations: \nactual:\n${this}\nexpected:\n$expectedPeriods" }
    }
}