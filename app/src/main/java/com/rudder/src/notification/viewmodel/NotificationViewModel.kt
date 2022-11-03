package com.rudder.src.notification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.NotificationDto
import com.rudder.model.RetrofitClient
import com.rudder.model.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _notificationList: MutableLiveData<List<NotificationDto.Companion.Notification>> = MutableLiveData()
    val notificationList: LiveData<List<NotificationDto.Companion.Notification>> = _notificationList

    private val getNotificationsRequest = NotificationDto.Companion.GetNotificationsRequest(endNotificationId = null)

    val isLoadingFlag = MutableLiveData<Boolean> (false)

    fun getNotifications(isMore: Boolean=false) {
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = NotificationRepository().getNotifications(getNotificationsRequest)
            isLoadingFlag.value = false
            if (apiResponse.code()==200){
                val getNotificationsResponse: NotificationDto.Companion.GetNotificationsResponse = apiResponse.body()?: NotificationDto.Companion.GetNotificationsResponse(
                    arrayListOf()
                )

                if (isMore){
                    val copyList = _notificationList.value?.toMutableList()
                    copyList?.addAll(getNotificationsResponse.notifications)
                    copyList?.let {
                        _notificationList.value = copyList
                    }
                }else{
                    _notificationList.value = getNotificationsResponse.notifications
                }


                if(getNotificationsResponse.notifications.isNotEmpty()){
                    getNotificationsRequest.endNotificationId = getNotificationsResponse.notifications.last().notificationId
                }else{
                    _toastMessage.value = "No more notifications"
                }


            }
        }
    }

    fun refreshNotifications(){
        getNotificationsRequest.endNotificationId = null
        getNotifications()
    }
}