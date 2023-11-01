package com.example.phototest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.phototest.databinding.ActivityMainBinding

import com.example.phototest.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.phototest.adapter.LoadMoreAdapter
import com.example.phototest.adapter.PhotoAdapter
import com.example.phototest.adapter.SearchAdapter
import com.example.phototest.model.PhotoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private val photoViewModel:PhotoViewModel by viewModels()
    private val photosAdapter by lazy { PhotoAdapter(photoOnClick=::photoOnClick) }
    private val searchAdapter:SearchAdapter by  lazy { SearchAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        lifecycleScope.launchWhenCreated {
            setupRecyclerView()
            photoViewModel.photolist.collect{
                photosAdapter.submitData(it)
                photosAdapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launchWhenCreated {
            photosAdapter.loadStateFlow.collect{
                val state = it.refresh
                binding!!.progressBar.isVisible= state is LoadState.Loading
            }
        }
        binding!!.recyclerView.adapter=photosAdapter.withLoadStateFooter(
            LoadMoreAdapter{
                photosAdapter.retry()
            }
        )

        binding!!.editTextText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString().trim()
                val filteredData: Flow<PagingData<PhotoResponse.Photo>> = filterData(photoViewModel.photolist, searchTerm)
                lifecycleScope.launch {
                    photosAdapter.submitData(PagingData.empty())
                    filteredData.collect { pagingData ->
                        photosAdapter.submitData(pagingData)
                        photosAdapter.notifyDataSetChanged()
                    }
                }

            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    private fun setupRecyclerView() {
            binding!!.recyclerView.apply {
            binding!!.recyclerView.setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = photosAdapter
        }
    }

    private fun setupRecyclerView2() {
        binding!!.recyclerView.apply {
            binding!!.recyclerView.setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = searchAdapter
        }
    }

    private fun filterData(data: Flow<PagingData<PhotoResponse.Photo>>, searchItem: String): Flow<PagingData<PhotoResponse.Photo>> {
        return data.map { pagingData ->
            pagingData.filter {
                it.alt.trim() .contains(searchItem, ignoreCase = true)
                it.avg_color.contains(searchItem, ignoreCase = true)
                it.photographer.contains(searchItem, ignoreCase = true)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun photoOnClick(photo: PhotoResponse.Photo){
        val intent = Intent(this, PhotoDetail::class.java)
        intent.putExtra("key", photo.src.portrait)
        startActivity(intent)
    }
}