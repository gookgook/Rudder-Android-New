package com.rudder.model.service

import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetPartyService {
    @GET("/parties")
    suspend fun getParties(
        @Query("endPartyId") endPartyId: Int? = null
    ): Response<PartyDto.Companion.GetPartiesResponse>

    @GET("/parties/{partyId}")
    suspend fun getPartyDetail(
        @Path("partyId") partyId: Int
    ): Response<PartyDto.Companion.GetPartyDetailResponse>

    @GET("/parties/approved")
    suspend fun getApprovedParties(

    ): Response<PartyDto.Companion.GetApprovedPartyResponse>

    @GET("/parties/pending")
    suspend fun getAppliedParties(

    ): Response<PartyDto.Companion.GetAppliedPartyResponse>

}