package com.pcodelight.quadrant.service

import com.pcodelight.quadrant.model.AuthResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<AuthResponse>
}