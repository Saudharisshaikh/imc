package sa.med.imc.myimc.location.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsStates
import sa.med.imc.myimc.location.extension.LocationSettingHelper.Companion.CODE


interface LocationSettingHelper {

    fun checkLocationSettings()

    fun onActivityResult(requestCode : Int, resultCode : Int , data : Intent?)

    companion object{
        const val CODE = 900
        fun get(context: Context,locationHelper: LocationHelper) : LocationSettingHelper = LocationSettingHelperImpl(context,locationHelper)
    }

}
internal class LocationSettingHelperImpl(private val context: Context, private val locationHelper: LocationHelper) : LocationSettingHelper {

    override fun checkLocationSettings() {
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(locationHelper.getRequest())
        task.addOnSuccessListener{
            locationHelper.requestLocation()
        }.addOnFailureListener {e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult((context as Activity), CODE)
                } catch (ex: SendIntentException) {
                    ex.printStackTrace()
                }
            }
        }.addOnCanceledListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        val states = LocationSettingsStates.fromIntent(data) ?: return
        if (states.isNetworkLocationUsable) {
           locationHelper.requestLocation()
        } else {
         ///denied
        }
    }

}

