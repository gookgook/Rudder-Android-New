package com.rudder.model.service

import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface FixMemberService {
    @PATCH("/parties/{partyId}/fix-members")
    suspend fun fixMembers(
        @Path("partyId") partyId: Int
    ): Response<Void>
}