package com.shz.imagepicker.imagepickerapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.shz.imagepicker.imagepicker.ImagePicker
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.model.GalleryPicker
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.shz.imagepicker.imagepickerapp.databinding.ActivityMainBinding

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
                .useCamera()
                .build()
                .launch(this)
        }
        binding.btnGallery.setOnClickListener {
            imagePicker
                .useGallery()
                .build()
                .launch(this)
        }
        binding.btnGalCustomSingle.setOnClickListener {
            imagePicker
                .useGallery()
                .galleryPicker(GalleryPicker.CUSTOM)
                .build()
                .launch(this)
        }
        binding.btnGalCustomMultiple.setOnClickListener {
            imagePicker
                .useGallery()
                .multipleSelection()
                .galleryPicker(GalleryPicker.CUSTOM)
                .build()
                .launch(this)
        }
        binding.btnDialog.setOnClickListener {
            imagePicker.useGallery()
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
