package org.ucarsu.dynamicui.widget.interfaces


interface Validator<T> {

    fun isValid(value: T?): Boolean

    fun setError(message: String)

    fun getErrorMessage(): String

    fun setSuccess(message: String)

    fun getSuccesMessage(): String
}