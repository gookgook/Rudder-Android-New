package com.rudder.model.dto

data class UserNotification(
    val notificationId: Int,
    val notificationType: String,
    val notificationTiem: String,
    val itemId: Int,
    val notificationBody: String,
    val notificationTitle: String
)
