package com.pcodelight.service

import com.pcodelight.model.IPData
import retrofit2.http.GET

interface IpifyService {
    @GET("https://api.ipify.org?format=json")
    suspend fun getPublicIPV4(): IPData
}