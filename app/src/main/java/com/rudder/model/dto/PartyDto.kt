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

        data class GetPartyDetailRequest(
            var partyId: Int
        )

        data class GetPartyDetailResponse(
            var partyDetail: PartyDetail
        )

        data class ApplyPartyRequest(
            var numberApplicants: Int
        )



        data class PartyDetail(
            val applyCount: Int,
            val currentNumberOfMember: Int,
            val partyDescription: String,
            val partyId: Int,
            val partyLocation: String,
            val partyPhase: String,
            val partyStatus: String,
            val partyThumbnailUrl: String,
            val partyTime: Timestamp,
            val partyTitle: String,
            val totalNumberOfMember: Int,
            val universityName: String,
            val partyMembers: List<PartyMember>
        )

        data class PartyMember(
            val partyProfileId: Int,
            val partyProfileImageUrl: String,
            val partyStatus: String,
            val userInfoId: Int,
            val userNickname: String,
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