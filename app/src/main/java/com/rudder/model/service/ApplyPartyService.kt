package com.rudder.model.service

import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.*

interface ApplyPartyService {
    @POST("/parties/{partyId}/apply")
    suspend fun applyParty(
        @Path("partyId") partyId: Int,
        @Body applyPartyRequest: PartyDto.Companion.ApplyPartyRequest
    ): Response<Void>
}