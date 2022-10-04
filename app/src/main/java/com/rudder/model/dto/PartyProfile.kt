package com.rudder.model.dto


data class PartyProfileResponse(
    val partyProfile: PartyProfile
)

data class PartyProfile(
    val userNickname: String,
    val partyProfileBody: String,
    val partyProfileId: Int,
    val partyProfileImages: Array<String>,
    val schoolImageUrl: String,
    val schoolName: String
)

