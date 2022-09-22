package com.rudder.model.dto

data class LoginInfo(
    val notificationToken: String,
    val os: String,
    val userId: String,
    val userPassword: String
)
