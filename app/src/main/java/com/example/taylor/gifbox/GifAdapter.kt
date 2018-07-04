package com.example.taylor.gifbox

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.taylor.gifbox.model.Gif
import kotlinx.android.synthetic.main.cell_gif.view.*

class GifAdapter: PagedListAdapter<Gif, GifViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.gifTitle.text = getItem(position)?.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_gif, parent, false)
        return GifViewHolder(view)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Gif>() {
            override fun areItemsTheSame(oldItem: Gif?, newItem: Gif?): Boolean =
                    oldItem?.id == newItem?.id

            override fun areContentsTheSame(oldItem: Gif?, newItem: Gif?): Boolean =
                    oldItem == newItem
        }
    }
}

class GifViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val gifTitle: TextView = itemView.gifTitle
}