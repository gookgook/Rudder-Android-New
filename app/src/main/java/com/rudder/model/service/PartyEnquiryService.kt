package com.rudder.model.service

import com.rudder.model.dto.PartyEnquiryInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PartyEnquiryService {
    @POST("/customer-sound")
    suspend fun sendPartyEnquiry(
        @Body partyEnquiryInfo: PartyEnquiryInfo
    ): Response<Void>
}