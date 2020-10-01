package com.pcodelight.repository

import com.API
import com.pcodelight.listener.QuadrantDataListener
import com.pcodelight.model.LocationData
import com.pcodelight.model.MonthlyData
import com.pcodelight.service.QuadrantService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class QuadrantRepository {
    fun sendData(
        authToken: String,
        longitude: Double,
        latitude: Double,
        ipAddress: String,
        responseListener: QuadrantDataListener?
    ) {
        API.getInstance().create(QuadrantService::class.java)
            .sendLocation(authToken, latitude, longitude, ipAddress, getCurrentTimeFormatted())
            .enqueue(object : Callback<LocationData> {
                override fun onFailure(call: Call<LocationData>, t: Throwable) {
                    responseListener?.onSentLocationError(t.toString())
                }

                override fun onResponse(
                    call: Call<LocationData>,
                    response: Response<LocationData>
                ) {
                    if (response.isSuccessful) {
                        responseListener?.onSentLocationSuccess(response.body())
                    } else {
                        responseListener?.onSentLocationError(response.message())
                    }
                }
            })
    }

    fun getMonthlyData(authToken: String, responseListener: QuadrantDataListener?) {
        API.getInstance().create(QuadrantService::class.java)
            .getMonthlyData(authToken)
            .enqueue(object: Callback<List<MonthlyData>> {
                override fun onFailure(call: Call<List<MonthlyData>>, t: Throwable) {
                    responseListener?.onMonthlyDataRequestError(t.toString())
                }

                override fun onResponse(
                    call: Call<List<MonthlyData>>,
                    response: Response<List<MonthlyData>>
                ) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        responseListener?.onMonthlyDataRequestSuccess(result)
                    } else {
                        responseListener?.onMonthlyDataRequestError("Can't fetch monthly data")
                    }
                }
            })
    }

    fun getLocations(authToken: String, responseListener: QuadrantDataListener?) {
        API.getInstance().create(QuadrantService::class.java)
            .getLocations(authToken)
            .enqueue(object: Callback<List<LocationData>> {
                override fun onFailure(call: Call<List<LocationData>>, t: Throwable) {
                    responseListener?.onLocationsRequestError(t.toString())
                }

                override fun onResponse(
                    call: Call<List<LocationData>>,
                    response: Response<List<LocationData>>
                ) {
                    val locations = response.body()
                    if (response.isSuccessful && locations != null) {
                        responseListener?.onLocationsRequestSuccess(locations)
                    } else {
                        responseListener?.onLocationsRequestError("Can't fetch locations")
                    }
                }
            })
    }

    private fun getCurrentTimeFormatted() =
        SimpleDateFormat(
            "dd/mm/YYYY hh:mm:ss",
            Locale.getDefault()
        ).format(Date())
}