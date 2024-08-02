package com.example.locationapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel: LocationViewModel = viewModel()
            LocationAppTheme {

                Surface {
                    MyApp(viewModel)
                }
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    LocationDisplay(modifier = Modifier.padding(innerPadding))
//                }
            }
        }
    }
}


@Composable
fun MyApp(viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(viewModel = viewModel,locationUtils = locationUtils, context = context)
}

@Composable
fun LocationDisplay(
    viewModel: LocationViewModel,
    locationUtils: LocationUtils,
    context: Context
){

    val location = viewModel.locationData.value

    val addresses = location?.let {
        locationUtils.reverseGeocode(it)
    }

    val requestPermissionLuncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->

            if( permissions[ACCESS_FINE_LOCATION] == true &&
                permissions[ACCESS_COARSE_LOCATION] == true ){



                //acess granted

                locationUtils.requestLocationUpdates(viewModel = viewModel)

            }else{
                //request permission
                val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    ACCESS_COARSE_LOCATION
                )

                if(rationalRequired){
                    Toast.makeText(context,"Location permission required", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    Toast.makeText(context,"Location permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){


        if(location != null){
            Text(text = "Latitude: ${location.latitude}")
            Text(text = "Longitude: ${location.longitude}")
            Text(text = "${addresses}")
        }else{
            Text(text = "Location not available")
        }
        Button(onClick = {
            if (locationUtils.hasLocationPermission(context)){
                // Permissions granted already

                locationUtils.requestLocationUpdates(viewModel = viewModel)

            }else{
                // request location permission
                requestPermissionLuncher.launch(arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                ))
            }

        }) { Text(text = "Get Location") }
    }
}

