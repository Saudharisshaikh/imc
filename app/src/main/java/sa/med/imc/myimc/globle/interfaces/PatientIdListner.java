package sa.med.imc.myimc.globle.interfaces;

public interface PatientIdListner {
    void onSuccess(String patientId);
    void onFailed(String error);
}
