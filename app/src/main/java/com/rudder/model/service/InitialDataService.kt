package com.rudder.model.service

import com.rudder.model.dto.InitialDataDtoArround
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface InitialDataService {
    @GET("/initial-data")
    suspend fun getInitialData():Response<InitialDataDtoArround>
}