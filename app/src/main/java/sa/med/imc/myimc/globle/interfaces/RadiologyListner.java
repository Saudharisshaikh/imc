package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.MedicalReport;

public interface RadiologyListner {
    void onSuccess(List<MedicalReport> medicalReports);
    void onFaild();

}
