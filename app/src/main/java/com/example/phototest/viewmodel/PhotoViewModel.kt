package com.example.phototest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.phototest.paging.PagingSourceImg
import com.example.phototest.model.PhotoResponse
import com.example.phototest.repository.PhotoRepository
import com.example.phototest.utils.API_KEY
import com.example.phototest.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(val photoRepository: PhotoRepository,private val searchTerm: String? = null) :ViewModel() {
    private val _photoList = MutableStateFlow<DataStatus<PhotoResponse>>(DataStatus.Loading())
    val photoList = _photoList

    var photolist = Pager(PagingConfig(1)) {
        PagingSourceImg(photoRepository)
    }.flow.cachedIn(viewModelScope)



    private val _photoListLiveData = MutableLiveData<DataStatus<PhotoResponse>>()
    val photoListLiveData: LiveData<DataStatus<PhotoResponse>> = _photoListLiveData

    // Bu işlev, API'yi çağırır ve sonuçları LiveData listesine ekler
    fun searchPhotos(query: String, page: Int, perPage: Int, auth: String) {
        viewModelScope.launch {
            val result = photoRepository.photoSearch(query, page, perPage, auth)
            _photoListLiveData.value = result
        }
    }
}