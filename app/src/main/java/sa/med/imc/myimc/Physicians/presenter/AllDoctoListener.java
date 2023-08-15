package sa.med.imc.myimc.Physicians.presenter;

import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;

public interface AllDoctoListener {
    void onSuccess(AllDoctorListModel response1);
    void onFaild(String message);
}
