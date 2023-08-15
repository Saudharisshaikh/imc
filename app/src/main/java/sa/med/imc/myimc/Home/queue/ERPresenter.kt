package sa.med.imc.myimc.Home.queue

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sa.med.imc.myimc.Base.GenericResponse
import sa.med.imc.myimc.Home.model.ERInfoItem
import sa.med.imc.myimc.Network.*

typealias OnLoading = (value : Boolean)->Unit
typealias OnInformation = (values:List<ERInfoItem>)->Unit
interface AppointmentQPresenter {
    fun getInformation()
}

class AppointmentQPresenterImpl(val onLoading: OnLoading,val onInformation: OnInformation) : AppointmentQPresenter {
    private val context = ImcApplication.getInstance().applicationContext


    override fun getInformation() {
        onLoading.invoke(true)
        ServiceGenerator.createService(WebService::class.java,SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN,"")).appointmentInfo().enqueue(object :
            Callback<GenericResponse<List<ERInfoItem>>> {
            override fun onResponse(call: Call<GenericResponse<List<ERInfoItem>>>, response: Response<GenericResponse<List<ERInfoItem>>>) {

                onLoading.invoke(false)
                if (response.isSuccessful && response.body()!=null){
                    onInformation.invoke(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<GenericResponse<List<ERInfoItem>>>, t: Throwable) {
                onLoading.invoke(false)
            }
        })



    }

}
