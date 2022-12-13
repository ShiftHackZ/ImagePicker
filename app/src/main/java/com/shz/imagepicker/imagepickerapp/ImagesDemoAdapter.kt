package com.shz.imagepicker.imagepickerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shz.imagepicker.imagepicker.model.PickedImage
import com.shz.imagepicker.imagepickerapp.databinding.ItemDemoImageBinding

class ImagesDemoAdapter : ListAdapter<PickedImage, ImagesDemoAdapter.ViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemDemoImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemDemoImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PickedImage) {
            Glide.with(binding.image)
                .load(item.file)
                .centerCrop()
                .into(binding.image)
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<PickedImage>() {
            override fun areItemsTheSame(
                oldItem: PickedImage,
                newItem: PickedImage
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PickedImage,
                newItem: PickedImage
            ): Boolean = oldItem == newItem
        }
    }
}