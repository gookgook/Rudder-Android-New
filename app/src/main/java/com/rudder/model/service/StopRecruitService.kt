package com.rudder.model.service

import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface StopRecruitService {
    @PATCH("/parties/{partyId}/stop-recruit")
    suspend fun stopRecruit(
        @Path("partyId") partyId: Int
    ): Response<Void>
}