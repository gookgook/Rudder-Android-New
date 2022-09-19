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

    fun getNotifications(isMore: Boolean=false) {
        RetrofitClient.updateAuthToken("eyJhbGciOiJIUzUxMiJ9.eyJwcm9tb3Rpb25NYWlsQWdyZWVtZW50IjpmYWxzZSwiYXV0aCI6IlJPTEVfVVNFUiIsInNjaG9vbCI6eyJzY2hvb2xJZCI6NCwic2Nob29sTmFtZSI6Im5hdmVyIiwicmVnZXgiOiJtaHBhcmswMjIwQG5hdmVyLmNvbSJ9LCJ1c2VyTmlja25hbWUiOiJhZG1pbm0iLCJ1c2VyRW1haWwiOiJtaHBhcmswMjIwQG5hdmVyLmNvbSIsInVzZXJUeXBlIjowLCJ1c2VySWQiOiJtaHBhcmswMjIwQG5hdmVyLmNvbSIsInVzZXJJbmZvSWQiOjM0Nywibm90aWZpY2F0aW9uVG9rZW4iOiJyaWdodENhc2UifQ.c5ByggYQazfIK1tf0TvFf7Zg3VH4nWoQtX3o_9DV9rSa9uovGC9G4Bd9O92CagFsl10DLjLeNiV8dqbFxzMSfg")//삭제예정
        viewModelScope.launch {
            val apiResponse = NotificationRepository.instance.getNotifications(getNotificationsRequest)
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