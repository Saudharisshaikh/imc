package sa.med.imc.myimc.location.places

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vincent.filepicker.DividerListItemDecoration
import kotlinx.coroutines.launch
import sa.med.imc.myimc.Base.BaseBottomSheet
import sa.med.imc.myimc.R
import sa.med.imc.myimc.databinding.FragmentPlacesBinding
import sa.med.imc.myimc.location.places.model.PlaceItem

typealias OnPlace = (place:Place?,address:String)->Unit
class PlacesSheet(val onPlace: OnPlace) : BaseBottomSheet() , PlacesView {
    private lateinit var binding: FragmentPlacesBinding
    private val presenter = PlacePresenterImpl(this)
    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val adaptor by lazy {
        PlacesAdaptor(requireContext()) {
            lifecycleScope.launch {
                onPlace(presenter.getPlaceDetail(it.id),it.address)
                dismiss()
            }
        }
    }
    private val runnable = {
        val query = binding.etSearch.text.toString().trim()
        if (query.isNotBlank()){
            presenter.getPlaces(query)
        }

    }

    companion object{
        fun newInstance(onPlace: OnPlace): PlacesSheet {
            return PlacesSheet(onPlace)
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onPlaces(list: MutableList<PlaceItem>) {
        adaptor.setAndClearData(list)
        adaptor.notifyDataSetChanged()
        setState(BottomSheetBehavior.STATE_EXPANDED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlacesBinding.inflate(inflater, container,false)
        hideLoading()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.addItemDecoration(DividerListItemDecoration(requireContext(),RecyclerView.VERTICAL,-1))
        binding.recyclerView.adapter = adaptor
        /*binding.etSearch.addTextChangedListener (afterTextChanged = { text ->
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable,1000)
        })*/

    }

    override fun showLoading() {
        binding.showProgress = true
    }

    override fun hideLoading() {
        binding.showProgress = false
    }


}