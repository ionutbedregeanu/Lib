package com.lib.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lib.R
import com.lib.ui.model.UiAlbum
import kotlinx.android.synthetic.main.albums_list_item.view.*

class AlbumsAdapter : ListAdapter<UiAlbum, AlbumsAdapter.ViewHolder>(AlbumsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView: View = LayoutInflater.from(parent.context).inflate(R.layout.albums_list_item, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(album: UiAlbum) {
            itemView.title.text = album.title
        }
    }
}

class AlbumsDiffCallback : DiffUtil.ItemCallback<UiAlbum>() {
    override fun areItemsTheSame(oldItem: UiAlbum, newItem: UiAlbum) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UiAlbum, newItem: UiAlbum) = oldItem == newItem
}
