package com.shz.imagepicker.imagepicker.activity.customgallery.adapter.multiple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shz.imagepicker.imagepicker.ImagePickerLoadDelegate
import com.shz.imagepicker.imagepicker.R
import java.io.File

internal class GalleryImagesMultipleAdapter(
    private val loadDelegate: ImagePickerLoadDelegate,
    private val maximum: Int = Int.MAX_VALUE,
    private val onSelectionChanged: (List<File>) -> Unit = {},
) : ListAdapter<GalleryImagesMultipleAdapter.Model, GalleryImagesMultipleAdapter.ViewHolder>(diff) {

    private var files = ArrayList<File>()

    private val selectionIds: ArrayList<Int> = arrayListOf()

    val selectedFiles: List<File>
        get() = selectionIds.map(files::get)

    init {
        setHasStableIds(true)
    }

    fun submit(files: List<File>) {
        selectionIds.clear()
        this.files = ArrayList(files)
        submitList(files.map { Model(it, false) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.image_picker_item_gallery_image_multiple, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long = position * 1L

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Model) {
            view.setOnClickListener {
                if (item.isSelected) {
                    selectionIds.removeAll { it == bindingAdapterPosition }
                } else if (selectionIds.size < maximum) {
                    selectionIds.add(bindingAdapterPosition)
                }
                notifyItemChanged(bindingAdapterPosition)
                onSelectionChanged(selectedFiles)
                submitList(files.mapIndexed { index, file -> Model(file, selectionIds.contains(index)) })
            }
            view.findViewById<ImageView>(R.id.image)?.let { iv -> loadDelegate.load(iv, item.file) }
            view.findViewById<CheckBox>(R.id.checkbox).isChecked = item.isSelected
        }
    }

    data class Model(
        val file: File,
        val isSelected: Boolean,
    )

    companion object {
        private val diff = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(
                oldItem: Model,
                newItem: Model,
            ): Boolean = oldItem.file.path == newItem.file.path

            override fun areContentsTheSame(
                oldItem: Model,
                newItem: Model,
            ): Boolean = oldItem == newItem
        }
    }
}
