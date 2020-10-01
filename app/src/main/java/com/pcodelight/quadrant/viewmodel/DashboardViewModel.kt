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
    val isGettingDeviceLocation = MutableLiveData<Boolean>()
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

        override fun onProcessTypeChange(isRetrieving: Boolean) {
            isGettingDeviceLocation.postValue(isRetrieving)
        }

        override fun onSentLocationError(error: String) {
            isHomeLoading.postValue(false)
            sendLocationError.postValue(error)
        }

        override fun onSentLocationSuccess(locationData: LocationData?) {
            isHomeLoading.postValue(false)
            sendLocationResult.postValue(locationData)
        }

        override fun onMonthlyDataRequestSuccess(data: List<MonthlyData>) {
            isMonthlyDataLoading.postValue(false)
            monthlyDataRequestResult.postValue(data)
        }

        override fun onMonthlyDataRequestError(error: String) {
            isMonthlyDataLoading.postValue(false)
            monthlyDataRequestError.postValue(error)
        }

        override fun onLocationsRequestSuccess(data: List<LocationData>) {
            isListLocationDataLoading.postValue(false)
            locationRequestResult.postValue(data)
        }

        override fun onLocationsRequestError(error: String) {
            isListLocationDataLoading.postValue(false)
            locationRequestError.postValue(error)
        }
    }

    init {
        repo.setDataCallback(qlDataListener)
        initTracking()
    }

    fun sendDataToServer() {
        if (locationData.value != null) {
            isHomeLoading.postValue(true)
            GlobalScope.launch {
                repo.sendData()
            }
        } else {
            repo.startTrackingLocationData()
        }
    }

    fun initTracking() {
        repo.startTrackingLocationData()
    }

    fun getLocations() {
        isListLocationDataLoading.postValue(true)
        repo.getLocations()
    }

    fun getMonthlyData() {
        isMonthlyDataLoading.postValue(true)
        repo.getMonthlyData()
    }
}