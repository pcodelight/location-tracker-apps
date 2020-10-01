package com.pcodelight.quadrant

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class API {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8125"

        val instance: Retrofit
            get() {
                AuthHelper.instance.run {
                    if (isHasAuthToken() && isAuthExpired()) {
                        refreshAuthToken()
                    }
                }

                val token = AuthHelper.instance.getAuthToken()
                val builder = OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)

                if (AuthHelper.instance.isHasAuthToken()) {
                    builder.addInterceptor(object: Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val request = chain.request()
                            val authorizedReq = request.newBuilder()
                                .addHeader("Authorization", "$token")
                                .build()

                            return chain.proceed(authorizedReq)
                        }

                    })
                }

                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
    }
}