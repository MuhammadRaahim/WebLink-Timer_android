package com.example.timmer.utils

import android.content.Context
import android.content.Intent
import com.example.timmer.activities.WebViewActivity
import com.example.timmer.utils.Constants.Companion.ADDRESS
import com.example.timmer.utils.Constants.Companion.LINK
import java.util.concurrent.TimeUnit

class BaseUtils {
    companion object{

        fun sendUrl(context: Context, id: String, url: String){
            val intent = Intent(context,WebViewActivity::class.java)
            intent.putExtra(LINK,id)
            intent.putExtra(ADDRESS,url)
            context.startActivity(intent)
        }

        fun getDurationBreakdown(millis: Long): String? {
            var millis = millis
            require(millis >= 0) { "Duration must be greater than zero!" }
            val days: Long = TimeUnit.MILLISECONDS.toDays(millis)
            millis -= TimeUnit.DAYS.toMillis(days)
            val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
            millis -= TimeUnit.HOURS.toMillis(hours)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millis)
            millis -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millis)
            val sb = StringBuilder(64)
            sb.append(days)
            sb.append(" Days ")
            sb.append(hours)
            sb.append(" Hours ")
            sb.append(minutes)
            sb.append(" Minutes ")
            sb.append(seconds)
            sb.append(" Seconds")
            return sb.toString()
        }


    }
}