package com.rudder.model.repository

import com.rudder.model.dto.NotificationDto
import com.rudder.model.RetrofitClient
import com.rudder.model.service.GetNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class NotificationRepository {
    companion object{
        val instance = NotificationRepository()
    }
    private val getNotificationService = RetrofitClient.getClient("https://test.rudderuni.com").create(
        GetNotificationService::class.java)

    suspend fun getNotifications(getNotificationsRequest: NotificationDto.Companion.GetNotificationsRequest): Response<NotificationDto.Companion.GetNotificationsResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getNotificationService.getNotifications(endNotificationId = getNotificationsRequest.endNotificationId)
        }.await()
    }
}