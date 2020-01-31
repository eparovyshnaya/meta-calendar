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
package ru.cleverclover.metacalendar

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Aggregates result of a [MetaCalendar] resolution to a set of years in a particular time zone.
 *
 * Does not perform actual resolution until resolved periods are queried.
 * */
class ResolvedCalendar(meta: MetaCalendar, val years: Set<Int>, val zone: ZoneId) {
    private val periods = Cashed(meta) { meta ->
        years.map { year ->
            meta.periods().asSequence()
                    .map { it.resolve(year, zone) }
                    .flatMap { it.asSequence() }
                    .toSet()
        }
                .flatten()
                .toSet()

    }

    constructor(meta: MetaCalendar, year: Int, zone: ZoneId) : this(meta, setOf(year), zone)

    /**
     * Set of unique resolved periods
     * */
    fun periods() = periods.get()
}

/**
 * Represents a resolution result for a [Period]
 * */
data class NotedResolvedPeriod(val from: ZonedDateTime, val to: ZonedDateTime, val note: Any? = null) {
    init {
        require(from <= to) { "Resolved period validation error: from-mark must be less or equal than to-mark" }
    }

    /**
     * Time bounds of the resolved period
     * */
    fun bounds() = Pair(from, to)
}


internal fun Pair<ZonedDateTime, ZonedDateTime>.notedResolvedPeriod(note: Any? = null) =
        NotedResolvedPeriod(this.first, this.second, note)
