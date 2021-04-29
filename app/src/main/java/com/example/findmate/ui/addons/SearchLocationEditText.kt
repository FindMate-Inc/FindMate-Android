package com.example.findmate.ui.addons

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

public class SearchLocationEditText(context: Context, attrs: AttributeSet?): AppCompatEditText(context, attrs) {
    private var backActionListener: BackActionListener? = null

    fun setBackListener(listener: BackActionListener) {
        backActionListener = listener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backActionListener?.onBack()
        }
        return false
    }

    interface BackActionListener {
        fun onBack()
    }
}