package com.rudder.model.dto

import java.io.Serializable

data class ApplicantProfileRequest(
    val partyId: Int,
    val partyMemberId: Int,
    val userInfoId: Int
): Serializable