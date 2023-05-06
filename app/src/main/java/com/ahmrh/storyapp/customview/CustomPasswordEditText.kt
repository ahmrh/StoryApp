package com.ahmrh.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ahmrh.storyapp.R

class CustomPasswordEditText: AppCompatEditText{
    private lateinit var clearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.
            }
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length < 8) showLengthError(8)
                if (s.toString().isEmpty()) showEmptyError()
                // Do nothing.
            }
        })
    }

    private fun showEmptyError() {
        error = "Field must not be Empty"
        requestFocus()
    }
    private fun showLengthError(size: Int?) {
        error = "Password length must be at least $size characters long"
        requestFocus()
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        setCompoundDrawables(null, null, icon, null)
        super.setError(error, icon)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

//    override fun onTouch(v: View?, event: MotionEvent): Boolean {
//        if (compoundDrawables[2] != null) {
//            val clearButtonStart: Float
//            val clearButtonEnd: Float
//            var isClearButtonClicked = false
//            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
//                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
//                when {
//                    event.x < clearButtonEnd -> isClearButtonClicked = true
//                }
//            } else {
//                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
//                when {
//                    event.x > clearButtonStart -> isClearButtonClicked = true
//                }
//            }
//            if (isClearButtonClicked) {
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
//                        showClearButton()
//                        return true
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_close_24) as Drawable
//                        when {
//                            text != null -> text?.clear()
//                        }
//                        hideClearButton()
//                        return true
//                    }
//                    else -> return false
//                }
//            } else return false
//        }
//        return false
//    }
}