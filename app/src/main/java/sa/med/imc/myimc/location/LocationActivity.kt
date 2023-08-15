package sa.med.imc.myimc.location

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sa.med.imc.myimc.Base.RootActivity
import sa.med.imc.myimc.R
import sa.med.imc.myimc.databinding.ActivityLocationBinding
import sa.med.imc.myimc.location.extension.*
import sa.med.imc.myimc.location.places.PlacesSheet
import kotlin.properties.Delegates

open class LocationActivity : RootActivity(){
    private lateinit var googleMap: GoogleMap
    private val binding : ActivityLocationBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_location)
    }
    private var address = ""
    private var lastKnownLocation : LatLng by Delegates.observable(LatLng(0.0,0.0)){property, oldValue, newValue ->
        if (!::googleMap.isInitialized){
            return@observable
        }
        googleMap.clear()
        googleMap.addMarker(this.bitmapDescriptorFromAsset(R.drawable.ic_map_location),this.lastKnownLocation)
        googleMap.moveCamera(newValue)
    }

    private val placesSheet by lazy {
        PlacesSheet.newInstance() {it,address->
            if (it != null && it.latLng != null) {
                this.address = address
                this.lastKnownLocation = it.latLng
            }
        }
    }

    private val locationHelper by lazy {
        val helper = LocationHelper.get(this){ h , l->
            h.removeLocationUpdate()
            onCreateMap{
                googleMap = it
                this.googleMap.setOnMapClickListener {
                    address = ""
                    this.lastKnownLocation = it
                }
                lastKnownLocation = l
            }

        }
        helper.onCreate()
        helper
    }

    private val locationSettingHelper by lazy {
     LocationSettingHelper.get(this,locationHelper)
    }

    companion object {
        const val DATA = "location_data"
        const val CODE = 100

        @JvmStatic
        fun startActivityForResult(fragment: Fragment,code:Int){
            fragment.startActivityForResult(Intent(fragment.context,LocationActivity::class.java),code)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.onNavigation = View.OnClickListener {
            finish()
        }
        binding.onSearch = View.OnClickListener {
            if (placesSheet.isAdded){
                return@OnClickListener
            }
            placesSheet.showNow(supportFragmentManager,placesSheet.javaClass.simpleName)
        }
        binding.onDone = View.OnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val result = if (address.isNotBlank()){
                    address
                }
                else if (lastKnownLocation.latitude!= 0.0 && lastKnownLocation.longitude!=0.0) {
                     lastKnownLocation.toAddress(this@LocationActivity)

                }
                else{
                    ""
                }
                val intent = Intent()
                intent.putExtra(DATA, result)
                setResult(RESULT_OK, intent)
                finish()
            }

        }
        locationSettingHelper.checkLocationSettings()
    }

    private inline fun onCreateMap(crossinline onReady : (googleMap:GoogleMap)->Unit) {
        val fragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(binding.mapFragment.id,fragment,fragment.javaClass.simpleName)
            .commit()
        fragment.getMapAsync { p0 -> onReady(p0) }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationSettingHelper.onActivityResult(requestCode, resultCode, data)
    }


}

