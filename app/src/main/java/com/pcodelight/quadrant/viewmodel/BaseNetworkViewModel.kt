package com.pcodelight.quadrant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseNetworkViewModel<T>: ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val onError = MutableLiveData<String>()
    val onSuccess = MutableLiveData<T>()
}