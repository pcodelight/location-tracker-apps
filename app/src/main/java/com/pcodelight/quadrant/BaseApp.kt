package com.pcodelight.quadrant

import android.app.Application
import com.pcodelight.joindesign.AuthHelper

class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AuthHelper.init(this)
    }
}