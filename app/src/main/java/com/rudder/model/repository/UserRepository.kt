package com.rudder.model.repository

import com.rudder.BuildConfig
import com.rudder.model.dto.NotificationDto
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.PartyProfileResponse
import com.rudder.model.service.GetNotificationService
import com.rudder.model.service.GetPartyProfileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class UserRepository {
    companion object{
        val instance = UserRepository()
    }
    private val getPartyProfileService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(
        GetPartyProfileService::class.java)

    suspend fun getPartyProfile(userInfoId: Int): Response<PartyProfileResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getPartyProfileService.getPartyProfile(userInfoId = userInfoId.toString())
        }.await()
    }
}