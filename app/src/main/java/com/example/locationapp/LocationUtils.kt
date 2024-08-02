package com.example.locationapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationUtils(val context: Context) {
    fun hasLocationPermission(context: Context):Boolean{
            return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}