package com.example.findmate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmate.FindMateApplication
import com.example.findmate.R
import com.example.findmate.ViewModelFactory
import com.example.findmate.ui.create.CreatePostActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainViewModel> {viewModelFactory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as FindMateApplication).getComponent().inject(this)

        val layoutManager = LinearLayoutManager(applicationContext)
        val adapter = MainAdapter()
        posts.adapter = adapter
        posts.layoutManager = layoutManager
/*
        btnCreatePost.setOnClickListener {
            CreatePostActivity.start(this)
        }*/

        viewModel.loadPosts()
        viewModel.posts.observe(this) {
            adapter.updateList(it)
        }

        registerForContextMenu(btnCreatePost)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        Log.d("TestPish", "test")
        menu!!.setHeaderTitle("Context Menu");
        menu.add(0, v?.getId() ?: 0, 0, "Upload");
        menu.add(0, v?.getId() ?:0, 0, "Search");
        menu.add(0, v?.getId()?:0, 0, "Share");
        menu.add(0, v?.getId()?: 0, 0, "Bookmark");
    }
}