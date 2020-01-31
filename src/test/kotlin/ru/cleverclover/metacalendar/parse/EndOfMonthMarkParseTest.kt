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
import ru.cleverclover.metacalendar.LastDayOfMonth
import ru.cleverclover.metacalendar.MetaCalendarParseException
import ru.cleverclover.metacalendar.ParsedDayMark
import java.time.Month

class EndOfMonthMarkParseTest {

    @Test
    fun regular() =
        assert(ParsedDayMark("конец июня").mark() == LastDayOfMonth(Month.JUNE))

    @Test
    fun february() =
        assert(ParsedDayMark("28 (29) февраля").mark() == LastDayOfMonth(Month.FEBRUARY))

    @Test
    fun incorrectFormatFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("хрю-хрю-хрю").mark() }
    }

    @Test
    fun unknownMonthFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("конец хрюкабря").mark() }
    }

    @Test
    fun unknownEndTitleFails() {
        assertThrows<MetaCalendarParseException> { ParsedDayMark("END-OF ноябрь").mark() }
    }

}
