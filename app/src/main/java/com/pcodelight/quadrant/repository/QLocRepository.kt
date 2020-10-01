package com.pcodelight.quadrant.repository

import com.pcodelight.listener.QuadrantDataListener
import com.pcodelight.qlretriever.QLRetriever

class QLocRepository {
    fun setDataCallback(qlDataListener: QuadrantDataListener) {
        QLRetriever.getInstance()?.setQuadrantDataListener(qlDataListener)
    }

    /**
     * to start listening to any location data changes
     */
    fun startTrackingLocationData() {
        QLRetriever.getInstance()?.startTracking()
    }

    /**
     * for sending data to server
     */
    suspend fun sendData() {
        QLRetriever.getInstance()?.sendLocationData()
    }

    /**
     * it's already implemented at the backend
     * after our location data we've sent is received, the backend not only write to DB but also
     * send to Kinesis
     */
    fun sendDataToKinesis() {}

    fun getMonthlyData() {
        QLRetriever.getInstance()?.getMonthlyData()
    }

    fun getLocations() {
        QLRetriever.getInstance()?.getLocations()
    }
}