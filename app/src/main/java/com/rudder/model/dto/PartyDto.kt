package com.rudder.model.dto

import java.io.File
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

        data class PostPartyRequest(
            val location: String,
            val partyDescription: String,
            val partyTime: Timestamp,
            val partyTitle: String,
            val totalNumberOfMember: Int
        )

        data class PostPartyResponse(
            val partyId: Int
        )

        data class GetPartyImageUploadUrlRequest(
            val imageMetaData: ImageMetaData,
            val partyId: Int
        )

        data class GetPartyImageUploadUrlResponse(
            val urls: List<String>
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
        ) {
            companion object {
                val mock = PartyDetail(
                    applyCount = 0,
                    currentNumberOfMember = 0,
                    partyDescription = "",
                    partyId = -1,
                    partyLocation = "",
                    partyPhase = "",
                    partyStatus = "",
                    partyThumbnailUrl = "",
                    partyTime = Timestamp(System.currentTimeMillis()),
                    partyTitle = "",
                    totalNumberOfMember = 0,
                    universityName = "",
                    partyMembers = arrayListOf()
                )
            }
        }

        data class PartyMember(
            val partyProfileId: Int,
            val partyProfileImageUrl: String,
            val partyStatus: String,
            val userInfoId: Int,
            val userNickname: String,
        )

        data class ImageForUpload(
            var file: File,
            var imageMetaData: ImageMetaData
        )


        data class GetApprovedPartyResponse(
            val parties: List<PartyPreview>
        )

        data class GetAppliedPartyResponse(
            val parties: List<PartyPreview>
        )

        data class GetHostPartiesResponse(
            val parties: List<PartyOnlyDate>
        )

        data class PartyOnlyDate(
            val partyId: Int,
            val partyDate: Timestamp
        ) {
            override fun toString(): String {
                if (partyId.equals(-1)) {
                    return "N/A"
                } else {
                    val date = Date(partyDate.time)
                    val format: DateFormat = SimpleDateFormat("MMM dd")

                    return format.format(date)
                }
            }
        }


        data class GetPartyApplicantsResponse(
            val applicants: List<PartyApplicant>
        )

        data class PartyApplicant(
            val isChatExist: Boolean,
            val numberApplicants: Int,
            val partyMemberId: Int,
            val partyProfileImageUrl: String,
            val partyStatus: String,
            val userInfoId: Int,
            val userNickname: String
        )

        data class ApprovedPartyItem(
            val party: PartyPreview,
            var partyGroupChatRoom: ChatDto.Companion.PartyGroupChatRoom
        )

        data class AppliedPartyItem(
            val party: PartyPreview,
            var partyOneToOneChatRoom: ChatDto.Companion.PartyOneToOneChatRoom
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
        ) {
            companion object {
                fun getMock(): PartyPreview {
                    return PartyPreview(
                        1,
                        1,
                        false,
                        1,
                        1,
                        "",
                        "",
                        "",
                        Timestamp(System.currentTimeMillis()),
                        "",
                        1,
                        "",
                        ""
                    )
                }
            }
        }
    }

}