package sa.med.imc.myimc.location.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

internal typealias OnLocation = (locationHelper:LocationHelper,latLng:LatLng) -> Unit

interface LocationHelper {

    fun onCreate()

    fun getRequest () : LocationSettingsRequest

    fun requestLocation()

    fun removeLocationUpdate()

    companion object {
        fun get(context: Context , onLocation : OnLocation = {h , l -> }) : LocationHelper = LocationHelperImpl(context,onLocation)
    }
}

internal class LocationHelperImpl constructor(private val context: Context, private val onLocation : OnLocation ={h , l -> }) : LocationHelper {
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastKnownLocation : LatLng
    private val CODE = 386
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate() {
        client = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.interval = 1000;
        locationRequest.fastestInterval = 500;
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        builder =  LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult:LocationResult) {
                lastKnownLocation = locationResult.lastLocation.toLatLng()
                onLocation(this@LocationHelperImpl,lastKnownLocation)
            }
        }

    }

    override fun getRequest(): LocationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

    override fun requestLocation() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  ){
                return
            }
        }
       client.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    override fun removeLocationUpdate() {
       client.removeLocationUpdates(locationCallback)
    }


}