package com.example.findmate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.findmate.FindMateApplication
import com.example.findmate.R
import com.example.findmate.ViewModelFactory
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainViewModel> {viewModelFactory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as FindMateApplication).getComponent().inject(this)
        Log.d("TestPish", "viewModel ${viewModel.check()}")
    }
}