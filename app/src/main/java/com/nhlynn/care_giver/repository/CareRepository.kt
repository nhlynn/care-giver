package com.nhlynn.care_giver.repository

import com.nhlynn.care_giver.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CareRepository
@Inject constructor(private val apiService: ApiService) {
    suspend fun login(userName: String, password: String) = apiService.login(userName, password)

    suspend fun register(userName: String, contact: String, password: String) =
        apiService.register(userName, contact, password)

    suspend fun createNewFeed(name: String, instruction: String, caution: String, photo: String?) =
        apiService.createNewFeed(name, instruction, caution, photo)

    suspend fun getNewFeed() = apiService.getNewFeed()

    suspend fun getAlarm() = apiService.getAlarm()

    suspend fun createAlarm(alarmTime: String) = apiService.createAlarm(alarmTime)

    suspend fun closeAlarm(alId: Int, flag: Int) = apiService.closeAlarm(alId, flag)

    suspend fun nearestHospital(lat: Double, lng: Double) = apiService.nearestHospital(lat, lng)
}