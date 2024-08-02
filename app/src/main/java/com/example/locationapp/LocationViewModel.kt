package com.example.locationapp

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State


class LocationViewModel: ViewModel() {
    private val _locationData = mutableStateOf<LocationData?>(null)
    val locationData: State<LocationData?> = _locationData

    fun updateLocationData(locationData: LocationData) {
        _locationData.value = locationData
    }
}