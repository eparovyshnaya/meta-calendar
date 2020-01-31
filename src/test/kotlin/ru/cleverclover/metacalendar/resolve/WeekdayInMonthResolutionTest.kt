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
import ru.cleverclover.metacalendar.WeekdayInMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Here we bind *the second* - *monday* - *of august*
 * to the precise date on a year of, say, 2019.
 * What date will [the second monday of august] have in 2019?
 */
// todo: test and describe failures

class WeekdayInMonthResolutionTest : ResolutionTest() {

    /**
     * August in 2019 starts after Monday,
     * so we expect the first partial week to be skipped
     * */
    @Test
    fun monthStartsAfterTheDay() {
        val mark = WeekdayInMonth(Month.AUGUST, 2, DayOfWeek.MONDAY)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 12)
        testResolution(mark, expectedDate)
    }

    /**
     * August in 2019 starts exactly with Thursday,
     * so we do not expect the first partial week to be skipped
     * */
    @Test
    fun monthStartsWithTheDay() {
        val mark = WeekdayInMonth(Month.AUGUST, 2, DayOfWeek.THURSDAY)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 8)
        testResolution(mark, expectedDate)
    }

    /**
     * August in 2019 starts before Friday,
     * so we do not expect the first partial week to be skipped.
     *
     * Check end-of-the-day resolution also.
     * */
    @Test
    fun monthStartsBeforeTheDay() {
        val mark = WeekdayInMonth(Month.AUGUST, 2, DayOfWeek.FRIDAY)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 9)
        testResolution(mark, expectedDate)
    }

}
