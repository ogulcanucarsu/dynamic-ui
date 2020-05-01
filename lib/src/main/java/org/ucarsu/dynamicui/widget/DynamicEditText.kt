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
    private var defaultKeyboardType: Int = 0
    private var defaultMaxLength: Int = 50
    private var defaultMinLength: Int = 0
    private var defaultLine: Int = 1
    private var defaultPrefix: String = ""
    private var defaultHintMessage: String = ""
    private var defaultMinLengthErrorMessage: String = ""
    private var defaultEmptyLengthErrorMessage: String = ""
    private var defaultOptional = false

    init {
        obtainData(attrs)
        initView()
    }

    override fun getValue(): String? {
        return text?.toString()
    }

    override fun validate(): Boolean {
        if (!defaultOptional) {
            return when {
                getValue().isNullOrEmpty() -> {
                    (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                    (this.parent.parent as? TextInputLayout)?.error = defaultEmptyLengthErrorMessage
                    false
                }
                getValue()!!.length < defaultMinLength -> {
                    (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                    (this.parent.parent as? TextInputLayout)?.error = defaultMinLengthErrorMessage
                    false
                }
                else -> {
                    (this.parent.parent as? TextInputLayout)?.isErrorEnabled = false
                    true
                }
            }
        } else {
            (this.parent.parent as? TextInputLayout)?.isErrorEnabled = false
            return true
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
            defaultMaxLength =
                typedArray.getInt(R.styleable.DynamicEditText_max_length, defaultMaxLength)
            defaultMinLength =
                typedArray.getInt(R.styleable.DynamicEditText_min_length, defaultMinLength)
            defaultKeyboardType =
                typedArray.getInt(R.styleable.DynamicEditText_keyboard_type, defaultKeyboardType)
            defaultLine = typedArray.getInt(R.styleable.DynamicEditText_line, defaultLine)
            defaultPrefix = typedArray.getString(R.styleable.DynamicEditText_prefix)!!
            defaultHintMessage = typedArray.getString(R.styleable.DynamicEditText_hint_message)!!
            defaultMinLengthErrorMessage =
                typedArray.getString(R.styleable.DynamicEditText_minLength_errorMessage)!!
            defaultEmptyLengthErrorMessage =
                typedArray.getString(R.styleable.DynamicEditText_empty_errorMessage)!!
            defaultOptional = typedArray.getBoolean(R.styleable.DynamicEditText_optional, false)
        } catch (e: Exception) {
            // ignored
        } finally {
            typedArray?.recycle()
        }
    }

    private fun initView() {
        val maxFilter = InputFilter.LengthFilter(defaultMaxLength)
        val filterArray = Array<InputFilter>(1) { maxFilter }
        filters = filterArray
        setLines(defaultLine)
        inputType = when (defaultKeyboardType) {
            KeyboardType.NUMERIC.value -> InputType.TYPE_CLASS_NUMBER
            KeyboardType.ALPHA_NUMERIC.value -> InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_CLASS_TEXT
        }
        hint = defaultHintMessage
        withValidators(DefaultValidator(defaultMaxLength, defaultMinLength))
        this.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    (this.parent.parent as? TextInputLayout)?.isErrorEnabled = false
                } else {
                    if (!defaultOptional) {
                        if (this.text != null) {
                            if (this.text.toString().isEmpty()) {
                                (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                                (this.parent.parent as? TextInputLayout)?.error =
                                    defaultEmptyLengthErrorMessage
                            } else if (this.text.toString().length < defaultMinLength) {
                                (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                                (this.parent.parent as? TextInputLayout)?.error =
                                    defaultMinLengthErrorMessage
                            }
                        } else {
                            (this.parent.parent as? TextInputLayout)?.isErrorEnabled = true
                            (this.parent.parent as? TextInputLayout)?.error =
                                defaultEmptyLengthErrorMessage
                        }
                    }
                }
            }
        }
    }

    fun setMaxLength(maxLength: Int) {
        this.defaultMaxLength = maxLength
        initView()
    }

    fun setMinLength(minLength: Int) {
        this.defaultMinLength = minLength
        initView()
    }

    fun setMessageHint(hintMessage: String) {
        this.defaultHintMessage = hintMessage
        initView()
    }

    fun setMessageMinLengthError(minLengthMessage: String) {
        this.defaultMinLengthErrorMessage = minLengthMessage
        initView()
    }

    fun setMessageEmptyLengthError(emptyLengthMessage: String) {
        this.defaultEmptyLengthErrorMessage = emptyLengthMessage
        initView()
    }

    fun setKeyboardType(keyboardType: Int) {
        this.defaultKeyboardType = keyboardType
        initView()
    }

    fun setLine(line: Int) {
        this.defaultLine = line
        initView()
    }

    fun setOptional(optional: Boolean) {
        this.defaultOptional = optional
        initView()
    }
}