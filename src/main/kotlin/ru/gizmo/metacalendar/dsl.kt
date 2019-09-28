package ru.gizmo.metacalendar

import java.util.*

@DslMarker
annotation class MetaCalendarDsl

@Suppress("ClassName")
@MetaCalendarDsl
object calendar {
    operator fun invoke(constructCalendar: ConstructionContext.() -> Unit): MetaCalendar {
        val context = ConstructionContext()
        context.constructCalendar()
        return context.calendar
    }
}

@MetaCalendarDsl
class ConstructionContext {
    val calendar = MetaCalendar()

    fun period(from: String, till: String, note: Any? = null): Unit =
            PeriodContext(from, till, note).let {
                it.period()?.let { period -> calendar.addPeriod(period) }
            }

    @MetaCalendarDsl
    class PeriodContext(private val from: String,
                        private val till: String,
                        private val note: Any?) {
        fun period(): Period? {
            val start = dayMark(from) ?: return null
            val end = dayMark(till) ?: return null
            return Period(start, end, note)
        }
    }
}