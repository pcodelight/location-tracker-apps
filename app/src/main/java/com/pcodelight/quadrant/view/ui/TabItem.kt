package com.pcodelight.quadrant.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.pcodelight.quadrant.R
import kotlinx.android.synthetic.main.item_tab_layout.view.*

class TabItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var ivImg: ImageView
    private var tvLabel: TextView

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val view = View.inflate(context, R.layout.item_tab_layout, this)
        ivImg = view.ivTab
        tvLabel = view.tvTab
    }

    fun bind(@DrawableRes res: Int, text: String) {
        ivImg.setImageResource(res)
        tvLabel.text = text
    }
}