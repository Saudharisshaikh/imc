package sa.med.imc.myimc.FitnessAndWellness.value

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import okhttp3.internal.notify
import sa.med.imc.myimc.Base.BaseAdaptor
import sa.med.imc.myimc.Base.BaseBottomSheet
import sa.med.imc.myimc.FitnessAndWellness.value.model.HealthValueModel
import sa.med.imc.myimc.Network.Constants
import sa.med.imc.myimc.Network.SharedPreferencesUtils
import sa.med.imc.myimc.databinding.FragmentAddHealthValueBinding
import sa.med.imc.myimc.get
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine


class AddHealthValueSheet() : BaseBottomSheet() {
    private lateinit var binding:FragmentAddHealthValueBinding
    private val adaptor by lazy {
        AddHealthValueAdaptor(requireContext())
    }
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        tree.v(throwable)
        hideLoading()
        dismiss()
    }
    private val scope = CoroutineScope(SupervisorJob()+lifecycleScope.coroutineContext+handler)

    companion object{
        const val DATA = "dataforvalue"
        const val START = "start"
        const val END = "end"

        @JvmStatic
        fun newInstance(model: ArrayList<HealthValueModel>) = AddHealthValueSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(DATA,model)
            }
        }

        @JvmStatic
        fun newInstance(model: ArrayList<HealthValueModel>,start:Long,end:Long) = AddHealthValueSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(DATA,model)
                putLong(START,start)
                putLong(END,end)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddHealthValueBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.toolbar.setOnMenuItemClickListener {
            scope.launch {
                withContext(Dispatchers.Main){
                    showLoading()
                }
                withContext(Dispatchers.IO){
                    val map = hashMapOf<DataType,MutableList<Triple<Field,Float,String>>>()
                    var result = false
                    for (model in adaptor.internalDataList()){
                        if (model.value.isNotEmpty()) {
                            var list = map[model.dataType]
                            if (list.isNullOrEmpty()){
                                list = mutableListOf()
                            }
                            val pair = Triple(model.field,model.value.toFloat(),model.title)
                            list.add(pair)
                            map[model.dataType] = list
                            result = true
                        }
                        else{
                            result=false
                        }
                    }
                    if (result){
                        for (entry in map.keys){
                            insertSession(map[entry]!!,entry)

                        }
                    }
                }
                withContext(Dispatchers.Main){
                    if (context!=null) {
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent().apply {
                            action = Constants.REFRESH
                        })
                    }
                    hideLoading()
                    dismiss()

                }
            }

            true
        }
        if (arguments!=null){
            adaptor.setData(arguments?.getParcelableArrayList(DATA)).run {
                adaptor.notifyDataSetChanged()
                adaptor.notifyItemChanged(adaptor.itemCount-1, bundleOf().apply {

                })
            }
       //     binding.title = adaptor[0].title
        }
        else{
            dismiss()
        }
        binding.recyclerView.adapter = adaptor
    }
    private suspend fun insertSession(list : MutableList<Triple<Field,Float,String>>, dataType: DataType?) = suspendCoroutine<Unit> {
        val startTime = arguments?.getLong(START,System.currentTimeMillis()) ?:System.currentTimeMillis()
        val end = arguments?.getLong(END,System.currentTimeMillis()) ?: System.currentTimeMillis()
        val source = DataSource.Builder().setDataType(dataType).setType(DataSource.TYPE_RAW).build()
        val point = DataPoint.builder(source).setTimestamp(startTime + 500, TimeUnit.MILLISECONDS).apply {
            for (tripe in list){
                setField(tripe.first,tripe.second)
            }
        }.build()
        val set = DataSet.builder(source).add(point).build()
        // Create a session with metadata about the activity.
        val session = Session.Builder()
            .setName(
                SharedPreferencesUtils.getInstance(requireActivity())
                    .getValue(Constants.KEY_USER).mrn + System.currentTimeMillis()
            )
            .setIdentifier(
                SharedPreferencesUtils.getInstance(requireActivity())
                    .getValue(Constants.KEY_USER).mrn
            )
            .setDescription(dataType!!.name) //.setActivity(FitnessActivities.RUNNING)
            .setStartTime(startTime, TimeUnit.MILLISECONDS)
            .setEndTime(end, TimeUnit.MILLISECONDS)
            .build()

// Build a session insert request
        val insertRequest = SessionInsertRequest.Builder()
            .setSession(session) // Optionally add DataSets for this session.
            .addDataSet(set)
            .build()
        val readFitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_WRITE)
            .addDataType(HealthDataTypes.TYPE_BLOOD_GLUCOSE, FitnessOptions.ACCESS_WRITE)
            .addDataType(HealthDataTypes.TYPE_OXYGEN_SATURATION, FitnessOptions.ACCESS_WRITE)
            .addDataType(HealthDataTypes.TYPE_BODY_TEMPERATURE, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_WRITE)
            .build()
        Fitness.getSessionsClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireContext(), readFitnessOptions))
            .insertSession(insertRequest)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    tree.v("task was successful")
                    it.resumeWith(Result.success(Unit))

                }
                else{
                    it.resumeWith(Result.failure(Throwable("")))
                }
            }
    }

}

