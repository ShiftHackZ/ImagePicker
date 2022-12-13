package com.shz.imagepicker.imagepickerapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.shz.imagepicker.imagepicker.ImagePicker
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.ImagePickerLoadDelegate
import com.shz.imagepicker.imagepicker.model.GalleryPicker
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.shz.imagepicker.imagepickerapp.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity(), ImagePickerCallback {

    private val imagePicker: ImagePicker.Builder
        get() = ImagePicker.Builder(this.packageName + ".provider", this)

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ImagesDemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adapter = ImagesDemoAdapter()

        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(this, 2)

        binding.btnCamera.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useCamera()
                .build()
                .launch(this)
        }
        binding.btnGallery.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .build()
                .launch(this)
        }
        binding.btnGalCustomSingle.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .galleryPicker(GalleryPicker.CUSTOM)
                .loadDelegate { imageView, file ->
                    Glide.with(this)
                        .load(file)
                        .into(imageView)
                }
                .build()
                .launch(this)
        }
        binding.btnGalCustomMultiple.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .multipleSelection()
                .galleryPicker(GalleryPicker.CUSTOM)
                .loadDelegate { imageView, file ->
                    Glide.with(this)
                        .load(file)
                        .into(imageView)
                }
                .build()
                .launch(this)
        }
        binding.btnDialog.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .useCamera()
                .multipleSelection()
                .galleryPicker(GalleryPicker.NATIVE)
                .build()
                .launch(this)
        }
    }

    override fun onImagePickerResult(result: PickedResult) {
        Log.d("MainActivity", "result: $result")
        when (result) {
            PickedResult.Empty -> {
                adapter.submitList(emptyList())
            }
            is PickedResult.Error -> {
                result.throwable.printStackTrace()
            }
            is PickedResult.Multiple -> {
                adapter.submitList(result.images)
            }
            is PickedResult.Single -> {
                adapter.submitList(listOf(result.image))
            }
        }
    }
}
