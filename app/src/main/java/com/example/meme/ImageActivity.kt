package com.example.meme

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.meme.GridAdapter.Companion.index
import com.example.meme.photoActivity.Companion.loadImageFromInternalStorage
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class ImageActivity : AppCompatActivity(), IImageClick {

    val gridAdapter =GridAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val images = loadImageFromInternalStorage()
        imageView.setImageBitmap(images[index])
         shareButton.setOnClickListener {
            val uri= getLocalBitmapUri(images[index])
             val intent= Intent(Intent.ACTION_SEND)
             intent.type="image/*"
             intent.putExtra(Intent.EXTRA_STREAM, uri)
             val chooser = Intent.createChooser(intent,"Share this meme using...")
             startActivity(chooser)
         }
         deleteButton.setOnClickListener {
             val directory = File("/data/user/0/com.example.meme/app_imageDir")
             val files = directory.listFiles()
             files[index].delete()
             gridAdapter.updateImage(loadImageFromInternalStorage())
         }
    }

    fun  getLocalBitmapUri(bitm: Bitmap) : Uri? {
        var bitmap : Uri? =null
        try{
            val strictMode= StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(strictMode.build())
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"share meme"+System.currentTimeMillis()+".png")
            val out = FileOutputStream(file)
            bitm.compress(Bitmap.CompressFormat.PNG,100,out)
            out.close()
            bitmap= Uri.fromFile(file)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return bitmap
    }

    override fun onItemClick(item: Bitmap) {
        TODO("Not yet implemented")
    }
}