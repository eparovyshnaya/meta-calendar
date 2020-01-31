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

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.cleverclover.metacalendar.LastWeekdayInMonth
import ru.cleverclover.metacalendar.MetaCalendarParseException
import ru.cleverclover.metacalendar.ParsedDayMark
import java.time.DayOfWeek
import java.time.Month

class LastWeekdayInMonthMarkParseTest {

    @Test
    fun parsed() =
        assert(
            ParsedDayMark("последний вторник ноября").mark() ==
                    LastWeekdayInMonth(Month.NOVEMBER, DayOfWeek.TUESDAY)
        )

    @Test
    fun incorrectFormatFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("хрю-хрю-хрю").mark() }
    }

    @Test
    fun incorrectWeekNoFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("хрю-хрю вторник ноября").mark() }
    }

    @Test
    fun incorrectDayOfMonthFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("первый хрю-хрю ноября").mark() }
    }

    @Test
    fun incorrectMonthFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("первый четверг хрюкабря").mark() }
    }

}
