package com.pcodelight.quadrant.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pcodelight.listener.QuadrantDataListener
import com.pcodelight.model.LocationData
import com.pcodelight.model.MonthlyData
import com.pcodelight.quadrant.repository.QLocRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardViewModel(val repo: QLocRepository) : ViewModel() {
    /**
     * Home Section
     */
    val isPermissionDenied = MutableLiveData<Boolean>()
    val locationData = MutableLiveData<Location?>()
    val isHomeLoading = MutableLiveData<Boolean>()
    val sendLocationError = MutableLiveData<String>()
    val sendLocationResult = MutableLiveData<LocationData>()

    /**
     * List Data Section
     */
    val locationRequestError = MutableLiveData<String>()
    val isListLocationDataLoading = MutableLiveData<Boolean>()
    val locationRequestResult = MutableLiveData<List<LocationData>>()

    /**
     * Monthly Data Section
     */
    val monthlyDataRequestError = MutableLiveData<String>()
    val isMonthlyDataLoading = MutableLiveData<Boolean>()
    val monthlyDataRequestResult = MutableLiveData<List<MonthlyData>>()

    /**
     * listener for QLDataRetriever Library
     */
    private val qlDataListener = object : QuadrantDataListener {
        override fun onLocationChanged(location: Location?) {
            locationData.postValue(location)
        }

        override fun onPermissionDenied() {
            isPermissionDenied.postValue(true)
        }

        override fun onSentLocationError(error: String) {
            sendLocationError.postValue(error)
        }

        override fun onSentLocationSuccess(locationData: LocationData?) {
            sendLocationResult.postValue(locationData)
        }

        override fun onMonthlyDataRequestSuccess(data: List<MonthlyData>) {
            monthlyDataRequestResult.postValue(data)
        }

        override fun onMonthlyDataRequestError(error: String) {
            monthlyDataRequestError.postValue(error)
        }

        override fun onLocationsRequestSuccess(data: List<LocationData>) {
            locationRequestResult.postValue(data)
        }

        override fun onLocationsRequestError(error: String) {
            locationRequestError.postValue(error)
        }
    }

    init {
        repo.apply {
            setDataCallback(qlDataListener)
            startTrackingLocationData()
        }
    }

    fun sendDataToServer() {
        GlobalScope.launch {
            if (locationData.value != null) {
                repo.sendData()
            } else {
                repo.startTrackingLocationData()
            }
        }
    }

    fun getLocations() {
        repo.getLocations()
    }

    fun getMonthlyData() {
        repo.getMonthlyData()
    }
}