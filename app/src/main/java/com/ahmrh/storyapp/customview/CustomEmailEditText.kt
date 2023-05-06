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

class CustomEmailEditText: AppCompatEditText{
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


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
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.
            }
            override fun afterTextChanged(s: Editable) {
                if (! s.toString().trim().matches(emailPattern.toRegex())) showEmailError()
                if (s.toString().isEmpty()) showEmptyError()
                // Do nothing.
            }
        })
    }

    private fun showEmptyError() {
        error = "Field must not be Empty"
        requestFocus()
    }
    private fun showEmailError() {
        error = "Input must be Email"
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

}