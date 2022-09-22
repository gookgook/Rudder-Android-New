package com.rudder.model.service

import com.rudder.model.dto.ImageUrlRequestInfo
import com.rudder.model.dto.ImageUrlRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageUrlRequestService {
    @POST("/party-profile-image/image-upload-url/generate")
    suspend fun getImageUrls(
        @Body imageUrlRequestInfo: ImageUrlRequestInfo,
    ):Response<ImageUrlRequestResponse>
}