package sa.med.imc.myimc.location.places

import android.text.style.CharacterStyle
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sa.med.imc.myimc.Network.ImcApplication
import sa.med.imc.myimc.location.places.model.PlaceItem
import kotlin.coroutines.suspendCoroutine

interface PlacesPresenter {
    fun getPlaces(query:String)

    suspend fun getPlaceDetail(id:String):Place
}

class PlacePresenterImpl constructor(val view:PlacesView) :  PlacesPresenter {
    private val client : PlacesClient = Places.createClient(ImcApplication.getInstance())
   private val token = AutocompleteSessionToken.newInstance()

    override fun getPlaces(query: String) {
        view.showLoading()
        val request =
            FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationRestriction(bounds)
               // .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build()
        client.findAutocompletePredictions(request).addOnSuccessListener {
            val list = mutableListOf<PlaceItem>()
            for (i in it.autocompletePredictions){
                list.add(PlaceItem(i.placeId,i.getFullText(null).toString(),null))
            }
            view.hideLoading()
            view.onPlaces(list)
        }.addOnFailureListener {
            view.hideLoading()
        }.addOnCanceledListener {
            view.hideLoading()
        }.addOnCompleteListener {
            view.hideLoading()
        }
    }

    override suspend fun getPlaceDetail(id: String): Place = suspendCoroutine { c->
        // Specify the fields to return.
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG)

// Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(id, placeFields)
        client.fetchPlace(request).addOnSuccessListener {
            c.resumeWith(Result.success(it.place))
        }.addOnFailureListener {
            c.resumeWith(Result.failure(it))
        }
    }

}