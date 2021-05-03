package com.example.findmate.ui.create

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import com.example.findmate.FindMateApplication
import com.example.findmate.R
import com.example.findmate.ViewModelFactory
import kotlinx.android.synthetic.main.activity_create_post.*
import javax.inject.Inject


class CreatePostActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<CreatePostViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        (application as FindMateApplication).getComponent().inject(this)

        inputAge.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.age.value = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        inputAge.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputAgeContainer.isHelperTextEnabled = true
                inputAgeContainer.helperText = getString(R.string.createPostAgeHelper)
            } else inputAgeContainer.isHelperTextEnabled = false
        }

        inputSex.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.sex.value = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        inputSex.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputSexContainer.isHelperTextEnabled = true
                inputSexContainer.helperText = getString(R.string.createPostSexHelper)
            } else inputSexContainer.isHelperTextEnabled = false
        }

        inputLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.location.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        inputLocation.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputLocationContainer.isHelperTextEnabled = true
                inputLocationContainer.helperText = getString(R.string.createPostLocationHelper)
            } else inputLocationContainer.isHelperTextEnabled = false
        }

        inputText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.text.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.textError.observe(this) {
            if (it) {
                inputTextContainer.helperText = getString(R.string.createPostTextError)
                inputTextContainer.setHelperTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this,
                            R.color.pomegranate
                        )
                    )
                )
                inputText.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.selector_edit_text_rectangle_red,
                    null
                )
            } else {
                inputTextContainer.helperText = getString(R.string.createPostTextHelper)
                inputTextContainer.setHelperTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this,
                            R.color.black_38
                        )
                    )
                )
                inputText.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.selector_edit_text_rectangle,
                    null
                )
            }
        }

        btnCreatePost.setOnClickListener { viewModel.createPost() }
        ivBackButton.setOnClickListener { finish() }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CreatePostActivity::class.java)
            context.startActivity(intent)
        }
    }
}