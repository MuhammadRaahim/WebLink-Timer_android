package com.example.timmer.utils

import android.content.Context
import android.content.Intent
import com.example.timmer.activities.WebViewActivity
import com.example.timmer.utils.Constants.Companion.ADDRESS
import com.example.timmer.utils.Constants.Companion.LINK

class BaseUtils {
    companion object{

        fun sendUrl(context: Context, id: String, url: String){
            val intent = Intent(context,WebViewActivity::class.java)
            intent.putExtra(LINK,id)
            intent.putExtra(ADDRESS,url)
            context.startActivity(intent)
        }


    }
}