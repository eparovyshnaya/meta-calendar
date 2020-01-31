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
package ru.cleverclover.metacalendar.resolve

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.LastWeekdayInMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

class LastWeekdayInMonthResolutionTest : ResolutionTest() {

    @Test
    fun fifthWeekday() {
        testResolution(
            LastWeekdayInMonth(Month.SEPTEMBER, DayOfWeek.MONDAY),
            LocalDate.of(2019, Month.SEPTEMBER, 30)
        )
    }

    @Test
    fun fourthWeekday() {
        testResolution(
            LastWeekdayInMonth(Month.SEPTEMBER, DayOfWeek.SATURDAY),
            LocalDate.of(2019, Month.SEPTEMBER, 28)
        )
    }

    @Test
    fun fifthWeekdayInLeapFebruary() {
        testResolution(
            LastWeekdayInMonth(Month.FEBRUARY, DayOfWeek.SATURDAY),
            LocalDate.of(2020, Month.FEBRUARY, 29)
        )
    }

    @Test
    fun fourthWeekdayInShortFebruary() {
        testResolution(
            LastWeekdayInMonth(Month.FEBRUARY, DayOfWeek.THURSDAY),
            LocalDate.of(2019, Month.FEBRUARY, 28)
        )
    }

}
