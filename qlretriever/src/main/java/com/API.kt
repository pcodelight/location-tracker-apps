package com

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class API {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8125"
        private const val IPIFY_BASE_URL = "https://api.ipify.org"
        private var instance: Retrofit? = null
        private var ipifyInstance: Retrofit? = null

        fun getInstance(): Retrofit {
            if (instance == null) {
                val builder = OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)

                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return instance!!
        }

        fun getIpifyInstance(): Retrofit {
            if (ipifyInstance == null) {
                val builder = OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)

                ipifyInstance = Retrofit.Builder()
                    .baseUrl(IPIFY_BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return ipifyInstance!!
        }
    }


}