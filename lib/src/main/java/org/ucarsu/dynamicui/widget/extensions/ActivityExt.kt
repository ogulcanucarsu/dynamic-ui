package org.ucarsu.dynamicui.widget.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT

/*
 * Shows Keyboard
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.toggleSoftInput(SHOW_IMPLICIT, HIDE_IMPLICIT_ONLY)
}

/*
 * Hides Keyboard
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
