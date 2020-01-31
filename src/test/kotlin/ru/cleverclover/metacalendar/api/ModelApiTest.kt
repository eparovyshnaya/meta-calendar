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
package ru.cleverclover.metacalendar.api

import org.junit.jupiter.api.Test
import ru.cleverclover.metacalendar.DayOfMonth
import ru.cleverclover.metacalendar.MetaCalendar
import ru.cleverclover.metacalendar.Period
import java.time.Month

/**
 * Illustrate api methods and three ways to construct and fill MetaCalendar
 */
class ModelApiTest {

    @Test
    fun addPeriod() {
        assert(1 == MetaCalendar(setOf(period())).size())
    }

    @Test
    fun retrievePeriod() {
        assert(period() == MetaCalendar().apply { add(period()) }.periods().random())
    }

    @Test
    fun removePeriod() {
        assert(1 == MetaCalendar(period(), period("another")).also { it.remove(period()) }.size())
    }

    private fun period(note: String? = null) =
        Period(DayOfMonth(Month.JANUARY, 1), DayOfMonth(Month.JANUARY, 31), note)

}
