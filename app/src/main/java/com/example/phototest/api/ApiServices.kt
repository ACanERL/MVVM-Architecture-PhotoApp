package com.example.phototest.api

import com.example.phototest.model.PhotoResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServices {
    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") apikey:String
    ): Response<PhotoResponse>


    @GET("v1/search")
    suspend fun photoSearch(
        @Query("query") quer:String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") apikey:String
    ): Response<PhotoResponse>
}