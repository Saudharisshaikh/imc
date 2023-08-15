package sa.med.imc.myimc.Interfaces;

import java.io.Serializable;

public interface FragmentAdd {

    void openProfileMedicineFragment(String value);

    void openPhysicianFragment(String value, String clinic_id);

    void openClinicFragment(String value);

    void openPaymentInfoFragment(Serializable value,String key);

    void openTelrFragment(Serializable book,Serializable res, String value);

    void openMedicineFragment(String type,String value);

    void backPressed(String type);

    void openLMSRecordFragment(String value,String type,String episodeId);

    void startTask(int time);

    void openHealthSummary(String value);

    void openFitness(String value);

    void openWellness(String value);

    void openBodyMeasurement(String value);

    void openActivity(String value);

    void openSleepCycle(String value);

    void openHeatAndVitals(String value);


    void openAllergies(String value);

    void openVitalSigns(String value);

    void checkLocation();

}
