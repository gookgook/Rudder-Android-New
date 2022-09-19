package com.rudder.model.service

import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetPartyService {
    @GET("/parties")
    suspend fun getParties(
        @Query("endPartyId") endPartyId: Int? = null
    ): Response<PartyDto.Companion.GetPartiesResponse>
}