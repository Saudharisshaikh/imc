package sa.med.imc.myimc.Physicians.presenter;

import sa.med.imc.myimc.Physicians.model.DoctorFullDeatilsModel;

public interface FullDoctorDetailListner {
    void onSuccess(DoctorFullDeatilsModel doctorFullDeatilsModel);
    void onFailed();
}
