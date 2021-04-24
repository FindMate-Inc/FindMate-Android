package com.example.findmate.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmate.FindMateApplication
import com.example.findmate.R
import com.example.findmate.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainAdapter.OnMoreListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as FindMateApplication).getComponent().inject(this)

        popupInnerContainer.setOnClickListener {
            if (popupInnerContainer.isVisible) {
                popupInnerContainer.isVisible = false
            }
        }

        complain.setOnClickListener {

        }

        val layoutManager = LinearLayoutManager(applicationContext)
        val adapter = MainAdapter(onMoreListener = this)
        posts.adapter = adapter
        posts.layoutManager = layoutManager

        viewModel.loadPosts()
        viewModel.posts.observe(this) {
            adapter.updateList(it)
        }
    }

    override fun onMoreClicked(x: Float, y: Float, id: String) {
        popupContainer.x = x - popupContainer.width
        popupContainer.y = y
        popupInnerContainer.isVisible = true
    }
}