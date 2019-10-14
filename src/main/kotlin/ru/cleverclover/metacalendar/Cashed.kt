package ru.cleverclover.metacalendar

class Cashed<S, T>(private val source: S, private val retrieve: (S) -> T) {
    private val value = mutableListOf<T>()

    fun get(): T {
        if (value.isEmpty()) {
            value.add(retrieve(source))
        }
        return value[0]
    }
}