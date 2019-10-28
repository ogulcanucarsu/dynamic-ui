package org.ucarsu.dynamicui.widget.validator

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import org.ucarsu.dynamicui.widget.interfaces.Validatable
import org.ucarsu.dynamicui.widget.interfaces.Validator

class ValidatableInputLayout<T>(context: Context?, attrs: AttributeSet?) : TextInputLayout(context, attrs),
    Validatable<T> {

    override fun setOnItemsValidationListener(listener: Validatable.ItemsValidationListener?) {
        editText?.let {
            if (it is Validatable<*>) {
                it.setOnItemsValidationListener(listener)
            }
        }
    }

    override fun validate(): Boolean {
        editText?.let {
            if (it is Validatable<*>) {
                return it.validate()
            }
        }
        return false
    }

    override fun withValidators(vararg validator: Validator<T>) {
        //empty
    }

}