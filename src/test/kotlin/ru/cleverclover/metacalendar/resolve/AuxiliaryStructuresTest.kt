package ru.cleverclover.metacalendar.resolve

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ru.cleverclover.metacalendar.*
import java.time.DayOfWeek
import java.time.Month

class AuxiliaryStructuresTest {

    @ParameterizedTest(name = "Parse month from {0}")
    @CsvSource(
            "января, 1",
            "февраля, 2",
            "марта, 3",
            "апреля, 4",
            "мая, 5",
            "июня, 6",
            "июля, 7",
            "августа, 8",
            "сентября, 9",
            "октября, 10",
            "ноября, 11",
            "декабря, 12")
    fun monthFound(origin: String, month: Int) {
        assert(MonthResolved(origin).month() == Month.of(month))
    }

    @Test
    fun monthNotFound() {
        assertThrows<MetaCalendarParseException> { MonthResolved("хрюкабря").month() }
    }

    @ParameterizedTest(name = "Parse week day from {0}")
    @CsvSource(
            "понедельник, 1",
            "вторник, 2",
            "среда, 3",
            "четверг, 4",
            "пятница, 5",
            "суббота, 6",
            "воскресенье, 7")
    fun weekDayFound(origin: String, weekDay: Int) {
        assert(WeekdayResolved(origin).datOfWeek() == DayOfWeek.of(weekDay))
    }

    @Test
    fun weekDayNotFound() {
        assertThrows<MetaCalendarParseException> { WeekdayResolved("хрюкота").datOfWeek() }
    }

    @ParameterizedTest(name = "Parse week day from {0}")
    @CsvSource(
            "первый, 1",
            "второй, 2",
            "третий, 3",
            "четвертый, 4")
    fun weekNoNotFound(origin: String, weekNo: Int) {
        assert(WeekNoResolved(origin).weekNoInMonth() == weekNo)
    }

    @Test
    fun weekNoNotFound() {
        assertThrows<MetaCalendarParseException> { WeekNoResolved("пятая").weekNoInMonth() }
    }

    @ParameterizedTest(name = "Read last of of month {0}")
    @CsvSource(
            "1, 31",
            "2, 29",
            "3, 31",
            "4, 30",
            "5, 31",
            "6, 30",
            "7, 31",
            "8, 31",
            "9, 30",
            "10, 31",
            "11, 30",
            "12, 31")
    fun endOfMonthValid(month: Int, lastDay: Int) {
        assert(EndOfMonth(Month.of(month)).lastDay() == lastDay)
    }
}