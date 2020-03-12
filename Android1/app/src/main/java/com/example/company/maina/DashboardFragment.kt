package com.example.company.maina

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.net.InetAddress
import android.net.NetworkInfo
import android.support.v4.content.ContextCompat.getSystemService




class DashboardFragment : Fragment() {
     var prefs:SharedPreferences?=null
    private lateinit var locationManager:LocationManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    companion object {

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
         prefs=this.context?.getSharedPreferences("NamePrefs", Context.MODE_PRIVATE)

        locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        var locationListener:LocationListener=object:LocationListener{
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                checkEnabled()
                 }
            override fun onLocationChanged(location: Location) {
                checkEnabled()
            }

            override fun onProviderEnabled(provider: String?) {
                checkEnabled()
            }

            override fun onProviderDisabled(provider: String?) {
                checkEnabled()
            }
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10.toFloat(), locationListener)
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10.toFloat(),
                    locationListener)
        } catch (e:SecurityException){
            e.printStackTrace()
        }
        checkEnabled()
        but_geo_set.setOnClickListener { startActivity( Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        but_change.setOnClickListener {
            val intent=Intent(context,AuthActivity::class.java)
            startActivityForResult(intent,1)

        }
        CheckInternet()
        but_net_set.setOnClickListener { startActivity(Intent(
                android.provider.Settings.ACTION_WIRELESS_SETTINGS
        )) }

    }
    fun CheckInternet(){
        //улучшить
        Log.d("CheckInet","enter")
       if(internet.isInternetAvailable(context?:return)){
           Net_en.setText("Enabled")

       }
        else Net_en.setText("Disabled")
    }




    override fun onStart() {
        super.onStart()
        if(name_view.text.isEmpty())
        name_view.text=prefs?.getString("name","Данные не введены")?:return
    }
    private fun checkEnabled() {

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            GPS_en?.setText("Enabled")?:return
        else GPS_en?.setText("Disabled")?:return

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data==null)return
        if(resultCode==1){
            name_view.text=data.getStringExtra("name")
            prefs?.edit()?.putString("name","${name_view.text}")?.apply()?:return
        }
    }


}