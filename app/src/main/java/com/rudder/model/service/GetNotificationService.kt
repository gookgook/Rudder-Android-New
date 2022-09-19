package com.rudder.model.service

import com.rudder.model.dto.NotificationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetNotificationService {
    @GET("/notifications")
    suspend fun getNotifications(
        @Query("endNotificationId") endNotificationId: Int? = null
    ): Response<NotificationDto.Companion.GetNotificationsResponse>
}