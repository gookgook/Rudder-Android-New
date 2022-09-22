package com.rudder.model

import com.rudder.config.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitS3Client {
    private lateinit var retrofit: Retrofit
    private lateinit var loggingInterceptor: HttpLoggingInterceptor
    private lateinit var addHeaderInterceptor: Interceptor
    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(RetrofitS3Client.loggingInterceptor)
            .addInterceptor(RetrofitS3Client.addHeaderInterceptor)
            .addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", "Android")
                chain.proceed(requestBuilder.build())
            }.build()
    }
    fun getClient(): Retrofit {
        if ( !this::retrofit.isInitialized) {
            RetrofitS3Client.loggingInterceptor = HttpLoggingInterceptor()
            RetrofitS3Client.loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            RetrofitS3Client.addHeaderInterceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val newRequest = chain.request().newBuilder()
                        .build()
                    return chain.proceed(newRequest)
                }

            }

            RetrofitS3Client.retrofit = Retrofit.Builder()
                .baseUrl("https://rudder-test-image-bucket.s3.ap-northeast-2.amazonaws.com/")
                .client(RetrofitS3Client.client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return RetrofitS3Client.retrofit
    }
}