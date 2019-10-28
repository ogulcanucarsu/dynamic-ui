package org.ucarsu.dynamicui.widget


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.view.MotionEvent
import androidx.annotation.IntDef
import org.ucarsu.dynamicui.widget.interfaces.DrawableClickListener
import org.ucarsu.dynamicui.widget.interfaces.Validatable
import org.ucarsu.dynamicui.widget.interfaces.Validator
import org.ucarsu.dynamicui.widget.validator.ValidatorBuilder

abstract class ValidatableEditText<T> constructor(context: Context, attrs: AttributeSet?) :
    EditText(context, attrs), Validatable<T>, View.OnFocusChangeListener, TextWatcher {

    private val validators: MutableList<Validator<T>> by lazy {
        ArrayList<Validator<T>>()
    }
    var itemsValidationListener: Validatable.ItemsValidationListener? = null

    init {
        addTextChangedListener(this)
        onFocusChangeListener = this
    }

    final override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        editTextBeforeTextChanged(s, start, count, after)
    }

    final override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        itemsValidationListener?.checkItemsValidation()
        editTextTextChanged(s, start, before, count)
    }

    final override fun afterTextChanged(s: Editable?) {
        editTextAfterTextChanged(s)
    }

    final override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validate()
        }
        focusChanged(v, hasFocus)
    }

    override fun validate(): Boolean {
        var isValid = true

        validators.takeIf { it.isNotEmpty() }?.let { list ->
            list.forEach { validator ->
                val validBuilder =
                    ValidatorBuilder(getValue()!!)
                isValid = validBuilder
                    .addRule(validator)
                    .check()
            }
        }
        return isValid
    }

    override fun withValidators(vararg validator: Validator<T>) {
        if (validators.isNotEmpty()) {
            validators.clear()
        }
        validators.addAll(validator)
    }

    override fun setOnItemsValidationListener(listener: Validatable.ItemsValidationListener?) {
        itemsValidationListener = listener
    }

    override fun onDetachedFromWindow() {
        removeTextChangedListener(this)
        onFocusChangeListener = null
        super.onDetachedFromWindow()
    }

    fun setDrawableClickListener(listener: DrawableClickListener?, @DrawablePosition drawablePos: Int = DRAWABLE_RIGHT) {
        setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && compoundDrawables[drawablePos] != null) {
                if (event.rawX >= right - compoundDrawables[drawablePos].bounds.width()) {
                    listener?.onDrawableClick(drawablePos)
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    fun handleTextLength(text: String?, length: Int): String {
        text?.takeIf { it.isNotEmpty() }?.let {
            return if (it.length > length) {
                it.substring(0, length)
            } else it
        } ?: return ""
    }


    abstract fun getValue(): T?

    open fun editTextTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    open fun editTextBeforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    open fun editTextAfterTextChanged(s: Editable?) {}
    open fun focusChanged(v: View?, hasFocus: Boolean) {}

    companion object {
        const val DRAWABLE_LEFT = 0
        const val DRAWABLE_TOP = 1
        const val DRAWABLE_RIGHT = 2
        const val DRAWABLE_BOTTOM = 3

        @IntDef(DRAWABLE_LEFT, DRAWABLE_TOP, DRAWABLE_RIGHT, DRAWABLE_BOTTOM)
        annotation class DrawablePosition
    }
}