package com.example.findmate.ui.create

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.text.style.TypefaceSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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

        btnCreatePost.setOnClickListener {
            val age = inputAge.text.toString()
            //val location = inputLocation.text.toString()
            //val sex = inputSex.text.toString()
            //val text = inputText.text.toString()
            //viewModel.createPost(age, location, sex, text)
        }

        ivBackButton.setOnClickListener { finish() }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CreatePostActivity::class.java)
            context.startActivity(intent)
        }
    }
}