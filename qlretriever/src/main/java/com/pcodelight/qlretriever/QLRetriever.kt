package com.pcodelight.qlretriever

import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder
import com.amazonaws.regions.Regions
import com.pcodelight.listener.QuadrantDataListener
import com.pcodelight.repository.IpifyRepository
import com.pcodelight.repository.QuadrantRepository
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.configuration.PermissionConfiguration
import com.yayandroid.locationmanager.constants.ProcessType
import com.yayandroid.locationmanager.listener.LocationListener


class QLRetriever(
    activity: Activity,
    applicationContext: Context,
    private var authToken: String
) {
    private var quadrantDataListener: QuadrantDataListener? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var ipAddress: String? = null
    private val quadrantRepo: QuadrantRepository = QuadrantRepository()
    private val ipifyRepo: IpifyRepository = IpifyRepository()

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
            }
            quadrantDataListener?.onLocationChanged(location)
        }

        override fun onPermissionGranted(alreadyHadPermission: Boolean) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
        override fun onProcessTypeChanged(processType: Int) {
            Log.d("QLRetriever", "Status : $processType")
            quadrantDataListener?.onProcessTypeChange(
                processType > ProcessType.ASKING_PERMISSIONS
            )
        }

        override fun onLocationFailed(type: Int) {}
    }

    private val rationalMessage = "Location permission is needed"
    private val gpsMessage = "Google play service is needed"
    private val permissionConfiguration = PermissionConfiguration.Builder()
        .rationaleMessage(rationalMessage)
        .build()

    private val googlePlayServiceConfiguration = GooglePlayServicesConfiguration.Builder()
        .build()

    private val defaultProviderConfiguration = DefaultProviderConfiguration.Builder()
        .gpsMessage(gpsMessage)
        .build()

    private val locationManagerConfiguration = LocationConfiguration.Builder()
        .keepTracking(true)
        .askForPermission(permissionConfiguration)
        .useGooglePlayServices(googlePlayServiceConfiguration)
        .useDefaultProviders(defaultProviderConfiguration)
        .build()

    private val locationManager = LocationManager
        .Builder(applicationContext)
        .activity(activity)
        .configuration(locationManagerConfiguration)
        .notify(locationListener)
        .build()

    fun setQuadrantDataListener(listener: QuadrantDataListener) {
        quadrantDataListener = listener
    }

    fun startTracking() {
        locationManager.get()
    }

    fun getMonthlyData() {
        quadrantRepo.getMonthlyData(authToken, quadrantDataListener)
    }

    fun getLocations() {
        quadrantRepo.getLocations(authToken, quadrantDataListener)
    }

    /**
     * SendLocationDataToKinesis already implemented at backend side
     * After we send request to database, backend service automatically sends it
     * to Kinesis
     * So there's no need to reimplement at client side
     */
    suspend fun sendLocationData() {
        val longitude = longitude ?: throw Exception("Longitude is null")
        val latitude = latitude ?: throw Exception("Latitude is null")
        val ipAddress = ipifyRepo.getIPAddress()
            ?: this.ipAddress
            ?: throw Exception("IP address is not set yet")

        quadrantRepo.sendData(authToken, longitude, latitude, ipAddress, quadrantDataListener)
    }

    suspend fun sendDataToKinesis() {
        val longitude = longitude ?: throw Exception("Longitude is null")
        val latitude = latitude ?: throw Exception("Latitude is null")
        val ipAddress = ipifyRepo.getIPAddress()
            ?: this.ipAddress
            ?: throw Exception("IP address is not set yet")

        quadrantRepo.sendDataToKinesis(kinesisRecorder, longitude, latitude, ipAddress)
    }

    /**
     * Actually, i already implemented kinesis at backend service
     * This is just for experimenting
     */
    private val region = Regions.US_EAST_2

    /**
     * Seems much safer using cognito,
     * in the meantime, we could utilize build config to hide
     * the real value of access_id / secret_key, pass it when build the apk
     */
    private val credential = BasicAWSCredentials(
        BuildConfig.ACCESS_ID,
        BuildConfig.SECRET_KEY
    )

    private val provider = StaticCredentialsProvider(credential)

    private val kinesisRecorder = KinesisRecorder(
        applicationContext.cacheDir,
        region,
        provider
    )

    init {
        LocationManager.enableLog(true)
    }

    companion object {
        private var instance: QLRetriever? = null
        fun init(activity: Activity, appContext: Context, authToken: String) {
            instance = QLRetriever(activity, appContext, authToken)
        }
        fun getInstance() = instance
    }
}