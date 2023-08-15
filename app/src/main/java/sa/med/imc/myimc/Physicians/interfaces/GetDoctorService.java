package sa.med.imc.myimc.Physicians.interfaces;

import sa.med.imc.myimc.Physicians.model.DrServiceResponse;

public interface GetDoctorService {
    void onGetServiceList(DrServiceResponse serviceResponse);
    void onFail(String error);
}
