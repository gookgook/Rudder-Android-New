package com.rudder.model.service

import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetInitDataService {
    @GET("/initial-data/guest")
    suspend fun getGuestInitData(
        @Query("appVersion") appVersion: String,
        @Query("os") os: String
    ): Response<GetGuestInitDataResponse>

}

data class GetGuestInitDataResponse(
    val results: GuestInitData
)

data class GuestInitData(
    val isNewest: Boolean,
    val notReadNotificationCount: Int
)