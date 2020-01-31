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

import ru.cleverclover.metacalendar.meta.DayMark
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

abstract class ResolutionTest {

    protected val startingHour = LocalTime.of(0, 0, 0, 0)!!
    protected val endingHour = LocalTime.of(23, 59, 59, 999)!!

    protected fun testResolution(mark: DayMark, expected: LocalDate) {
        // given
        val zone = ZoneId.systemDefault()

        // when
        val resolved = mark.resolve(expected.year, zone, true)

        // then
        val expectation = ZonedDateTime.of(expected, startingHour, zone)
        assert(expectation == resolved)
        {
            """$mark, is resolved incorrectly to year ${expected.year} and zone $zone:
                    expected: $expected
                      actual: $resolved"""
        }
    }

}
