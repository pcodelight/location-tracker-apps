package com.pcodelight.joindesign

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.pcodelight.quadrant.BaseApp
import com.pcodelight.quadrant.Conf
import com.pcodelight.quadrant.view.screen.LoginScreen
import java.util.*

class AuthHelper(context: Context) {
    private val tokenExpireIn = 36000 //second
    private var token: String? = null
    private var sharedPreferences: SharedPreferences? = null

    fun getAuthToken(): String? {
        sharedPreferences?.also { sp ->
            sp.getString(Conf.ACCESS_TOKEN_KEY, "")?.takeIf { it.isNotBlank() }?.let {
                token = it
            }
        }
        return token
    }

    fun refreshAuthToken() {
        BaseApp.getInstance().apply {
            startActivity(Intent(this, LoginScreen::class.java).apply {
                putExtra(LoginScreen.LOGIN_FLAG, LoginScreen.EXPIRED_TOKEN)
            })
        }
    }

    fun isHasAuthToken(): Boolean {
        return sharedPreferences?.getString(Conf.ACCESS_TOKEN_KEY, "").isNullOrBlank().not()
    }

    fun isAuthExpired(): Boolean {
        val lastUpdate = sharedPreferences?.getLong(Conf.LAST_UPDATE_TOKEN, 0) ?: 0
        val delta = Date().time - lastUpdate
        return delta > tokenExpireIn
    }

    fun removeAuthToken() {
        sharedPreferences?.edit()?.apply {
            remove(Conf.ACCESS_TOKEN_KEY)
            apply()
        }
        token = ""
    }

    fun setAuthToken(authToken: String) {
        sharedPreferences?.edit()?.apply {
            putString(Conf.ACCESS_TOKEN_KEY, authToken)
            putLong(Conf.LAST_UPDATE_TOKEN, Date().time)
            apply()
        }
    }

    init {
        sharedPreferences =
            context.getSharedPreferences(Conf.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var instance: AuthHelper

        fun init(context: Context) {
            instance = AuthHelper(context)
        }
    }
}