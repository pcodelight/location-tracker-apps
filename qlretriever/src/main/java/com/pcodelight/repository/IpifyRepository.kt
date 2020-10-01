package com.pcodelight.repository

import com.API
import com.pcodelight.service.IpifyService

class IpifyRepository {
    suspend fun getIPAddress(): String? {
        return try {
            val ipAddrResponse = API.getIpifyInstance().create(IpifyService::class.java).getPublicIPV4()
            ipAddrResponse.ip
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}