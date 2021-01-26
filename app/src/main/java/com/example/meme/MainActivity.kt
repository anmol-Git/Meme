package com.example.meme

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var path : String
    private var currentImageUrl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
        viewSaved.setOnClickListener {
            val intent =Intent(this,photoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadmeme(){
// Instantiate the RequestQueue.
        progress.visibility=View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        //request start from here
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            {response ->
            currentImageUrl=response.getString("url")
                Glide.with(this).asBitmap().load(currentImageUrl).into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        progress.visibility=View.GONE
                        grid_view.setImageBitmap(resource)
                        save.setOnClickListener {
                            saveImageToInternalStorage(resource)
                              Snackbar.make(mainActivity,"Image saved",Snackbar.LENGTH_LONG).setAction("Okay"){}.show()
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                           grid_view.setImageDrawable(placeholder)
                    }

                })
            },
            {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            })  //request end here it have 5 param 
       MySingleton.getInstance(this).addToRequestQueue(jsonRequest)


        Sharebutton.setOnClickListener {
            shareMeme()
        }

    }

    private fun shareMeme(){
        Picasso.get().load(currentImageUrl).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val intent=Intent(Intent.ACTION_SEND)
                intent.type="image/*"
                intent.putExtra(Intent.EXTRA_STREAM, bitmap?.let { getLocalBitmapUri(it) })
                val chooser = Intent.createChooser(intent,"Share this meme using...")
                startActivity(chooser)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) { }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) { }

        })
    }

    fun  getLocalBitmapUri(bitm: Bitmap) : Uri? {
         var bitmap :Uri? =null
         try{
             val strictMode= StrictMode.VmPolicy.Builder()
             StrictMode.setVmPolicy(strictMode.build())
             val file =File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"share meme"+System.currentTimeMillis()+".png")
             val out =FileOutputStream(file)
             bitm.compress(Bitmap.CompressFormat.PNG,100,out)
             out.close()
             bitmap= Uri.fromFile(file)
         }catch (e:IOException){
              e.printStackTrace()
         }
        return bitmap
    }

   fun saveImageToInternalStorage(bitmap: Bitmap) : String{
       Log.d("in","the save function")
       val contextWrapper = ContextWrapper(applicationContext)
       val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
       val myPath = File(directory,System.currentTimeMillis().toString() +".jpg" )
       var fos :FileOutputStream? =null
       try{
           fos = FileOutputStream(myPath)
           bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
       }catch (e :Exception){
           e.printStackTrace()
       }finally {
           try {
               if (fos != null) {
                   fos.close()
               }
           } catch (e : IOException){
               e.printStackTrace()
           }
       }
       path = directory.absolutePath
       Log.d("path is --->",path)
       return path
   }

    fun nextMeme(view: View) {
      loadmeme()
    }



}


