package com.rudder.mvvm.Model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EmailValidateService {

    @GET("/email/{userEmail}/validate")
    suspend fun getEmailValidateResult(@Path("userEmail") userEmail:String): Response<Void>
}