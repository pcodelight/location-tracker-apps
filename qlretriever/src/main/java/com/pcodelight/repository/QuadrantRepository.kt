package com.pcodelight.repository

import android.util.Log
import com.API
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder
import com.pcodelight.listener.QuadrantDataListener
import com.pcodelight.model.LocationData
import com.pcodelight.model.MonthlyData
import com.pcodelight.service.QuadrantService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class QuadrantRepository {
    private val streamName = "sandytest02"

    fun sendData(
        authToken: String,
        longitude: Double,
        latitude: Double,
        ipAddress: String,
        responseListener: QuadrantDataListener?
    ) {
        Log.d("QLRetriever", "Now date ${Date()}")
        API.getInstance().create(QuadrantService::class.java)
            .sendLocation(authToken, latitude, longitude, ipAddress, Date().toString())
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

    suspend fun sendDataToKinesis(
        recorder: KinesisRecorder,
        longitude: Double,
        latitude: Double,
        ipAddress: String
    ) {
        try {
            val json = JSONObject().apply {
                accumulate("latitude", latitude)
                accumulate("longitude", longitude)
                accumulate("ip_address", ipAddress)
                accumulate("timestamp", getFormattedDate())
            }
            recorder.saveRecord(json.toString(), streamName)
            withContext(Dispatchers.Default) {
                recorder.submitAllRecords()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMonthlyData(authToken: String, responseListener: QuadrantDataListener?) {
        API.getInstance().create(QuadrantService::class.java)
            .getMonthlyData(authToken)
            .enqueue(object : Callback<List<MonthlyData>> {
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
            .enqueue(object : Callback<List<LocationData>> {
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

    private fun getFormattedDate() =
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
}