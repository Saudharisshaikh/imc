package sa.med.imc.myimc.splash

import sa.med.imc.myimc.splash.HospitalModel.HospitalList

class HospitalModel {
    var hospital_list: List<HospitalList>? = null
    var status: String? = null

    inner class HospitalList {
        var hospital_code: String? = null
        var hospital_description: String? = null
    }
}