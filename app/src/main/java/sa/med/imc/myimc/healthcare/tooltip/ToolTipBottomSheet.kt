package sa.med.imc.myimc.healthcare.tooltip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sa.med.imc.myimc.Base.BaseBottomSheet
import sa.med.imc.myimc.databinding.FragmentToolTipBinding

class ToolTipBottomSheet : BaseBottomSheet() {
    lateinit var binding:FragmentToolTipBinding

    companion object{
         const val DATA = "value"

        @JvmStatic
        fun newInstance(data:String) = ToolTipBottomSheet().apply {
            val b = Bundle()
            b.putString(DATA,data)
            arguments = b
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolTipBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!=null && arguments!!.containsKey(DATA)){
            binding.data = arguments!!.getString(DATA,"")
        }
    }

}