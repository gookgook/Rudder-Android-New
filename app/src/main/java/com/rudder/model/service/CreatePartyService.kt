package com.rudder.model.service

import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.*

interface CreatePartyService {
    @POST("/parties")
    suspend fun createParty(
        @Body postPartyRequest: PartyDto.Companion.PostPartyRequest
    ): Response<PartyDto.Companion.PostPartyResponse>

    @POST("/parties/image-upload-url/generate")
    suspend fun getPartyImageUploadUrl(
        @Body getPartyImageUploadUrlRequest: PartyDto.Companion.GetPartyImageUploadUrlRequest
    ): Response<PartyDto.Companion.GetPartyImageUploadUrlResponse>


}