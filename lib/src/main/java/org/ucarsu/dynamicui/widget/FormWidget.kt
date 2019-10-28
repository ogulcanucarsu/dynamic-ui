package org.ucarsu.dynamicui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import org.ucarsu.dynamicui.widget.interfaces.Form
import org.ucarsu.dynamicui.widget.interfaces.Validatable

class FormWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Form, View.OnLayoutChangeListener, Validatable.ItemsValidationListener {

    private var formValidationListener: ValidationListener? = null
    private var form: Form = DefaultForm()
    private val childViews: MutableList<View> by lazy { ArrayList<View>() }

    init {
        addOnLayoutChangeListener(this)
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is Validatable<*> && !childViews.contains(view)) {
                if (view.visibility == View.VISIBLE) {
                    childViews.add(view)
                    view.setOnItemsValidationListener(this)
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        removeOnLayoutChangeListener(this)
        super.onDetachedFromWindow()
    }

    override fun isValid(): Boolean {
        return form.isValid()
    }

    fun clearAllFocus() = childViews.forEach { it.clearFocus() }

    override fun checkItemsValidation() {
        formValidationListener?.onAllItemsValid(form.isValid())
    }

    fun setValidator(form: Form) {
        this.form = form
    }

    fun setValidationListener(listener: ValidationListener) {
        formValidationListener = listener
    }

    inner class DefaultForm : Form {
        override fun isValid(): Boolean {
            for (view in childViews) {
                if ((view is Validatable<*>) && view.visibility == View.VISIBLE) {
                    if (!view.validate()) {
                        return false
                    }
                }
            }
            return true
        }
    }

    interface ValidationListener {
        fun onAllItemsValid(isValid: Boolean)
    }
}