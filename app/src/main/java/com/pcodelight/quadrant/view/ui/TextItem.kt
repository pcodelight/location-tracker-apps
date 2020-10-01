package com.pcodelight.quadrant.view.ui

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.pcodelight.quadrant.R
import kotlinx.android.synthetic.main.ui_textview_item.view.*

class TextItem(val text: String): AbstractItem<TextItem.ViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.ui_textview_item
    override val type: Int
        get() = TextItem::class.java.hashCode()
    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(val view: View): FastAdapter.ViewHolder<TextItem>(view) {
        override fun bindView(item: TextItem, payloads: List<Any>) {
            view.tvLabel.text = item.text
        }

        override fun unbindView(item: TextItem) {}
    }
}