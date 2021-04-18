package com.inventoryapp.mobile.service

import retrofit2.Response
import retrofit2.http.GET

interface HealthCheckApi {

    @GET("api/healthcheck")
    suspend fun healthCheck(): Response<String>

}