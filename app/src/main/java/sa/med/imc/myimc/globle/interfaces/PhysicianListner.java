package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.Physicians.model.DoctorModel;

public interface PhysicianListner {
    void onSuccess(DoctorModel doctorModel);
    void onFail();
}
