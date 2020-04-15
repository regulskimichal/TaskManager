@file:Suppress("UNCHECKED_CAST", "UNUSED")

package pl.michalregulski.taskmanager.utils

import androidx.databinding.Observable


open class ObservableField<T> : androidx.databinding.ObservableField<T> {

    constructor()

    constructor(value: T) : super(value)

    constructor(vararg dependencies: Observable) : super(*dependencies)

    override fun get(): T = super.get() as T

}

inline fun <T> ObservableField<T>.update(transform: T.() -> Unit) {
    val value = get()
    if (value != null) {
        transform(value)
        notifyChange()
    }
}

inline fun <T> ObservableField<T>.addOnPropertyChanged(crossinline callback: (T) -> Unit) {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, i: Int) {
            if (observable is ObservableField<*>) {
                callback(observable.get() as T)
            }
        }
    })
}

fun ObservableBoolean.negate() = set(get().not())

typealias ObservableBoolean = ObservableField<Boolean>

typealias ObservableChar = ObservableField<Char>

typealias ObservableByte = ObservableField<Byte>

typealias ObservableShort = ObservableField<Short>

typealias ObservableInt = ObservableField<Int>

typealias ObservableLong = ObservableField<Long>

typealias ObservableFloat = ObservableField<Float>

typealias ObservableDouble = ObservableField<Double>

typealias ObservableString = ObservableField<String>
