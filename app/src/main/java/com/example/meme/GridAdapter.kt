package com.example.meme

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meme.databinding.GridItemViewBinding

class GridAdapter(private val listener :IImageClick) : RecyclerView.Adapter<GridAdapter.MyViewHolder>() {

            val list = ArrayList<Bitmap>()
    companion object{
        var index = 0
    }

    class MyViewHolder(private val binding: GridItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind (item: Bitmap){
           binding.savedImages.setImageBitmap(item)
        }

        companion object {
            fun from(parent: ViewGroup) :MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =GridItemViewBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("in ","bind view")
         val currItem =list[position]
          holder.bind(currItem)
        holder.itemView.setOnClickListener {
            listener.onItemClick(currItem)
            Log.d("index is ","${holder.adapterPosition}")
            index =holder.adapterPosition
        }
    }

   fun updateImage( newList : ArrayList<Bitmap>){
        list.clear()
        list.addAll(newList)
        Log.d("list","${list.size}")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return list.size
    }
}

interface IImageClick {
    fun onItemClick(item : Bitmap)
}
