package org.ucarsu.dynamicui.widget.validator

import org.ucarsu.dynamicui.widget.interfaces.Validator

@Suppress("UNCHECKED_CAST")
class ValidatorBuilder<T>(private val value: T) {

    companion object {
        const val ERROR_MESSAGE = "ERROR_MESSAGE"
        const val SUCCES_MESSAGE = "SUCCESS_MESSAGE"
    }

    private var isValid = true

    private var errorMessage =
        ERROR_MESSAGE

    private var successMessage =
        SUCCES_MESSAGE

    private var errorCallback: ((message: String) -> Unit)? = null

    private var successCallback: ((message: String) -> Unit)? = null

    private var ruleList = arrayListOf<Validator<T>>()

    fun check(): Boolean {
        for (rule in ruleList) {
            if (!rule.isValid(value)) {
                setError(rule.getErrorMessage())
                errorCallback?.invoke(errorMessage)
            } else {
                setSuccess(rule.getSuccesMessage())
                successCallback?.invoke(successMessage)
            }
        }
        return isValid
    }

    fun setError(message: String) {
        isValid = false
        errorMessage = message
    }

    fun setSuccess(message: String) {
        successMessage = message
    }

    fun errorCallback(callback: (message: String) -> Unit) = apply {
        errorCallback = callback
    }

    fun successCallback(callback: (message: String) -> Unit) = apply {
        successCallback = callback
    }

    fun addRule(rule: Validator<T>) = apply {
        ruleList.add(rule)
    }

}