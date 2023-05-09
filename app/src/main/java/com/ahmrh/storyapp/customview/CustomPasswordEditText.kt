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
                if (s.toString().isEmpty()) showEmptyError()
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) showLengthError(8)
                if (s.toString().isEmpty()) showEmptyError()
            }
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length < 8) showLengthError(8)
                if (s.toString().isEmpty()) showEmptyError()
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}