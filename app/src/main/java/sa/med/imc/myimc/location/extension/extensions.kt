package sa.med.imc.myimc.location.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import sa.med.imc.myimc.R
import kotlin.coroutines.suspendCoroutine


fun Location.toLatLng() = LatLng(this.latitude,this.longitude)

fun GoogleMap.moveCamera(latLng: LatLng,zoom:Float = 18f) = moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))

fun GoogleMap.addMarker(bitmapDescriptor: BitmapDescriptor,latLng: LatLng) {
    val options  = MarkerOptions().let {
        it.position(latLng)
        it.icon(bitmapDescriptor)
        it
    }
    addMarker(options)
}

fun Context.bitmapDescriptorFromAsset(vectorResId:Int) : BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

suspend fun LatLng.toAddress(context: Context) = suspendCoroutine<String> {
    val geocoder = Geocoder(context)
    if (geocoder==null){
        it.resumeWith(Result.success(""))
        return@suspendCoroutine
    }
    val result = geocoder.getFromLocation(latitude,longitude,5)
    if (result.isNullOrEmpty())
        it.resumeWith(Result.success(""))
    else
        it.resumeWith(Result.success(result[0].getAddressLine(0)))
}

fun Context.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}