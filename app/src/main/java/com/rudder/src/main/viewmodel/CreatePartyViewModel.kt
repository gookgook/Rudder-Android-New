package com.rudder.src.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitS3Client
import com.rudder.model.dto.ImageMetaData
import com.rudder.model.repository.PartyRepository
import com.rudder.model.service.PhotoUploadService
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.sql.Timestamp
import java.util.*

class CreatePartyViewModel : ViewModel() {

    companion object{
        private const val FIELD_EMPTY_ERROR = "One or more fields are empty"
    }

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    val partyTitle: MutableLiveData<String> = MutableLiveData()
    val partyDescription: MutableLiveData<String> = MutableLiveData()
    val partyLocation: MutableLiveData<String> = MutableLiveData()

    private val partyCalendar: Calendar = Calendar.getInstance()
    private var partyMemberCount: Int? = null
    private var imageForUpload: PartyDto.Companion.ImageForUpload? = null

    fun postParty() {

        if(imageForUpload==null){
            _toastMessage.value=FIELD_EMPTY_ERROR
            return
        }

        val postPartyRequest = PartyDto.Companion.PostPartyRequest(
            partyTitle = partyTitle.value ?: let {
                _toastMessage.value = FIELD_EMPTY_ERROR
                return
            },
            partyDescription = partyDescription.value ?: let {
                _toastMessage.value = FIELD_EMPTY_ERROR
                return
            },
            partyTime = Timestamp(partyCalendar.timeInMillis),
            location = partyLocation.value ?: let {
                _toastMessage.value = FIELD_EMPTY_ERROR
                return
            },
            totalNumberOfMember = partyMemberCount ?: let {
                _toastMessage.value = FIELD_EMPTY_ERROR
                return
            },
        )

        viewModelScope.launch {
            val createPartyApiResponse = PartyRepository.instance.createParty(postPartyRequest)
            if (createPartyApiResponse.isSuccessful){
                val postPartyResponse = createPartyApiResponse.body() ?: return@launch
                val getPartyImageUploadUrlRequest =
                    PartyDto.Companion.GetPartyImageUploadUrlRequest(
                        imageMetaData = imageForUpload!!.imageMetaData,
                        partyId = postPartyResponse.partyId
                    )
                val getImageUploadUrlApiResponse =
                    PartyRepository.instance.getPartyImageUploadUrl(getPartyImageUploadUrlRequest)

                if (getImageUploadUrlApiResponse.isSuccessful){
                    val getPartyImageUploadUrlResponse = getImageUploadUrlApiResponse.body() ?: return@launch

                    val requestBody = RequestBody.create(
                        MediaType.parse(imageForUpload!!.imageMetaData.contentType),
                        imageForUpload!!.file
                    )

                    RetrofitS3Client.getClient().create(PhotoUploadService::class.java).uploadPhoto(
                        getPartyImageUploadUrlResponse.urls[0],requestBody
                    )

                }else{
                    _toastMessage.value = "Fail to upload party image"
                    return@launch
                }
            }else{
                _toastMessage.value = "Network error"
                return@launch
            }
        }


    }

    fun setPartyMemberCount(count: Int) {
        partyMemberCount = count
    }

    fun setCalendarTime(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        partyCalendar.set(year, monthOfYear, dayOfMonth)
    }

    fun setCalendarTime(hourOfDay: Int, minute: Int) {
        partyCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        partyCalendar.set(Calendar.MINUTE, minute)
    }

    fun setImage(uri: File, contentType: String) {
        imageForUpload = PartyDto.Companion.ImageForUpload(
            file = uri,
            ImageMetaData(contentType = contentType, fileName = "mock")
        )
    }



}