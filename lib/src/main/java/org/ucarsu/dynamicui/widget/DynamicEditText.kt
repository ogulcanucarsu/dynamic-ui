package org.ucarsu.dynamicui.widget

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import org.ucarsu.dynamicui.R
import org.ucarsu.dynamicui.widget.enums.KeyboardType
import org.ucarsu.dynamicui.widget.validator.DefaultValidator

class DynamicEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ValidatableEditText<String>(context, attrs) {
    private var keyboardType: Int? = 0
    private var maxLength: Int? = 50
    private var minLength: Int? = 0
    private var line: Int? = 1
    private var prefix: String = ""
    private var hintMessage: String = ""
    private var minLengthErrorMessage: String = ""
    private var emptyLengthErrorMessage: String = ""

    init {
        obtainData(attrs)
        initView()
    }

    override fun getValue(): String? {
        return text?.toString()
    }

    override fun validate(): Boolean {
        return when {
            getValue().isNullOrEmpty() -> {
                (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                (this.parent.parent as? TextInputLayout)?.error = emptyLengthErrorMessage
                false
            }
            getValue()!!.length < minLength!! -> {
                (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                (this.parent.parent as? TextInputLayout)?.error = minLengthErrorMessage
                false
            }
            else -> {
                (this.parent.parent as? TextInputLayout)?.isErrorEnabled = false
                true
            }
        }
    }

    override fun onDetachedFromWindow() {
        removeTextChangedListener(this)
        super.onDetachedFromWindow()
    }

    private fun obtainData(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.DynamicEditText,
                0,
                0
            )
            maxLength = typedArray.getInt(R.styleable.DynamicEditText_max_length, maxLength!!)
            minLength = typedArray.getInt(R.styleable.DynamicEditText_min_length, minLength!!)
            keyboardType = typedArray.getInt(R.styleable.DynamicEditText_keyboard_type, keyboardType!!)
            line = typedArray.getInt(R.styleable.DynamicEditText_line, line!!)
            prefix = typedArray.getString(R.styleable.DynamicEditText_prefix)!!
            hintMessage = typedArray.getString(R.styleable.DynamicEditText_hint_message)!!
            minLengthErrorMessage = typedArray.getString(R.styleable.DynamicEditText_minLength_errorMessage)!!
            emptyLengthErrorMessage =  typedArray.getString(R.styleable.DynamicEditText_maxLength_errorMessage)!!
        } catch (e: Exception) {
            // ignored
        } finally {
            typedArray?.recycle()
        }
    }

    private fun initView() {
        val maxFilter = InputFilter.LengthFilter(maxLength!!)
        val filterArray = Array<InputFilter>(1) { maxFilter }
        filters = filterArray
        setLines(line!!)
        inputType = when (keyboardType) {
            KeyboardType.NUMERIC.value -> InputType.TYPE_CLASS_NUMBER
            KeyboardType.ALPHA_NUMERIC.value -> InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_CLASS_TEXT
        }
        hint = hintMessage
        withValidators(DefaultValidator(maxLength!!, minLength!!))
    }

    fun setMaxLength(maxLength: Int) {
        this.maxLength = maxLength
        initView()
    }

    fun setMinLength(minLength: Int) {
        this.minLength = minLength
        initView()
    }

    fun setMessageHint(hintMessage: String){
        this.hintMessage = hintMessage
        initView()
    }

    fun setMessageMinLengthError(minLengthMessage: String){
        this.minLengthErrorMessage = minLengthMessage
        initView()
    }

    fun setMessageEmptyLengthError(emptyLengthMessage: String){
        this.emptyLengthErrorMessage = emptyLengthMessage
        initView()
    }

    fun setKeyboardType(keyboardType : Int){
        this.keyboardType = keyboardType
        initView()
    }

    fun setLine(line: Int){
        this.line = line
        initView()
    }
}