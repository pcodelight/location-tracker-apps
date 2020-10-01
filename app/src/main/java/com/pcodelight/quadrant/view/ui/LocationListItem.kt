package com.pcodelight.quadrant.view.ui

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.pcodelight.model.LocationData
import com.pcodelight.quadrant.R
import kotlinx.android.synthetic.main.ui_location_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class LocationListItem(val locationData: LocationData) : AbstractItem<LocationListItem.ViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.ui_location_item
    override val type: Int
        get() = LocationListItem::class.java.hashCode()

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<LocationListItem>(view) {
        override fun bindView(item: LocationListItem, payloads: List<Any>) {
            view.apply {
                tvId.text = context.getString(R.string.id_placeholder, item.locationData.id)
                tvIP.text = context.getString(R.string.ip_placeholder, item.locationData.ipAddress)
                tvLatitude.text = context.getString(R.string.lat_placeholder, item.locationData.latitude)
                tvLongitude.text = context.getString(R.string.long_placeholder, item.locationData.longitude)
                tvTimestamp.text = context.getString(
                    R.string.timestamp_placeholder,
                    getFormattedTime(item.locationData.timestamp)
                )
            }
        }

        private fun getFormattedTime(date: Date) =
            SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
        override fun unbindView(item: LocationListItem) {}
    }
}