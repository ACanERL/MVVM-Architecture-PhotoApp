package com.example.phototest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.phototest.databinding.RecyclerItemBinding
import com.example.phototest.model.PhotoResponse
import javax.inject.Inject
class PhotoAdapter @Inject() constructor(private var photoOnClick: (PhotoResponse.Photo) -> Unit):PagingDataAdapter<PhotoResponse.Photo,PhotoAdapter.ViewHolder>(differCallback) {
    private lateinit var binding: RecyclerItemBinding
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.ViewHolder {
       val inflater=LayoutInflater.from(parent.context)
        binding= RecyclerItemBinding.inflate(inflater,parent,false)
        context=parent.context
        return ViewHolder()
    }
    override fun onBindViewHolder(holder: PhotoAdapter.ViewHolder, position: Int) {
            holder.bind(getItem(position)!!)
            holder.setIsRecyclable(false)
    }
    inner class ViewHolder():RecyclerView.ViewHolder(binding.root){
                fun bind(item: PhotoResponse.Photo){
                    binding.apply {

                        Glide.with(itemView)
                            .load(item.src.portrait
                            )
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(wallpaperDisplay)

                      binding.root.setOnClickListener {
                         photoOnClick(item)
                      }
                    }
                }
    }
    companion object {
        val differCallback = object : DiffUtil.ItemCallback<PhotoResponse.Photo>() {
            override fun areItemsTheSame(oldItem: PhotoResponse.Photo, newItem: PhotoResponse.Photo): Boolean {
                return oldItem.id == oldItem.id
            }
            override fun areContentsTheSame(oldItem: PhotoResponse.Photo, newItem: PhotoResponse.Photo): Boolean {
                return oldItem == newItem
            }
        }
    }

}