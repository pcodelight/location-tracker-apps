package com.pcodelight.quadrant

import android.app.Application
import android.content.Context
import com.pcodelight.joindesign.AuthHelper

class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AuthHelper.init(applicationContext)
        instance = applicationContext
    }

    companion object {
        private lateinit var instance: Context
        fun getInstance(): Context {
            return instance
        }
    }
}