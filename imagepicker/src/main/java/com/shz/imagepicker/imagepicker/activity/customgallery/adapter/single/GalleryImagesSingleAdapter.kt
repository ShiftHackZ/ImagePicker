package com.shz.imagepicker.imagepicker.activity.customgallery.adapter.single

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shz.imagepicker.imagepicker.R
import java.io.File

internal class GalleryImagesSingleAdapter(
    private val images: List<File> = emptyList(),
    private val onClick: (File) -> Unit = {},
) : RecyclerView.Adapter<GalleryImagesSingleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.image_picker_item_gallery_image_single, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: File) {
            view.setOnClickListener { onClick(images[bindingAdapterPosition]) }
            view.findViewById<ImageView>(R.id.image).setImageBitmap(BitmapFactory.decodeFile(item.path))
        }
    }
}
