package com.rudder.model.repository

import com.rudder.BuildConfig
import com.rudder.model.dto.NotificationDto
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.PartyProfileResponse
import com.rudder.model.service.GetGuestInitDataResponse
import com.rudder.model.service.GetInitDataService
import com.rudder.model.service.GetNotificationService
import com.rudder.model.service.GetPartyProfileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class InitDataRepository {
    companion object{
        val instance = InitDataRepository()
    }
    private val getInitDataService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(
        GetInitDataService::class.java)

    suspend fun getGuestInitData(appVersion:String, os : String): Response<GetGuestInitDataResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getInitDataService.getGuestInitData(appVersion,os)
        }.await()
    }
}