package org.ucarsu.customedittext.ui

import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_dynamic_screen.*
import org.ucarsu.customedittext.R
import java.io.InputStream
import com.google.gson.Gson
import org.ucarsu.dynamicui.widget.DynamicEditText
import org.ucarsu.dynamicui.widget.extensions.hideKeyboard

class DynamicScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_screen)

        val jsonString = readJSONFromAsset()
        if (!jsonString.isNullOrEmpty()) {
            val gsonData = Gson()
            val responseData = gsonData.fromJson(jsonString, Response::class.java)

            responseData?.information?.forEach {
                val view =
                    LayoutInflater.from(applicationContext).inflate(R.layout.row_info_cell, null)
                val information: TextView = view.findViewById(R.id.textView_information)
                information.text = it.text
                val image: ImageView = view.findViewById(R.id.imageView_info)
                when (it.type) {
                    0 -> {
                        image.background = resources.getDrawable(R.drawable.ic_fail)
                    }
                    1 -> {
                        image.background = resources.getDrawable(R.drawable.ic_info)
                    }
                }
                linearLayoutInformation.addView(view)
            }

            val viewList = arrayListOf<View>()

            val textInputLayout = TextInputLayout(this)
            textInputLayout.apply {
                boxStrokeColor = Color.parseColor(responseData?.textInputLayouts?.boxStrokeColor)
                when (responseData?.textInputLayouts?.backgroundMode) {
                    2 -> {
                        setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE)
                    }
                    else -> {
                        //no-op
                    }
                }
                when (responseData?.textInputLayouts?.textAppearance) {
                    1 -> {
                        setHintTextAppearance(R.style.ValidatableInputLayoutStyle_OutlineBox_HintInputLayoutStyle)
                    }
                    else -> {
                        //no-op
                    }
                }
                setBoxCornerRadii(
                    responseData?.textInputLayouts?.cornerRadii!!.toFloat(),
                    responseData.textInputLayouts.cornerRadii.toFloat(),
                    responseData.textInputLayouts.cornerRadii.toFloat(),
                    responseData.textInputLayouts.cornerRadii.toFloat()
                )

                setPadding(
                    responseData.textInputLayouts.leftPadding,
                    responseData.textInputLayouts.topPadding,
                    responseData.textInputLayouts.rightPadding,
                    responseData.textInputLayouts.bottomPadding
                )
            }
            mainFormWidget.addView(textInputLayout)
            viewList.add(textInputLayout)

            val dynamicEditText = DynamicEditText(this)
            dynamicEditText.apply {
                setMessageHint(responseData?.dynamicEditTexts!!.hintMessage)
                setMessageEmptyLengthError(responseData.dynamicEditTexts.emptyErrorMessage)
                setMessageMinLengthError("Please write min. ${responseData.dynamicEditTexts.minLength} character")
                setKeyboardType(responseData.dynamicEditTexts.keyboardType)
                setLine(responseData.dynamicEditTexts.line)
                setMinLength(responseData.dynamicEditTexts.minLength)
                setMaxLength(responseData.dynamicEditTexts.maxLength)
            }
            textInputLayout.addView(dynamicEditText)
            viewList.add(dynamicEditText)

            val button = Button(this)
            button.apply {
                height = responseData.button.height
                width = if (responseData.button.width > 100) {
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else {
                    responseData.button.width
                }
                text = responseData.button.text
                setTextColor(Color.parseColor(responseData.button.textColor))
                when (responseData.button.textSize) {
                    8 -> {
                        textSize =
                            resources.getDimension(R.dimen.font_xsmall) / resources.displayMetrics.scaledDensity
                    }
                    10 -> {
                        textSize =
                            resources.getDimension(R.dimen.font_small) / resources.displayMetrics.scaledDensity
                    }
                    12 -> {
                        textSize =
                            resources.getDimension(R.dimen.font_medium) / resources.displayMetrics.scaledDensity
                    }
                    14 -> {
                        textSize =
                            resources.getDimension(R.dimen.font_large) / resources.displayMetrics.scaledDensity
                    }
                    16 -> {
                        textSize =
                            resources.getDimension(R.dimen.font_xlarge) / resources.displayMetrics.scaledDensity

                    }
                }
                when (responseData.button.buttonBackground) {
                    1 -> {
                        background = context?.resources?.getDrawable(R.drawable.button_background)
                    }
                    else -> {
                        //no-op
                    }
                }

            }
            mainLinearLayout.addView(button)
            val params = button.layoutParams as LinearLayout.LayoutParams
            params.setMargins(
                responseData.button.margin,
                responseData.button.margin,
                responseData.button.margin,
                responseData.button.margin
            )
            button.layoutParams = params

            button.setOnClickListener {
                viewList.forEach {
                    when (it) {
                        is DynamicEditText -> {
                            if (it.validate()) {
                                //if UI element's is validate, call service, cache or local data source
                            }
                        }
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    v.hideKeyboard()

                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun readJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.dynamic)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}

