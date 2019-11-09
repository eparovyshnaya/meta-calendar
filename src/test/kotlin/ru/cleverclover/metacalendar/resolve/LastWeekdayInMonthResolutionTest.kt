/*******************************************************************************
 * Copyright (c) 2019 CleverClover
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
        val mark = LastWeekdayInMonth(Month.SEPTEMBER, DayOfWeek.MONDAY)
        val expectedDate = LocalDate.of(2019, Month.SEPTEMBER, 30)
        testResolution(mark, expectedDate)
    }

    @Test
    fun fourthWeekday() {
        val mark = LastWeekdayInMonth(Month.SEPTEMBER, DayOfWeek.SATURDAY)
        val expectedDate = LocalDate.of(2019, Month.SEPTEMBER, 28)
        testResolution(mark, expectedDate)
    }

    @Test
    fun fifthWeekdayInLeapFebruary() {
        val mark = LastWeekdayInMonth(Month.FEBRUARY, DayOfWeek.SATURDAY)
        val expectedDate = LocalDate.of(2020, Month.FEBRUARY, 29)
        testResolution(mark, expectedDate)
    }

    @Test
    fun fourthWeekdayInShortFebruary() {
        val mark = LastWeekdayInMonth(Month.FEBRUARY, DayOfWeek.THURSDAY)
        val expectedDate = LocalDate.of(2019, Month.FEBRUARY, 28)
        testResolution(mark, expectedDate)
    }
}
