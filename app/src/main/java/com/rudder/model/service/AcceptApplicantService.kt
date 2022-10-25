package com.rudder.model.service

import com.rudder.model.dto.AcceptApplicantRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AcceptApplicantService {
    @PATCH("/parties/{partyId}/approve")
    suspend fun acceptApplicant(
        @Path("partyId") partyId: Int,
        @Body acceptApplicantRequest: AcceptApplicantRequest
    ): Response<Void>
}
