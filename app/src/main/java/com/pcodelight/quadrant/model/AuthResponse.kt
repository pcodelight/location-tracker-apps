package com.pcodelight.quadrant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuthResponse: Serializable {
    @SerializedName("token")
    var authToken: String = ""
}