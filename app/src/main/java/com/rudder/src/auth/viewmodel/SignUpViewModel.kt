package com.rudder.src.auth.viewmodel

import android.content.ContentResolver
import android.media.Image
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.RetrofitS3Client
import com.rudder.model.dto.*
import com.rudder.model.service.EmailValidateService
import com.rudder.model.service.ImageUrlRequestService
import com.rudder.model.service.PhotoUploadService
import com.rudder.model.service.SignUpService
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.create
import java.io.File

class SignUpViewModel: ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val userNickname = MutableLiveData<String>()
    val userDescription = MutableLiveData<String>()

    val emailValidateResultFlag = MutableLiveData<Int>()
    val profileCheckFlag = MutableLiveData<Int>()

    val isTermsAgreed = MutableLiveData<Boolean>(false)

    var profileImages = arrayOf<File>()
    var imageMetadatas = arrayOf<ImageMetaData>()

    val isLoadingFlag = MutableLiveData<Boolean> (false)

    val signUpResultFlag = MutableLiveData<Int>()

    fun onClickNext() {

        val safeUserId: String = userId.value?:run { emailValidateResultFlag.postValue(-2); return }
        val safeUserPassword: String = userPassword.value?:run {  emailValidateResultFlag.postValue(-2); return }

        if (!isTermsAgreed.value!!) {
            emailValidateResultFlag.value = 4
            return
        }

        val emailValidateService: EmailValidateService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(EmailValidateService::class.java)

        viewModelScope.launch {
            isLoadingFlag.value = true
            val emailValidateRequest: Response<Void> = emailValidateService.getEmailValidateResult(safeUserId)
            isLoadingFlag.value = false
            Log.d("emailVal",emailValidateRequest.code().toString())
            when (emailValidateRequest.code()) {
                200 -> emailValidateResultFlag.value = 1
                406 -> emailValidateResultFlag.value = 2
                409 -> emailValidateResultFlag.value = 3
                else -> emailValidateResultFlag.value = -1
            }
        }
    }

    fun onClickNext2(){
        val safeUserNickname: String = userNickname.value?:run { profileCheckFlag.value = -2; return }
        val safeUserDescription: String = userDescription.value?:run {  profileCheckFlag.value = -2; return }

        if (safeUserDescription.length < 20) { profileCheckFlag.value = -3; return }

        profileCheckFlag.value = 1
    }

    fun onClickDone() {
        val signUpService: SignUpService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(SignUpService::class.java)

        if (imageMetadatas.size < 2) {
            signUpResultFlag.value = 2
            return
        }

        viewModelScope.launch {
            isLoadingFlag.value = true

            val signUpRequest: Response<SignUpResult> = signUpService.getSignUpResult(SignUpInfo(true, userId.value!!, userPassword.value!!, userNickname.value!!, userDescription.value!! ))
            val imageUrlRequestService: ImageUrlRequestService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(ImageUrlRequestService::class.java)
            val imageUrlRequestInfo = ImageUrlRequestInfo(imageMetadatas,signUpRequest.body()!!.userInfoId)
            val imageUrlRequestRequest: Response<ImageUrlRequestResponse> = imageUrlRequestService.getImageUrls(imageUrlRequestInfo)
            val photoUploadService: PhotoUploadService = RetrofitS3Client.getClient().create(PhotoUploadService::class.java)
            for(i: Int in 0 until imageMetadatas.size){
                val tmpRequestBody = RequestBody.create(MediaType.parse(imageMetadatas[i].contentType),profileImages[i])
                if(imageUrlRequestRequest.body() == null ) Log.d("photo Upload","isNull")
                val photoUploadRequest: Response<Void> = photoUploadService.uploadPhoto(imageUrlRequestRequest.body()!!.urls[i],tmpRequestBody)
            }

            Log.d("photoUpload","Success")
            signUpResultFlag.value = 1
            isLoadingFlag.value = false
        }
    }

    fun onCLickedAgreed(){
        isTermsAgreed.value = !isTermsAgreed.value!!
    }


    fun appendImage(uri: File, contentType: String){

        Log.d("image upload", uri.toString())
        profileImages = profileImages.plus(uri)
        imageMetadatas = imageMetadatas.plus(ImageMetaData(contentType,"namename"))
    }




}