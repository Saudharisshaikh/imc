package sa.med.imc.myimc.location.places

import sa.med.imc.myimc.Utils.Loading
import sa.med.imc.myimc.location.places.model.PlaceItem

interface PlacesView :Loading {
    fun onPlaces(list:MutableList<PlaceItem>)
}