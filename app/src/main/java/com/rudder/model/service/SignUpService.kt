package com.rudder.model.service

import com.rudder.model.dto.SignUpInfo
import com.rudder.model.dto.SignUpResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("/user-infos")
    suspend fun getSignUpResult(
        @Body signUpInfo: SignUpInfo
    ): Response<SignUpResult>
}