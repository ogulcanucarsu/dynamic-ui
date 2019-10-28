package org.ucarsu.dynamicui.widget.validator

import org.ucarsu.dynamicui.widget.interfaces.Validator


class DefaultValidator(
    private val maxLength: Int,
    private val minLength: Int,
    var errorMsg: String = DEFAULT,
    var successMsg: String = DEFAULT
) : Validator<String> {

    override fun isValid(value: String?): Boolean {
        value?.takeIf { it.isNotEmpty() }?.let {
            if (it.trim().length in minLength..maxLength) return true
        }
        return false
    }

    override fun setError(message: String) {
        errorMsg = message
    }

    override fun setSuccess(message: String) {
        successMsg = message
    }

    override fun getErrorMessage() = errorMsg

    override fun getSuccesMessage() = successMsg

    companion object {
        const val DEFAULT = "DEFAULT"
    }
}