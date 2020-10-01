package com.pcodelight.listener

import android.location.Location
import com.pcodelight.model.LocationData
import com.pcodelight.model.MonthlyData

interface QuadrantDataListener {
    /**
     * Will be invoked if there is any changes with user location
     */
    fun onLocationChanged(location: Location?)

    /**
     * Will be invoked if user reject permission request
     */
    fun onPermissionDenied()

    /**
     * Will be invoked if there is an error when sending data to server
     */
    fun onSentLocationError(error: String)

    /**
     * Will be invoked data sent successfully to server
     */
    fun onSentLocationSuccess(locationData: LocationData?)

    /**
     * Will be invoked after get monthly data request is success
     */
    fun onMonthlyDataRequestSuccess(data: List<MonthlyData>)

    /**
     * Will be invoked after get monthly data request is failed
     */
    fun onMonthlyDataRequestError(error: String)

    /**
     * Will be invoked after get monthly data request is success
     */
    fun onLocationsRequestSuccess(data: List<LocationData>)

    /**
     * Will be invoked after get monthly data request is failed
     */
    fun onLocationsRequestError(error: String)
}