package com.rudder.model.service

import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CancelPartyService {
    @PATCH("/parties/{partyId}/cancel")
    suspend fun cancelParty(
        @Path("partyId") partyId: Int
    ): Response<Void>
}