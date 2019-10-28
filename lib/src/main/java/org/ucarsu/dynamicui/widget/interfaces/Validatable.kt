package org.ucarsu.dynamicui.widget.interfaces


interface Validatable<T> {
    fun validate() : Boolean
    fun withValidators(vararg  validator: Validator<T>)
    fun setOnItemsValidationListener(listener: ItemsValidationListener?)

    interface ItemsValidationListener {
        fun checkItemsValidation()
    }
}