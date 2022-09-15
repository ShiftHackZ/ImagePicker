package com.shz.imagepicker.imagepicker.activity.customgallery.adapter.single

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shz.imagepicker.imagepicker.ImagePickerLoadDelegate
import com.shz.imagepicker.imagepicker.R
import java.io.File

internal class GalleryImagesSingleAdapter(
    private val loadDelegate: ImagePickerLoadDelegate,
    private val onClick: (File) -> Unit = {},
) : ListAdapter<File, GalleryImagesSingleAdapter.ViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.image_picker_item_gallery_image_single, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: File) {
            view.setOnClickListener { onClick(getItem(bindingAdapterPosition)) }
            view.findViewById<ImageView>(R.id.image)?.let { iv -> loadDelegate.load(iv, item) }
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<File>() {
            override fun areItemsTheSame(
                oldItem: File,
                newItem: File,
            ): Boolean = oldItem.path == newItem.path

            override fun areContentsTheSame(
                oldItem: File,
                newItem: File,
            ): Boolean = oldItem == newItem
        }
    }
}
