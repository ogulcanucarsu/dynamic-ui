package org.ucarsu.customedittext.ui

data class Response(
    val information: List<Information>,
    val dynamicEditTextInput: List<EditTextInput>,
    val button: Button
)

data class Information(
    val type: Int,
    val text: String
)

data class EditTextInput(
    val textInputLayouts: TextInputLayouts,
    val dynamicEditText: DynamicEditTexts
)

data class TextInputLayouts(
    val boxStrokeColor: String,
    val backgroundMode: Int,
    val textAppearance: Int,
    val cornerRadii: Int,
    val leftPadding: Int,
    val topPadding: Int,
    val rightPadding: Int,
    val bottomPadding: Int
)

data class DynamicEditTexts(
    val hintMessage: String?,
    val emptyErrorMessage: String?,
    val keyboardType: Int,
    val line: Int,
    val minLength: Int?,
    val maxLength: Int?,
    val optional: Boolean? = false
)

data class Button(
  val height: Int,
  val width: Int,
  val text: String,
  val textColor: String,
  val textSize: Int,
  val buttonBackground: Int,
  val margin: Int
)