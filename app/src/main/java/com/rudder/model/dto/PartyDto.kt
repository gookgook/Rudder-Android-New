package com.rudder.model.dto

import java.sql.Timestamp

class PartyDto {
    companion object {

        data class GetPartiesResponse(
            val parties: List<PartyPreview>
        )

        data class GetPartiesRequest(
            var endPartyId: Int?
        )

        data class PartyPreview(
            val applyCount: Int,
            val currentNumberOfMember: Int,
            val isChatExist: Boolean,
            val partyChatRoomId: Int,
            val partyId: Int,
            val partyPhase: String,
            val partyStatus: String,
            val partyThumbnailUrl: String,
            val partyTime: Timestamp,
            val partyTitle: String,
            val totalNumberOfMember: Int,
            val universityLogoUrl: String,
            val universityName: String
        ){
            companion object{
                fun getMock() : PartyPreview {
                    return PartyPreview(1,1,false,1,1,"","","",Timestamp(System.currentTimeMillis()),"",1,"","")
                }
            }
        }
    }
}