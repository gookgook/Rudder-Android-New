package com.rudder.model.service

import com.rudder.model.dto.PartyProfile
import com.rudder.model.dto.PartyProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPartyProfileService {
    @GET("/party-profiles/{userInfoId}")
    suspend fun getPartyProfile(@Path("userInfoId") userInfoId: String): Response<PartyProfileResponse>
}