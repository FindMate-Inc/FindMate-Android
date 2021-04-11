package com.example.findmate.ui.main

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.findmate.R
import com.example.findmate.asRelativeTime
import com.example.findmate.getUtcOffsetDateTime
import com.example.findmate.repositories.posts.Post
import kotlinx.android.synthetic.main.main_post_item.view.*


class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    val items = mutableListOf<PostAdapterItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_post_item, parent, false)
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

    val minimizedFilter = arrayOf(InputFilter.LengthFilter(MIN_TEXT_LENGTH))
    val maximizedFilter = arrayOf(InputFilter.LengthFilter(MAX_TEXT_LENGTH))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val view = holder.itemView
        val item = items[position]

        view.tvText.text = item.text

        val sexFormatted = if (item.sex == 1) {
            view.context.getString(R.string.sex_m)
        } else if (item.sex == 2) {
            view.context.getString(R.string.sex_w)
        } else "-"

        view.mist.isVisible = item.couldMinimize && item.isMinimized
        view.tvText.filters = if (item.isMinimized) minimizedFilter else maximizedFilter
        val onClickListener = if (item.couldMinimize) {
            View.OnClickListener {
                item.isMinimized = !item.isMinimized
                notifyItemChanged(position)
            }
        } else null
        view.tvText.setOnClickListener(onClickListener)
        view.tvText.text = item.text


        val timeFormatted = getUtcOffsetDateTime(item.createdDate).asRelativeTime(view.context)
        view.tvTime.text = timeFormatted

        val descriptionFormatted = "$sexFormatted, ${item.age}"
        view.tvDescription.text = descriptionFormatted

        view.tvLocation.text = item.locations.joinToString()
    }

    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view)

    data class PostAdapterItem(
        val text: String,
        val locations: List<String>,
        val sex: Int,
        val age: Int,
        val createdDate: Long,
        val couldMinimize: Boolean,
        var isMinimized: Boolean
    )

    companion object {
        const val MIN_TEXT_LENGTH = 300
        const val MAX_TEXT_LENGTH = 9999
        fun toAdapterItems(posts: List<Post>): List<PostAdapterItem> {
            return posts.map {
                PostAdapterItem(
                    text = it.text,
                    locations = it.locations,
                    sex = it.sex,
                    age = it.age,
                    createdDate = it.createdAt,
                    couldMinimize = it.text.length > MIN_TEXT_LENGTH,
                    isMinimized = true
                )
            }
        }
    }
}