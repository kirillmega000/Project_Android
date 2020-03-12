package com.example.company.maina;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

class MyLocationListener implements LocationListener {

    static Location imHere; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.
    static LocationManager locationManager;
    public static void SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    { try {
        locationManager= (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10*1000 ,
                10,
                locationListener); // здесь можно указать другие более подходящие вам параметры
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10*1000 ,
                10,
                locationListener);

        imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }catch (SecurityException e)
    {e.printStackTrace();}
    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere = loc;
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}