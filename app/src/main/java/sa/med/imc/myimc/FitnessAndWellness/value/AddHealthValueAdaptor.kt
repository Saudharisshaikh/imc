package sa.med.imc.myimc.FitnessAndWellness.value

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import sa.med.imc.myimc.Base.BaseAdaptor
import sa.med.imc.myimc.FitnessAndWellness.value.model.HealthValueModel
import sa.med.imc.myimc.databinding.ItemAddValueBinding
import sa.med.imc.myimc.get


const val NOTIFY  = "notify"
class AddHealthValueAdaptor(context: Context) : BaseAdaptor<HealthValueModel,Any,AddHealthValueAdaptor.Holder>(context) {


    inner class Holder( val binding: ItemAddValueBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=Holder(ItemAddValueBinding.inflate(LayoutInflater.from(context),parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.postfix = this@AddHealthValueAdaptor[position].postfix
        holder.binding.length = this@AddHealthValueAdaptor[position].length
        holder.binding.hint  = this@AddHealthValueAdaptor[position].hint
        /*holder.binding.etInput.addTextChangedListener(onTextChanged = { t, _, _, _ ->
            if (t != null)
                this@AddHealthValueAdaptor[holder.adapterPosition].value = t.toString()
        })*/
    }
}