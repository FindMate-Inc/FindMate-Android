package com.example.findmate.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmate.R
import com.example.findmate.asRelativeTime
import com.example.findmate.getUtcOffsetDateTime
import com.example.findmate.repositories.posts.Post
import kotlinx.android.synthetic.main.main_post_item.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    val items = mutableListOf<Post>()

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
        items.addAll(newItems)
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

        val timeFormatted = getUtcOffsetDateTime(item.createdAt).asRelativeTime(view.context)
        view.tvTime.text = timeFormatted

    }

    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view)
}