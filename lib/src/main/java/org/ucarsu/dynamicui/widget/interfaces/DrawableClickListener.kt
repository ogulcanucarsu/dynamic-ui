package org.ucarsu.dynamicui.widget.interfaces

import org.ucarsu.dynamicui.widget.ValidatableEditText

interface DrawableClickListener {
    fun onDrawableClick(@ValidatableEditText.Companion.DrawablePosition drawablePos: Int)
}