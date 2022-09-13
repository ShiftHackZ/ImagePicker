package com.shz.imagepicker.imagepicker.activity.customgallery

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shz.imagepicker.imagepicker.ImagePicker
import com.shz.imagepicker.imagepicker.ImagePickerCallback
import com.shz.imagepicker.imagepicker.R
import com.shz.imagepicker.imagepicker.activity.customgallery.adapter.multiple.GalleryImagesMultipleAdapter
import com.shz.imagepicker.imagepicker.activity.customgallery.adapter.single.GalleryImagesSingleAdapter
import com.shz.imagepicker.imagepicker.core.ImagePickerActivity
import com.shz.imagepicker.imagepicker.model.PickedImage
import com.shz.imagepicker.imagepicker.model.PickedResult
import com.shz.imagepicker.imagepicker.model.PickedSource
import com.shz.imagepicker.imagepicker.utils.checkGalleryPermission
import com.shz.imagepicker.imagepicker.utils.getAllImages
import java.io.File

internal class GalleryPickerCustomActivity : ImagePickerActivity() {

    override val requestCode: Int = 54503

    private var multipleSelection = false
    private var max = Int.MAX_VALUE
    private var min = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_picker_activity_gallery_custom)

        multipleSelection = intent.getBooleanExtra(BUNDLE_MULTI_SELECTION, false)
        max = intent.getIntExtra(BUNDLE_MAXIMUM, 10)
        min = intent.getIntExtra(BUNDLE_MINIMUM, 1)

        findViewById<View>(R.id.bg_bottom_view)?.isVisible = multipleSelection
        findViewById<FrameLayout>(R.id.fl_bottom_view)?.isVisible = multipleSelection

        findViewById<Button>(R.id.btn_select)?.setOnClickListener {
            (findViewById<RecyclerView>(R.id.rv_images).adapter as? GalleryImagesMultipleAdapter)
                ?.selectedFiles
                ?.let(::handleSelection)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(
                if (multipleSelection) R.string.image_picker_gallery_title_multiple
                else R.string.image_picker_gallery_title_single
            )
        }

        checkGalleryPermission(requestCode, ::startPicker)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun startPicker() {
        getAllImages(this).let { files ->
            findViewById<RecyclerView>(R.id.rv_images).apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = if (!multipleSelection) {
                    GalleryImagesSingleAdapter(files) { selectedFile ->
                        handleSelection(listOf(selectedFile))
                    }
                } else {
                    GalleryImagesMultipleAdapter(ArrayList(files), max) { selectedFiles ->
                        onSelectionChanged(selectedFiles.size)
                    }
                }
            }
            ImagePicker.debugLog("Files: $files")
        }
    }

    private fun onSelectionChanged(count: Int) {
        ImagePicker.debugLog("onSelectionChanged: $count")
        findViewById<TextView>(R.id.tv_select_cont)?.text = "$count"
        findViewById<Button>(R.id.btn_select)?.isEnabled = count >= min
    }

    private fun handleSelection(list: List<File>) {
        when (list.size) {
            0 -> PickedResult.Empty
            1 -> PickedResult.Single(PickedImage(PickedSource.GALLERY_CUSTOM, list.first()))
            else -> PickedResult.Multiple(list.map { PickedImage(PickedSource.GALLERY_CUSTOM, it) })
        }.let(callback::onImagePickerResult)
        finish()
    }

    companion object {
        internal const val BUNDLE_MULTI_SELECTION = "bundle_multi_selection"
        internal const val BUNDLE_MINIMUM = "bundle_minimum"
        internal const val BUNDLE_MAXIMUM = "bundle_maximum"

        @JvmField
        internal var callback: ImagePickerCallback = ImagePickerCallback { }
    }
}
