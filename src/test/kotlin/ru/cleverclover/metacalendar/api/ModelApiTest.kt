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
package ru.cleverclover.metacalendar.api

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.DayOfMonth
import ru.cleverclover.metacalendar.MetaCalendar
import ru.cleverclover.metacalendar.Period
import java.time.Month

class ModelApiTest {
    @Test
    fun addPeriod() {
        assert(1 == calendarOfOnePeriod(period()).size())
    }

    @Test
    fun iteratePeriod() {
        assert(period() == calendarOfOnePeriod(period()).periods().random())
    }

    @Test
    fun removePeriod() {
        assert(0 == calendarOfOnePeriod(period()).also { it.removePeriod(period()) }.size())
    }

    private fun period() = Period(DayOfMonth(Month.JANUARY, 1), DayOfMonth(Month.JANUARY, 31))

    private fun calendarOfOnePeriod(period: Period) = MetaCalendar(setOf(period))
}