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

/**
 * In-here specific [scope-controlling annotation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-dsl-marker/index.html)
 * */
@DslMarker
annotation class MetaCalendarDsl

/**
 * Use unconventionally-named object for the sake of it's
 * [invoke operator](https://kotlinlang.org/docs/reference/operator-overloading.html#invoke).
 *
 * As it has the only closure argument, then we can use highly readable form
 *
 * ```
 * calendar {
 *    // init code here
 * }
 * ```
 * */
@Suppress("ClassName")
@MetaCalendarDsl
object calendar {

    /**
     * New [ConstructionContext] is created to be used as the [constructCalendar]'s
     * [receiver](https://kotlinlang.org/docs/reference/lambdas.html#function-literals-with-receiver).
     *
     * Returns [MetaCalendar] instance constructed during the [constructCalendar] work.
     * */
    operator fun invoke(constructCalendar: ConstructionContext.() -> Unit): MetaCalendar {
        val context = ConstructionContext()
        context.constructCalendar()
        return context.calendar
    }

}

/**
 * Internal calendar's construction context.
 * Creates and hosts an instance of [MetaCalendar] and exposes it's methods to fill it with data.
 *
 * Use it like
 * ```
 * calendar {
 *      period(
 *          from = "12 декабря",
 *          till = "25 декабря",
 *          note = "Йоль в Исландии"
 *      )
 * }
 * ```
 * */
@MetaCalendarDsl
class ConstructionContext {

    val calendar = MetaCalendar()

    /**
     * Append new period to a [MetaCalendar] under construction.
     *
     * From-natural-language parsing is used to recognize
     * */
    fun period(from: String, till: String, note: Any? = null) =
        PeriodFromBoundDefinitions(from, till).bounds().let {
            calendar.add(it.period(note))
        }

    fun period(range: String, note: Any? = null) =
        PeriodFromRangeDefinition(range).bounds().let {
            calendar.add(it.period(note))
        }

}
