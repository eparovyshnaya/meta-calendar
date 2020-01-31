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
import ru.cleverclover.metacalendar.meta.DayOfMonth
import ru.cleverclover.metacalendar.meta.MetaCalendar
import ru.cleverclover.metacalendar.meta.Period
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

class CalendarResolutionTest : ResolutionTest() {

    @Test
    fun crossYearAndPlainPeriods() {
        val (year, zone) = Pair(2019, ZoneId.systemDefault())
        val note = "Cross a year"
        assert(
            MetaCalendar()
            .apply {
                add(
                    Period(
                        DayOfMonth(
                            Month.JANUARY,
                            1
                        ), DayOfMonth(Month.JANUARY, 21)
                    )
                )
                add(
                    Period(
                        DayOfMonth(
                            Month.NOVEMBER,
                            21
                        ), DayOfMonth(Month.JANUARY, 12), note
                    )
                )
            }
            .resolve(year)
            .periods()
                ==
                setOf(
                    Pair(
                        start(year, Month.JANUARY, 1, zone),
                        end(year, Month.JANUARY, 21, zone)
                    )
                        .notedResolvedPeriod(),
                    Pair(
                        start(year, Month.NOVEMBER, 21, zone),
                        end(year + 1, Month.JANUARY, 12, zone)
                    )
                        .notedResolvedPeriod(note),
                    NotedResolvedPeriod(
                        start(year - 1, Month.NOVEMBER, 21, zone),
                        end(year, Month.JANUARY, 12, zone),
                        note
                    )
                ))
    }

    @Test
    fun distinctResolvedPeriods() {
        val zone = ZoneId.systemDefault()

        // given: we have a calendar of a single cross-year period
        val calendar = MetaCalendar()
            .apply { add(
                Period(
                    DayOfMonth(
                        Month.DECEMBER,
                        31
                    ), DayOfMonth(Month.JANUARY, 1)
                )
            ) }

        // when: we resolve it to a couple of subsequent years
        val periods = calendar.resolve(setOf(2019, 2020)).periods()

        // then: despite a cross-year period for a particular year is resolved to 2 periods,
        //       at the end of the day have only 3 distinct resolved periods
        assert(
            periods ==
                    setOf(
                        Pair(
                            start(2018, Month.DECEMBER, 31, zone),
                            end(2019, Month.JANUARY, 1, zone)
                        )
                            .notedResolvedPeriod(),
                        Pair(
                            start(2019, Month.DECEMBER, 31, zone),
                            end(2020, Month.JANUARY, 1, zone)
                        )
                            .notedResolvedPeriod(),
                        Pair(
                            start(2020, Month.DECEMBER, 31, zone),
                            end(2021, Month.JANUARY, 1, zone)
                        )
                            .notedResolvedPeriod()
                    )
        )
    }

    private fun start(year: Int, month: Month, day: Int, zone: ZoneId) =
        ZonedDateTime.of(LocalDate.of(year, month, day), startingHour, zone)

    private fun end(year: Int, month: Month, day: Int, zone: ZoneId) =
        ZonedDateTime.of(LocalDate.of(year, month, day), endingHour, zone)

}
