package com.example.phototest.repository

import com.example.phototest.api.ApiServices
import com.example.phototest.model.PhotoResponse
import com.example.phototest.utils.DataStatus
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val apiServices: ApiServices) {
 suspend   fun getPhoto(page: Int, perPage: Int, auth: String): DataStatus<PhotoResponse> {
        val response = apiServices.getCuratedPhotos(page, perPage, auth)
        return if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                DataStatus.Success(data)
            } else {
                DataStatus.Error("No data available")
            }
        } else {
            DataStatus.Error("API request failed")
        }
    }
    suspend fun photoSearch(query:String,page: Int, perPage: Int, auth: String): DataStatus<PhotoResponse> {
        val response=apiServices.photoSearch(query,page,perPage,auth)
        return if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                DataStatus.Success(data)
            } else {
                DataStatus.Error("No data available")
            }
        } else {
            DataStatus.Error("API request failed")
        }
    }
}