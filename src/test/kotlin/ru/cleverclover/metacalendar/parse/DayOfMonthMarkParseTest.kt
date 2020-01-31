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
import ru.cleverclover.metacalendar.meta.DayOfMonth
import java.time.Month

class DayOfMonthMarkParseTest {

    @Test
    fun parsed() =
        assert(
            ParsedDayMark("11 января").mark() == DayOfMonth(
            Month.JANUARY,
            11
        )
        )

    @Test
    fun incorrectFormatFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark(
            "хрю-хрю хрю-хрю-хрю"
        ).mark() }
    }

    @Test
    fun outOfBoundDayFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark(
            "33 января"
        ).mark() }
    }

    @Test
    fun unknownMonthFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark(
            "24 хрюкабря"
        ).mark() }
    }

}
