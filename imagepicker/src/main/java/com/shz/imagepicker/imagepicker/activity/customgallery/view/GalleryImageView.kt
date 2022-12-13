package com.shz.imagepicker.imagepicker.activity.customgallery.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
internal class GalleryImageView : ImageView {

    private val hwRatio = 1f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight

        val calculatedHeight: Int = calculateHeightByRatio(width)

        if (calculatedHeight != height) {
            setMeasuredDimension(width, calculatedHeight)
        }
    }

    private fun calculateHeightByRatio(side: Int): Int {
        return (hwRatio * side.toFloat()).toInt()
    }
}
