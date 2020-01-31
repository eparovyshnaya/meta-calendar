/*******************************************************************************
 * Copyright (c) 2019, 2020 CleverClover
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT which is available at
 * https://spdx.org/licenses/MIT.html#licenseText
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *     CleverClover - initial API and implementation
 *******************************************************************************
 */
package ru.cleverclover.metacalendar.parse

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import ru.cleverclover.metacalendar.meta.WeekdayInMonth
import java.time.DayOfWeek
import java.time.Month

class WeekdayInMonthMarlParseTest {

    @ParameterizedTest(name = "parse {0}")
    @CsvSource(
        "первый понедельник июля, 1, 1, 7",
        "второй четверг августа, 2, 4, 8",
        "третья субброта сентября, 3, 6, 9",
        "четвертое воскресеье декабря, 4, 7, 12"
    )
    fun parsed(origin: String, weekNo: Int, weekDay: Int, month: Int) =
        assert(
            ParsedDayMark(origin).mark() ==
                    WeekdayInMonth(
                        Month.of(month),
                        weekNo,
                        DayOfWeek.of(weekDay)
                    )
        )

    @ParameterizedTest(name = "wait failure for {0}")
    @ValueSource(
        strings = [
            "хрю-хрю",
            "пятый вторник ноября",
            "второй хрю-хрю ноября",
            "второй вторник хрюкабря"
        ]
    )
    fun incorrectFormatFails(origin: String) {
        assertThrows<MetaCalendarParseException> { ParsedDayMark(
            origin
        ).mark() }
    }

}
