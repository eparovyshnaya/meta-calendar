package ru.gizmo.metacalendar

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

    fun period(from: String, till: String, note: Any? = null) =
            calendar.addPeriod(Period(
                    ParsedDayMark(from).mark(),
                    ParsedDayMark(till).mark(),
                    note))
}