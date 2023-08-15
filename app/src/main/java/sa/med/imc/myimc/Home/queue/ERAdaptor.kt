package sa.med.imc.myimc.Home.queue

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sa.med.imc.myimc.Base.BaseAdaptor
import sa.med.imc.myimc.Home.model.ERInfoItem
import sa.med.imc.myimc.Network.Constants
import sa.med.imc.myimc.Network.SharedPreferencesUtils
import sa.med.imc.myimc.R
import sa.med.imc.myimc.databinding.ListItemErV2Binding

class ERAdaptor(context: Context) : BaseAdaptor<ERInfoItem,Any,ERAdaptor.Holder>(context) {
    private val icons = arrayOf(context.resources.getDrawable(R.drawable.ic_er_time),context.resources.getDrawable(R.drawable.ic_er_patient))
    inner class Holder(val viewDataBinding: ListItemErV2Binding) : RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(ListItemErV2Binding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = getItem(position)
        holder.viewDataBinding.icon = icons[position]
        holder.viewDataBinding.value = model.value.toString()
        holder.viewDataBinding.label = if (SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,"ar").equals("ar",false)){
            model.labelAr
        }
        else{
            model.labelEn
        }
        holder.viewDataBinding.description =
            if (SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,"ar").equals("ar",true)){
                model.headingAr + '\n' + model.subHeadingAr
            }
            else{
                model.headingEn + '\n' + model.subHeadingEn
            }

    }
}