package com.rudder.model.dto

import java.sql.Timestamp

class NotificationDto {
    companion object {


        data class GetNotificationsRequest(
            var endNotificationId: Int?
        )

        data class GetNotificationsResponse(
           val notifications: List<Notification>
        )

        data class Notification(
            val itemId : Int,
            val notificationBody : String,
            val notificationId : Int,
            val notificationTime : Timestamp,
            val notificationTitle : String,
            val notificationType : String,
            val userInfoId : Int,
        )


    }
}