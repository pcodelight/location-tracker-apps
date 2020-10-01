package com.pcodelight.quadrant

interface ApiCallback<T> {
    fun onSuccess(data: T?)
    fun onError(error: String?)
}