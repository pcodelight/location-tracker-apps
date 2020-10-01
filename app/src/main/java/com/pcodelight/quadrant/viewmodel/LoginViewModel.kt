package com.pcodelight.quadrant.viewmodel

import com.pcodelight.quadrant.ApiCallback
import com.pcodelight.quadrant.AuthHelper
import com.pcodelight.quadrant.model.AuthResponse
import com.pcodelight.quadrant.repository.UserRepository

class LoginViewModel(private val repo: UserRepository): BaseNetworkViewModel<String>() {
    fun login(username: String, password: String) {
        isLoading.postValue(true)

        repo.login(username, password, object: ApiCallback<AuthResponse> {
            override fun onSuccess(data: AuthResponse?) {
                isLoading.postValue(false)
                data?.authToken?.takeIf { it.isNotBlank() }?.let {
                    AuthHelper.instance.setAuthToken(it)
                    onSuccess.postValue(it)
                    onError.postValue("")
                }
            }

            override fun onError(error: String?) {
                isLoading.postValue(false)
                onError.postValue(error)
            }
        })
    }
}