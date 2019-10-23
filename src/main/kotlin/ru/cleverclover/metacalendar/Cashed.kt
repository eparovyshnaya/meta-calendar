package ru.cleverclover.metacalendar

/**
 * Orthodox OOP construction.
 *
 * Seat of mutability caused by
 *  - prohibition of any calculation in a ctor and
 *  - cashing
 *
 * Typical use case:
 *  - you have a final field which, of course, must be initialized immediately in ctor
 *  - you follow OOOP rule for light-weight ctor: no calculations, period
 *
 * Hence, you init a field with
 *  - a source of data and
 *  - a memorized way of retrieving the value from this source
 *
 * The actual calculation is performed lazily, at the first [get] call.
 *
 * Then the result is cashed (forever) and all further [get]s do not cause any calculation.
 *
 * Sample:
 * 1: final field initialization
 * ```
 *     private val categories = Cashed(source) { array -> array.map { Category(it as JSONObject) } }
 * ```
 *
 * 2: value retrieval
 * ```
 * fun byId(id: String) = categories.get().find { it.id() == id }
 * fun all() = categories.get()
 * ```
 * */
class Cashed<S, T>(private val source: S, private val retrieve: (S) -> T) {
    private val value = mutableListOf<T>()

    fun get(): T {
        if (value.isEmpty()) {
            value.add(retrieve(source))
        }
        return value[0]
    }
}