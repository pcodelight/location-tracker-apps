package com.pcodelight.service

import com.pcodelight.model.LocationData
import com.pcodelight.model.MonthlyData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface QuadrantService {
    @FormUrlEncoded
    @POST("/location")
    fun sendLocation(
        @Header("Authorization") authToken: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("ip_address") ipAddress: String,
        @Field("timestamp") timestamp: String
    ): Call<LocationData>

    @GET("/locations")
    fun getLocations(
        @Header("Authorization") authToken: String
    ): Call<List<LocationData>>

    @GET("/monthlyData")
    fun getMonthlyData(
        @Header("Authorization") authToken: String
    ): Call<List<MonthlyData>>
}