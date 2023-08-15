package sa.med.imc.myimc

import androidx.recyclerview.widget.RecyclerView
import sa.med.imc.myimc.Base.BaseAdaptor

operator fun <T,E,H: RecyclerView.ViewHolder> BaseAdaptor<T, E, H>.get(position:Int): T = internalDataList()[position]