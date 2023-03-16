package com.example.memeapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.memeapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url:String = "https://meme-api.com/gimme"
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root);

        getMemeData()

        binding.btnNewMeme.setOnClickListener{
            getMemeData()
        }
    }

    fun getMemeData(){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait while data is fetching")
        progressDialog.show()
// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.e("Response","getMemeData: "+response.toString())

                var responseObject= JSONObject(response)

                //responseObject.get("title")

                responseObject.get("postLink")
                responseObject.get("author")

                binding.memeTitle.text=responseObject.getString("title")
                binding.memeAuthor.text=responseObject.getString("author")
                Glide.with(this).load(responseObject.get("url")).into(binding.memeImage)
                progressDialog.dismiss()
            },
            { error ->
                progressDialog.dismiss()
                Toast.makeText(this@MainActivity,"${error.localizedMessage}",Toast.LENGTH_SHORT).show();
            })

        queue.add(stringRequest)

    }

    fun shareMeme(view: android.view.View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this new meme")
        val chooser = Intent.createChooser(intent,"share this meme using")
        startActivity(chooser)
    }
}