package com.rudder.model.dto

data class SignUpInfo(
    val promotionMailAgreement: Boolean,
    val userEmail: String,
    val userPassword: String,
    val userNickname: String,
    val userProfileBody: String
)