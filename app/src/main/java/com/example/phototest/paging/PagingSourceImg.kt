package com.example.phototest.paging

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Build
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.phototest.api.ApiServices
import com.example.phototest.model.PhotoResponse
import com.example.phototest.repository.PhotoRepository
import com.example.phototest.utils.API_KEY
import java.io.IOException

class PagingSourceImg(val photoRepository: PhotoRepository,private val searchTerm: String? = null): PagingSource<Int, PhotoResponse.Photo>(){
    override fun getRefreshKey(state: PagingState<Int, PhotoResponse.Photo>): Int? {
        return null
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse.Photo> {
        val responseData = mutableListOf<PhotoResponse.Photo>()
        val searchData=mutableListOf<PhotoResponse.Photo>()
       return if (Build.VERSION.SDK_INT >= 24) {
           try{
               val currentPage = params.key ?: 1

               if (searchTerm != null) {
                  val searchResponse=photoRepository.photoSearch(searchTerm, currentPage, 10, API_KEY)
                   val data= searchResponse.data!!.photos
                   responseData.addAll(data)
                   LoadResult.Page(
                       data = searchData,
                       prevKey = if (currentPage == 1) null else -1,
                       nextKey = currentPage.plus(1)
                   )
               }
              else{
                   val response=photoRepository.getPhoto(currentPage,10, API_KEY)
                   val data= response.data!!.photos
                   responseData.addAll(data)

                   LoadResult.Page(
                       data = responseData,
                       prevKey = if (currentPage == 1) null else -1,
                       nextKey = currentPage.plus(1)
                   )
               }
           }
           catch (e: Exception) {
               LoadResult.Error(e)
           } catch (exception: HttpException) {
               LoadResult.Error(exception)
           }
       } else {
           TODO("VERSION.SDK_INT < 34")
       }
    }

}