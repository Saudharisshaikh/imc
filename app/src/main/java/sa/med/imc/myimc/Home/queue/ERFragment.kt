package sa.med.imc.myimc.Home.queue

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import sa.med.imc.myimc.Base.BaseFragment
import sa.med.imc.myimc.databinding.FragmentErBinding
import java.util.*


class ERFragment : BaseFragment() {
  private lateinit var binding:FragmentErBinding
  private val DELAY = 5000L
  private val handler by lazy {
     Handler(Looper.getMainLooper())
  }
    private val r by lazy {
        object:Runnable{
            override fun run() {
                var position = (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (adaptor.itemCount-1==position){
                    position--
                    binding.recyclerView.smoothScrollToPosition(position)
                }
                else{
                    position++
                    binding.recyclerView.smoothScrollToPosition(position)
                }
                handler.postDelayed(this,DELAY)
            }

        }
    }
  private val presenter:AppointmentQPresenter by lazy {
      AppointmentQPresenterImpl({ v ->
          binding.showProgress = v
      },{
          binding.showList = it.isNullOrEmpty()
          adaptor.setData(it)
          adaptor.notifyDataSetChanged()
          handler.postDelayed(r,DELAY)
      })

  }
    private val adaptor by lazy {
        ERAdaptor(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentErBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getInformation()
        val snapper = PagerSnapHelper()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        snapper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adaptor
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(r)
    }

    override fun onBackPressedAction(): Runnable {
        TODO("Not yet implemented")
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ERFragment().apply {

            }
    }
}