package com.rudder.model.repository

import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitClient
import com.rudder.model.service.ApplyPartyService
import com.rudder.model.service.CreatePartyService
import com.rudder.model.service.GetPartyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class PartyRepository {
    companion object{
        val instance = PartyRepository()
    }
    private val getPartyService = RetrofitClient.getClient("https://test.rudderuni.com").create(
        GetPartyService::class.java)

    private val applyPartyService = RetrofitClient.getClient("https://test.rudderuni.com").create(
        ApplyPartyService::class.java)

    private val createPartyService = RetrofitClient.getClient("https://test.rudderuni.com").create(
        CreatePartyService::class.java)

    suspend fun getParties(getPartiesRequest: PartyDto.Companion.GetPartiesRequest): Response<PartyDto.Companion.GetPartiesResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getPartyService.getParties(endPartyId = getPartiesRequest.endPartyId)
        }.await()
    }

    suspend fun getPartyDetail(getPartyDetailRequest: PartyDto.Companion.GetPartyDetailRequest): Response<PartyDto.Companion.GetPartyDetailResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getPartyService.getPartyDetail(partyId = getPartyDetailRequest.partyId)
        }.await()
    }

    suspend fun applyParty(partyId:Int,applyPartyRequest: PartyDto.Companion.ApplyPartyRequest): Response<Void> {

        return CoroutineScope(Dispatchers.IO).async {
            applyPartyService.applyParty(partyId, applyPartyRequest)
        }.await()
    }

    suspend fun createParty(postPartyRequest: PartyDto.Companion.PostPartyRequest): Response<PartyDto.Companion.PostPartyResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            createPartyService.createParty(postPartyRequest)
        }.await()
    }

    suspend fun getPartyImageUploadUrl(getPartyImageUploadUrlRequest: PartyDto.Companion.GetPartyImageUploadUrlRequest): Response<PartyDto.Companion.GetPartyImageUploadUrlResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            createPartyService.getPartyImageUploadUrl(getPartyImageUploadUrlRequest)
        }.await()
    }


}