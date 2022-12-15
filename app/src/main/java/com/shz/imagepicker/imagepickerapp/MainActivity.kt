package com.shz.imagepicker.imagepickerapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
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
        initializeButtons()
        initializeImagesList()
    }

    override fun onImagePickerResult(result: PickedResult) {
        Log.d("MainActivity", "result: $result")
        when (result) {
            PickedResult.Empty -> {
                adapter.submitList(emptyList())
            }
            is PickedResult.Error -> {
                result.throwable.printStackTrace()
                AlertDialog.Builder(this)
                    .setTitle(R.string.action_error)
                    .setMessage(result.throwable.message.toString())
                    .setPositiveButton(R.string.action_ok) { d, _ -> d.dismiss() }
                    .show()
            }
            is PickedResult.Multiple -> {
                adapter.submitList(result.images)
            }
            is PickedResult.Single -> {
                adapter.submitList(listOf(result.image))
            }
        }
    }

    private fun initializeButtons() = with(binding) {
        btnCamera.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useCamera()
                .build()
                .launch(this@MainActivity)
        }
        btnGallery.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .multipleSelection(binding.switchMultipleSelection.isChecked)
                .build()
                .launch(this@MainActivity)
        }
        btnGalCustomSingle.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .multipleSelection(binding.switchMultipleSelection.isChecked)
                .galleryPicker(GalleryPicker.CUSTOM)
                .loadDelegate { imageView, file ->
                    Glide.with(imageView)
                        .load(file)
                        .into(imageView)
                }
                .build()
                .launch(this@MainActivity)
        }
        btnDialog.setOnClickListener {
            imagePicker
                .autoRotate(binding.switchAutoRotate.isChecked)
                .useGallery()
                .useCamera()
                .multipleSelection(binding.switchMultipleSelection.isChecked)
                .galleryPicker(GalleryPicker.NATIVE)
                .build()
                .launch(this@MainActivity)
        }
    }

    private fun initializeImagesList() = with(binding) {
        adapter = ImagesDemoAdapter()
        recyclerImages.adapter = adapter
        recyclerImages.layoutManager = GridLayoutManager(this@MainActivity, 2)
    }
}
