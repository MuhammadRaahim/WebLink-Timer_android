package com.example.timmer.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timmer.R
import com.example.timmer.databinding.ActivityMainBinding
import com.example.timmer.utils.BaseUtils.Companion.sendUrl
import com.example.timmer.utils.Constants.Companion.ADDRESS_FOUR
import com.example.timmer.utils.Constants.Companion.ADDRESS_ONE
import com.example.timmer.utils.Constants.Companion.ADDRESS_THREE
import com.example.timmer.utils.Constants.Companion.ADDRESS_TWO
import com.example.timmer.utils.Constants.Companion.LINK_FOUR
import com.example.timmer.utils.Constants.Companion.LINK_ONE
import com.example.timmer.utils.Constants.Companion.LINK_THREE
import com.example.timmer.utils.Constants.Companion.LINK_TWO
import com.example.timmer.utils.Constants.Companion.POINTS
import com.google.android.material.navigation.NavigationView
import io.paperdb.Paper
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        binding.apply {
            if (Paper.book().contains(POINTS)) {
                val points:Int = Paper.book().read(POINTS)!!
                tvPoints.text = points.toString()
            }
            if (Paper.book().contains(LINK_ONE)) {
                setDuration(LINK_ONE,tvLinkOneTime)
            }
            if (Paper.book().contains(LINK_TWO)) {
                setDuration(LINK_TWO,tvLinkTwoTime)
            }
            if (Paper.book().contains(LINK_THREE)) {
                setDuration(LINK_THREE,tvLinkThreeTime)
            }
            if (Paper.book().contains(LINK_FOUR)) {
                setDuration(LINK_FOUR,tvLinkFourTime)
            }
        }
    }

    private fun setDuration(linkId: String, tv: TextView) {
        var duration:Long = Paper.book().read(linkId)!!
        if (duration < 60){
            tv.text = duration.toString().plus(" s")
        }else {
            duration /= 60
            tv.text = duration.toString().plus(" min")
        }
    }

    private fun setClickListeners() {
        binding.apply {
            toolbar.ivDrawer.setOnClickListener {
                drawer.openDrawer(GravityCompat.END)
            }
            tvReset.setOnClickListener {
                resetData()
            }
            ivYoutube.setOnClickListener {
                val url = getString(R.string.str_youtube_link)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resetData() {
        Paper.book().destroy()
        binding.apply {
            tvPoints.text = "0"
            tvLinkOneTime.text = "0s"
            tvLinkTwoTime.text = "0s"
            tvLinkThreeTime.text = "0s"
            tvLinkFourTime.text = "0s"
        }
    }

    @SuppressLint("HardwareIds")
    private fun initViews() {
        drawer = binding.myDrawerLayout
        binding.navigation.setNavigationItemSelectedListener(this)
        binding.tvDeviceId.text =  "ID: ".plus(Settings.Secure.getString(this.contentResolver,
            Settings.Secure.ANDROID_ID))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_link_one ->{
                sendUrl(this,LINK_ONE, ADDRESS_ONE)
            }
            R.id.nav_link_two ->{
                sendUrl(this,LINK_TWO, ADDRESS_TWO)
            }
            R.id.nav_link_three ->{
                sendUrl(this, LINK_THREE, ADDRESS_THREE)
            }
            R.id.nav_link_foure ->{
                sendUrl(this, LINK_FOUR, ADDRESS_FOUR)
            }
        }
        drawer.closeDrawers()
        return true
    }

}