package com.example.flobizhackathon

import com.example.flobizhackathon.model.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Url

interface ApiService {
private val url: String
    get() = "https://api.stackexchange.com/2.2/questions?key=ZiXCZbWaOwnDgpVT9Hx8IA((&order=desc&sort=activity&site=stackoverflow"

    @GET()
    suspend fun getData(@Url patch: String=url):Response
}