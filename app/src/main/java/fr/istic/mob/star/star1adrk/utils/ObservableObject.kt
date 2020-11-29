package fr.istic.mob.star.star1adrk.utils

import java.util.*

open class ObservableObject private constructor() : Observable() {

    fun updateValue(data: Any?) {
        synchronized(this) {
            setChanged()
            notifyObservers(data)
        }
    }

    companion object {
        val instance = ObservableObject()
    }
}