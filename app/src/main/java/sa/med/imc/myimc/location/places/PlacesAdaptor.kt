package sa.med.imc.myimc.location.places

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import sa.med.imc.myimc.Base.BaseAdaptor
import sa.med.imc.myimc.databinding.ItemPlacesBinding
import sa.med.imc.myimc.location.places.model.PlaceItem

typealias OnAction = (item:PlaceItem)->Unit
class PlacesAdaptor(context: Context,val onAction: OnAction) : BaseAdaptor<PlaceItem,PlacesAdaptor.Action,PlacesAdaptor.ViewHolder>(context) {

    inner class ViewHolder(val binding:ItemPlacesBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{
                onAction(getItem(adapterPosition))
            }
        }
    }

     enum class Action{
         CLICK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPlacesBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.setText(item.address)
    }
}