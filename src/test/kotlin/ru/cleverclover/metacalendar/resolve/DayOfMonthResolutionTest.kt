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
import org.junit.jupiter.api.assertThrows
import ru.cleverclover.metacalendar.meta.DayOfMonth
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

//todo: test ending hour resolution
class DayOfMonthResolutionTest : ResolutionTest() {

    @Test
    fun regular() {
        val mark = DayOfMonth(Month.AUGUST, 23)
        val expectedDate = LocalDate.of(2019, Month.AUGUST, 23)
        testResolution(mark, expectedDate)
    }

    @Test
    fun leapDay() {
        val mark = DayOfMonth(Month.FEBRUARY, 29)
        val expectedDate = LocalDate.of(2020, Month.FEBRUARY, 29)
        testResolution(mark, expectedDate)
    }

    @Test
    fun leapFailure() {
        val mark = DayOfMonth(Month.FEBRUARY, 29)
        // todo: custom  exception
        assertThrows<Exception> { mark.resolve(2019, ZoneId.systemDefault(), true) }
    }

}
