package ru.cleverclover.metacalendar.api

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.DayOfMonth
import ru.cleverclover.metacalendar.MetaCalendar
import ru.cleverclover.metacalendar.Period
import java.time.Month

class ModelApiTest {
    @Test
    fun addPeriod() {
        assert(1 == calendarOfOnePeriod(period()).size())
    }

    @Test
    fun iteratePeriod() {
        assert(period() == calendarOfOnePeriod(period()).periods().random())
    }

    @Test
    fun removePeriod() {
        assert(0 == calendarOfOnePeriod(period()).also { it.removePeriod(period()) }.size())
    }

    private fun period() = Period(DayOfMonth(Month.JANUARY, 1), DayOfMonth(Month.JANUARY, 31))
    private fun calendarOfOnePeriod(period: Period) = MetaCalendar().apply {
        addPeriod(period)
    }
}