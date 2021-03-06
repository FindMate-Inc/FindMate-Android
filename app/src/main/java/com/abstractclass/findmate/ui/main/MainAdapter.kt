package com.abstractclass.findmate.ui.main

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abstractclass.findmate.R
import com.abstractclass.findmate.asRelativeTime
import com.abstractclass.findmate.getUtcOffsetDateTime
import com.abstractclass.findmate.repositories.posts.Post
import kotlinx.android.synthetic.main.main_post_item.view.*
import kotlinx.coroutines.*
import kotlin.random.Random


class MainAdapter(private val moreMenuListener: MoreMenuListener) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    val items = mutableListOf<PostAdapterItem>()

    private val minimizedFilter = arrayOf(InputFilter.LengthFilter(MIN_TEXT_LENGTH))
    private val maximizedFilter = arrayOf(InputFilter.LengthFilter(MAX_TEXT_LENGTH))

    var openedMenuItemView: View? = null
    var openedItem: PostAdapterItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_post_item, parent, false)
        view.setOnClickListener {
            openedMenuItemView?.isVisible = false
        }
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(coroutineScope: CoroutineScope, newItems: List<Post>) {
        val newPosts = toAdapterItems(newItems)
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                val diffResult = DiffUtil.calculateDiff(PostDiffCallback(items, newPosts))
                withContext(Dispatchers.Main) {
                    items.clear()
                    items.addAll(newPosts)
                    diffResult.dispatchUpdatesTo(this@MainAdapter)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val view = holder.itemView
        val item = items[position]

        view.tvText.text = item.text

        val sexFormatted = if (item.sex == 1) {
            view.context.getString(R.string.sex_m)
        } else if (item.sex == 2) {
            view.context.getString(R.string.sex_w)
        } else "-"

        val descriptionFormatted = "$sexFormatted, ${item.age}"
        view.tvDescription.text = descriptionFormatted

        val timeFormatted = getUtcOffsetDateTime(item.createdDate).asRelativeTime(view.context)
        view.tvTime.text = timeFormatted

        view.tvLocation.text = item.locations.joinToString()

        //TODO: add getItemTypeForThis
        if (item.isReportedNow) {
            setReportedState(view)
        } else {
            setDefaultState(view)
        }

        initMoreMenu(view, item)

        //TODO: fix mist
        /*val onClickListener = if (item.couldMinimize) {
            View.OnClickListener {
                item.isMinimized = !item.isMinimized
                notifyItemChanged(position)
            }
        } else null

        view.tvText.setOnClickListener(onClickListener)

        view.mist.isVisible = item.couldMinimize && item.isMinimized
        view.tvText.filters = if (item.isMinimized) minimizedFilter else maximizedFilter*/
    }

    private fun setReportedState(view: View) {
        view.popupContainer.isVisible = false
        view.content.isVisible = false
        view.tvReportSend.isVisible = true
        view.container.background =
            ContextCompat.getDrawable(view.context, R.drawable.rectangle_unselected)
    }

    private fun setDefaultState(view: View) {
        view.content.isVisible = true
        view.tvReportSend.isVisible = false
    }

    private fun initMoreMenu(view: View, item: PostAdapterItem) {
        view.btnMore.setOnClickListener {
            openedMenuItemView?.isVisible = false
            view.popupContainer.isVisible = true
            openedMenuItemView = view.popupContainer

            openedItem?.isMenuOpened = false
            openedItem = item
            item.isMenuOpened = true
        }

        view.popupContainer.isVisible = item.isMenuOpened

        view.root.setOnClickListener {
            hidePanel()
        }

        view.complain.setOnClickListener {
            moreMenuListener.onReportClicked(item.id)
            item.isReportedNow = true
            item.isMenuOpened = false
            setReportedState(view)
            Toast.makeText(view.context, view.context.getString(R.string.reportSuccess), Toast.LENGTH_SHORT).show()
        }
    }

    fun hidePanel() {
        openedMenuItemView?.popupContainer?.isVisible = false
    }

    interface MoreMenuListener {
        fun onReportClicked(id: String)
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view)

    data class PostAdapterItem(
        val id: String,
        val text: String,
        val locations: List<String>,
        val sex: Int,
        val age: Int,
        val createdDate: Long,
        val couldMinimize: Boolean,
        var isMinimized: Boolean,
        var isMenuOpened: Boolean,
        var isReportedNow: Boolean
    )

    class PostDiffCallback(private val oldPosts: List<PostAdapterItem>, private val newPosts: List<PostAdapterItem>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].id == newPosts[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldPosts.size
        }

        override fun getNewListSize(): Int {
            return newPosts.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldPosts[oldItemPosition]
            val newItem = oldPosts[newItemPosition]
            return (oldItem.isMinimized && newItem.isMinimized)
                    && (oldItem.isMenuOpened && newItem.isMenuOpened)
                    && (oldItem.isReportedNow && newItem.isReportedNow)
        }

    }

    companion object {
        const val MIN_TEXT_LENGTH = 300
        const val MAX_TEXT_LENGTH = 9999
        fun toAdapterItems(posts: List<Post>): List<PostAdapterItem> {
            return posts.map {
                PostAdapterItem(
                    id = it.id,
                    text = it.text,
                    locations = it.locations,
                    sex = it.sex,
                    age = it.age,
                    createdDate = it.createdAt,
                    couldMinimize = it.text.length > MIN_TEXT_LENGTH,
                    isMinimized = true,
                    isMenuOpened = false,
                    isReportedNow = false,
                )
            }
        }
    }
}