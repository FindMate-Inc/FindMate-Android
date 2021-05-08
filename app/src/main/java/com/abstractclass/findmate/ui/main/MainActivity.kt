package com.abstractclass.findmate.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abstractclass.findmate.FindMateApplication
import com.abstractclass.findmate.R
import com.abstractclass.findmate.ViewModelFactory
import com.abstractclass.findmate.ui.addons.SearchLocationEditText
import com.abstractclass.findmate.ui.create.CreatePostActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private var keyBoardManager: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as FindMateApplication).getComponent().inject(this)
        keyBoardManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        initToolbar()
        initPosts()

        viewModel.screenState.observe(this) {
            when (it) {
                MainViewModel.States.DEFAULT -> showDefaultState()
                MainViewModel.States.SEARCH -> showSearchState()
                MainViewModel.States.LOADING -> showLoadingState()
                else -> showDefaultState()
            }
        }

        btnCreatePost.setOnClickListener { CreatePostActivity.start(this) }
    }
    var layoutManager: LinearLayoutManager? = null

    private fun initPosts() {
        layoutManager = LinearLayoutManager(applicationContext)
        val adapter = MainAdapter(object : MainAdapter.OnMoreListener {
            override fun onMoreClicked(x: Float, y: Float, id: String) {
                popupContainer.x = x - popupContainer.width
                popupContainer.y = y
                popupInnerContainer.isVisible = true
            }
        })
        posts.adapter = adapter
        posts.layoutManager = layoutManager

        postsSwipeContainer.setOnRefreshListener {
            viewModel.loadNewPosts()
        }

        posts.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.loadMorePosts(layoutManager?.findLastVisibleItemPosition() ?: 0)
            }
        })


        popupInnerContainer.setOnClickListener {
            if (popupInnerContainer.isVisible) {
                popupInnerContainer.isVisible = false
            }
        }

        viewModel.loadNewPosts()
        viewModel.posts.observe(this) {
            adapter.updateList(it)
        }
    }

    private fun initToolbar() {
        filterContainer.setOnClickListener { viewModel.screenState.value = MainViewModel.States.SEARCH }

        locationFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchLocation.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        locationFilter.setBackListener(object : SearchLocationEditText.BackActionListener {
            override fun onBack() {
                viewModel.screenState.value = MainViewModel.States.DEFAULT
            }
        })

        locationFilter.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.loadNewPosts()
                    return true
                }
                return false
            }
        })

        viewModel.searchLocation.observe(this) {
            if (it.isNullOrBlank()) {
                tvFilterText.text = getString(R.string.mainFilterLabel)
                locationFilter.text?.clear()
            } else {
                tvFilterText.text = it
            }
        }
        ivBackButton.setOnClickListener { viewModel.screenState.value = MainViewModel.States.DEFAULT }
        ivClear.setOnClickListener { viewModel.clearFilter()}
    }

    override fun onBackPressed() {
        if (locationFilter.isVisible) {
            viewModel.screenState.value = MainViewModel.States.DEFAULT
        } else {
            super.onBackPressed()
        }
    }

    private fun showSearchState() {
        postsSwipeContainer.isRefreshing = false
        locationFilter.isVisible = true
        ivBackButton.isVisible = true
        ivSearch.isVisible = false
        ivClear.isVisible = true
        toolbarTitle.isVisible = false
        tvFilterText.isVisible = false
        locationFilter.requestFocus()
        keyBoardManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun showDefaultState() {
        postsSwipeContainer.isRefreshing = false
        locationFilter.isVisible = false
        ivBackButton.isVisible = false
        toolbarTitle.isVisible = true
        ivSearch.isVisible = true
        ivClear.isVisible = false
        tvFilterText.isVisible = true
        locationFilter.clearFocus()
        keyBoardManager?.hideSoftInputFromWindow(root.windowToken, 0)
    }

    private fun showLoadingState() {
        postsSwipeContainer.isRefreshing = true
        locationFilter.isVisible = false
        ivBackButton.isVisible = false
        toolbarTitle.isVisible = true
        ivSearch.isVisible = true
        ivClear.isVisible = false
        tvFilterText.isVisible = true
        locationFilter.clearFocus()
        keyBoardManager?.hideSoftInputFromWindow(root.windowToken, 0)
    }
}