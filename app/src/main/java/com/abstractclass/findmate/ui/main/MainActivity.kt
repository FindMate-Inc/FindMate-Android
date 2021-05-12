package com.abstractclass.findmate.ui.main

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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

        viewModel.androidId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        initToolbar()
        initPosts()

        btnCreatePost.setOnClickListener { CreatePostActivity.start(this) }
        viewModel.screenState.observe(this) {
            when (it) {
                MainViewModel.States.DEFAULT -> showDefaultState()
                MainViewModel.States.LOADING -> showLoadingState()
                MainViewModel.States.NO_POSTS -> showInfoState()
                MainViewModel.States.ERROR -> showErrorState()
                else -> showDefaultState()
            }
        }

        btnFailedButtonReload.setOnClickListener {
            btnFailedButtonReload.isEnabled = false
            viewModel.loadNewPosts()
        }
    }

    private fun initPosts() {
        var layoutManager = LinearLayoutManager(applicationContext)
        val adapter = MainAdapter(object : MainAdapter.MoreMenuListener {
            override fun onReportClicked(id: String) {
                viewModel.reportPost(id)
            }
        })
        posts.adapter = adapter
        posts.layoutManager = layoutManager

        postsSwipeContainer.setOnRefreshListener {
            viewModel.screenState.value = MainViewModel.States.LOADING
            viewModel.loadNewPosts()
        }

        posts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.loadMorePosts(layoutManager?.findLastVisibleItemPosition() ?: 0)
            }
        })

        viewModel.screenState.value = MainViewModel.States.LOADING
        viewModel.loadNewPosts()
        viewModel.posts.observe(this) {
            adapter.updateList(it)
        }
    }

    private fun initToolbar() {
        filterContainer.setOnClickListener {
            showSearch(true)
        }

        locationFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchLocation.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        locationFilter.setBackListener(object : SearchLocationEditText.BackActionListener {
            override fun onBack() {
                showSearch(false)
            }
        })

        locationFilter.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showSearch(false)
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
        ivBackButton.setOnClickListener {
            showSearch(false)
        }
        ivClear.setOnClickListener { viewModel.clearFilter() }
    }

    override fun onBackPressed() {
        if (locationFilter.isVisible) {
            showSearch(false)
        } else {
            super.onBackPressed()
        }
    }

    fun showSearch(show: Boolean) {
        locationFilter.isVisible = show
        ivBackButton.isVisible = show
        ivClear.isVisible = show
        ivSearch.isVisible = !show
        toolbarTitle.isVisible = !show
        tvFilterText.isVisible = !show

        if (show) {
            locationFilter.requestFocus()
            keyBoardManager?.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        } else {
            locationFilter.clearFocus()
            keyBoardManager?.hideSoftInputFromWindow(root.windowToken, 0)
        }
    }

    private fun showDefaultState() {
        tvFailed.isVisible = false
        ivFailed.isVisible = false
        btnFailedButtonReload.isVisible = false
        postsSwipeContainer.isRefreshing = false
        postsSwipeContainer.isVisible = true
        showSearch(false)
    }

    private fun showLoadingState() {
        postsSwipeContainer.isRefreshing = true
        postsSwipeContainer.isVisible = true
        showSearch(false)
    }

    private fun showErrorState() {
        postsSwipeContainer.isRefreshing = false
        postsSwipeContainer.isVisible = false

        tvFailed.text = getString(R.string.mainFailedError)
        tvFailed.isVisible = true
        ivFailed.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_error, null))
        ivFailed.isVisible = true
        btnFailedButtonReload.isEnabled = true
        btnFailedButtonReload.isVisible = true
        showSearch(false)
    }

    private fun showInfoState() {
        postsSwipeContainer.isRefreshing = false
        postsSwipeContainer.isVisible = false
        tvFailed.text = getString(R.string.mainFailedNoElements)
        tvFailed.isVisible = true
        ivFailed.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_warning, null))
        ivFailed.isVisible = true
        btnFailedButtonReload.isVisible = false
    }
}