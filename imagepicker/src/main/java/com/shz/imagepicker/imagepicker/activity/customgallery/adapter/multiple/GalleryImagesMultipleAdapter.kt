package com.shz.imagepicker.imagepicker.activity.customgallery.adapter.multiple

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shz.imagepicker.imagepicker.R
import java.io.File

internal class GalleryImagesMultipleAdapter(
    private val images: ArrayList<File> = arrayListOf(),
    private val maximum: Int = Int.MAX_VALUE,
    private val onSelectionChanged: (List<File>) -> Unit = {},
) : RecyclerView.Adapter<GalleryImagesMultipleAdapter.ViewHolder>() {

    private val selectionIds: ArrayList<Int> = arrayListOf()

    val selectedFiles: List<File>
        get() = images.filter {
            selectionIds.contains(images.indexOf(it))
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.image_picker_item_gallery_image_multiple, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position], selectionIds.contains(position))
    }

    override fun getItemCount(): Int = images.size

    override fun getItemId(position: Int): Long = position * 1L

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: File, selected: Boolean) {
            view.setOnClickListener {
                if (selected) {
                    selectionIds.removeAll { it == bindingAdapterPosition }
                } else if (selectedFiles.size < maximum) {
                    selectionIds.add(bindingAdapterPosition)
                }
                notifyItemChanged(bindingAdapterPosition)
                onSelectionChanged(selectedFiles)
            }
            view.findViewById<ImageView>(R.id.image).setImageBitmap(BitmapFactory.decodeFile(item.path))
            view.findViewById<CheckBox>(R.id.checkbox).isChecked = selected
        }
    }
}
