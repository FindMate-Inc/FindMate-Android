package com.abstractclass.findmate.ui.main

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.abstractclass.findmate.R
import com.abstractclass.findmate.asRelativeTime
import com.abstractclass.findmate.getUtcOffsetDateTime
import com.abstractclass.findmate.repositories.posts.Post
import kotlinx.android.synthetic.main.main_post_item.view.*
import kotlin.random.Random


class MainAdapter(private val moreMenuListener: MoreMenuListener) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    val items = mutableListOf<PostAdapterItem>()

    private val minimizedFilter = arrayOf(InputFilter.LengthFilter(MIN_TEXT_LENGTH))
    private val maximizedFilter = arrayOf(InputFilter.LengthFilter(MAX_TEXT_LENGTH))
    var openedMenuItemView: View? = null

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

    fun updateList(newItems: List<Post>) {
        items.clear()
        items.addAll(toAdapterItems(newItems))
        notifyDataSetChanged()
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

        val random = Random.nextInt(0, 100)
        if (random > 60) {
            view.container.background =
                ContextCompat.getDrawable(view.context, R.drawable.drawable_post_background_orange)
        } else {
            view.container.background =
                ContextCompat.getDrawable(view.context, R.drawable.drawable_post_background_blue)
        }

        initMoreMenu(view, item)

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

    private fun initMoreMenu(view: View, item: PostAdapterItem) {
        view.btnMore.setOnClickListener {
            openedMenuItemView?.isVisible = false
            view.popupContainer.isVisible = true
            openedMenuItemView = view.popupContainer
        }

        view.root.setOnClickListener {
            hidePanel()
        }

        view.complain.setOnClickListener {
            moreMenuListener.onReportClicked(item.id)
            openedMenuItemView?.isVisible = false
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
        var isMenuOpened: Boolean
    )

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
                    isMenuOpened = false
                )
            }
        }
    }
}