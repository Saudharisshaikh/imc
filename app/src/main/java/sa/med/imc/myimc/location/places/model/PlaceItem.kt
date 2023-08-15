package sa.med.imc.myimc.location.places.model

import com.google.android.gms.maps.model.LatLng
import org.zakariya.stickyheaders.StickyHeaderLayoutManager

data class PlaceItem(val id:String , val address : String, val latLng: LatLng?=null)
