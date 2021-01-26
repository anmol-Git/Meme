package com.example.meme

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File

class photoActivity : AppCompatActivity(), IImageClick {
    private lateinit var mAdapter: GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        recyclerView.layoutManager=StaggeredGridLayoutManager(2,1)
         mAdapter =GridAdapter(this)
        recyclerView.adapter=mAdapter
        val allImage =loadImageFromInternalStorage()
        if (allImage.size==1){
            title="Saved Meme"
        }else{
            title="Saved Memes"
        }
        mAdapter.updateImage(allImage)

    }
    companion object {
        fun loadImageFromInternalStorage(): ArrayList<Bitmap> {
            val allImage = ArrayList<Bitmap>()
            val directory = File("/data/user/0/com.example.meme/app_imageDir")
            val files = directory.listFiles()

            for (element in files) {
                val imgFile = File("/data/user/0/com.example.meme/app_imageDir/" + element.name)
                Log.d("name of element", "${element.name}")
                if (imgFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    allImage.add(bitmap)
                }
            }
            Log.d("the length ", "${allImage.size}")
            return allImage
        }
    }
    override fun onItemClick(item: Bitmap) {
             val intent = Intent(this,ImageActivity::class.java)
               //intent.putExtra("key",item)
               startActivity(intent)
    }


}