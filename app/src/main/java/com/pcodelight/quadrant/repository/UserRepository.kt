package com.pcodelight.quadrant.repository

import com.pcodelight.quadrant.API
import com.pcodelight.quadrant.ApiCallback
import com.pcodelight.quadrant.model.AuthResponse
import com.pcodelight.quadrant.service.UserService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    fun login(username: String, password: String, callback: ApiCallback<AuthResponse>) {
        API.instance.create(UserService::class.java)
            .login(username, password)
            .enqueue(object: Callback<AuthResponse> {
                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    callback.onError(t.toString())
                }

                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onSuccess(response.body())
                    } else {
                        callback.onError("Username or password is wrong, or try again later")
                    }
                }
            })

    }
}