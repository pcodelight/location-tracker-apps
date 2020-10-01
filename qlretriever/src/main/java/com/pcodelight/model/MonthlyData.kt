package com.pcodelight.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MonthlyData: Serializable {
    @SerializedName("_id")
    var month: Int = 0
    var count: Int = 0
}