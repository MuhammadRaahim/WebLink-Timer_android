package com.example.timmer.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.timmer.databinding.ActivityWebViewBinding
import com.example.timmer.utils.Constants.Companion.ADDRESS
import com.example.timmer.utils.Constants.Companion.LINK
import com.example.timmer.utils.Constants.Companion.POINTS
import io.paperdb.Paper


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var linkId: String
    private lateinit var linkAddress: String
    private var statTime = 0L
    private var endTime = 0L
    var points: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

    }

    override fun onResume() {
        super.onResume()
        statTime = System.currentTimeMillis()
        calculatePoints()
    }

    private fun calculatePoints() {
        val handler: Handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                    points += 1
                    handler.postDelayed(this, 60000)
                    finishActivity()

            }
        }, 60000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveLinkDuration()
        savePoints()
    }

    private fun saveLinkDuration() {
        endTime = System.currentTimeMillis()
        var duration  = (endTime - statTime)/1000
        if (Paper.book().contains(linkId)){
            val xDuration:Long = Paper.book().read(POINTS)!!
            val tDuration = xDuration + duration
            Paper.book().write(linkId, tDuration)
        }else{
            Paper.book().write(linkId, duration)
        }
    }

    fun  finishActivity(){
        if (points == 2){
            saveLinkDuration()
            savePoints()
            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }


    private fun savePoints() {
        if (Paper.book().contains(POINTS)){
            val xPoints:Int = Paper.book().read(POINTS)!!
            val tPoints = xPoints + points
            Paper.book().write(POINTS, tPoints)
        }else{
            Paper.book().write(POINTS, points)
        }
    }

    override fun onPause() {
        super.onPause()
        saveLinkDuration()
        savePoints()
    }

    private fun initViews() {
        linkId = intent.getStringExtra(LINK)!!
        linkAddress = intent.getStringExtra(ADDRESS)!!

        if (linkAddress.isNotEmpty()){
            setWebView(linkAddress)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView(url: String) {
        binding.progressLayout.isVisible = true
        val webView = binding.webView
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadUrl(url)

        val webViewClient: WebViewClient = object: WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressLayout.isVisible = true
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressLayout.isVisible = false
                super.onPageFinished(view, url)
            }
        }
        webView.webViewClient = webViewClient
    }
}