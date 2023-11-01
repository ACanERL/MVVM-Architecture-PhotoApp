package com.example.phototest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.phototest.databinding.RecyclerItemBinding
import com.example.phototest.model.PhotoResponse

class SearchAdapter:RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private lateinit var binding: RecyclerItemBinding
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        binding= RecyclerItemBinding.inflate(inflater,parent,false)
        context=parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
       holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int=differ.currentList.size

    inner class ViewHolder:RecyclerView.ViewHolder(binding.root){
        fun bind(item: PhotoResponse.Photo){
            binding.apply {

                Glide.with(itemView)
                    .load(item.src.portrait
                    )
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(wallpaperDisplay)
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<PhotoResponse.Photo>() {
        override fun areItemsTheSame(oldItem: PhotoResponse.Photo, newItem: PhotoResponse.Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoResponse.Photo, newItem: PhotoResponse.Photo): Boolean {
            return oldItem == newItem
        }
    }
    val differ= AsyncListDiffer(this,differCallback)


}