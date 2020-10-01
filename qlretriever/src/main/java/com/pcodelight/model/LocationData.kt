package com.pcodelight.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class LocationData: Serializable {
    var id: Int = 0
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    @SerializedName("ip_address")
    var ipAddress: String = ""
    var timestamp: Date = Date()
}