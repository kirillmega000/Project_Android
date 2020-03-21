package com.example.company.maina

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaSession2Service
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {
   var frag:String="home";
    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override  fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_home -> {
                    frag="home"
                    loadFragment(HomeFragment.newInstance())
                    return true
                }
                R.id.navigation_dashboard -> {
                    frag="dash"
                    loadFragment(DashboardFragment.newInstance())
                    return true
                }
                R.id.navigation_notifications -> {
                    frag="note"
                    loadFragment (NotificationsFragment.newInstance())
                    return true
                }
            }
            return false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_content, fragment)
        ft.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkNeededPermissions()
        MyLocationListener.SetUpLocationListener(this)
        val navigation :BottomNavigationView= findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(HomeFragment.newInstance())


    }
    private fun checkNeededPermissions() {
        println("Requesting permission")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED)  {
            println("Requesting permission")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.INTERNET ,Manifest.permission.BLUETOOTH ), 0)
        }
    }



     override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
         if(keyCode==KeyEvent.KEYCODE_HEADSETHOOK||keyCode==KeyEvent.KEYCODE_MEDIA_PLAY||keyCode==KeyEvent.KEYCODE_MEDIA_PAUSE){
             Log.d("ENTERED","head")

                if(frag.equals("home")) {
                    var b: FloatingActionButton = findViewById(R.id.fab_start_recording)
                    b.callOnClick()
                }
         }
         return super.onKeyDown(keyCode, event)
     }

}