package sa.med.imc.myimc.Base;
//4 jan
//import ai.medicus.medicuscore.Patient;
//import ai.medicus.medicuscore.PatientRecord;

import ai.medicus.insights.MedicusInsights;
import ai.medicus.medicuscore.Patient;
import ai.medicus.medicuscore.PatientRecord;
import ai.medicus.smartreport.SmartReport;
import ai.medicus.utils.MedicusUtilities;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;


public class MCPatient {

    private static MCPatient instance;

    private Patient current;


    //
    private PatientRecord patientRecord;

    public static synchronized MCPatient getInstance() {
        if (instance==null){
            instance=new MCPatient();
        }
        if(instance.get() == null){
            if((SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_PATIENT_ID_FOR_MEDICUS,-1))!=-1){
                //4 jan
                instance.setCurrent(Patient.get((SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_PATIENT_ID_FOR_MEDICUS,-1))));
            }

        }
        return instance;
    }
    //4 jan
    public synchronized PatientRecord getPatientRecord() {

        if (patientRecord == null && current != null)
            patientRecord = current.getPatientRecord();

        return patientRecord;

    }

    public synchronized Patient get() {

        if (current != null) {

            if (patientRecord == null) {

                patientRecord = current.getPatientRecord();

            }

            return current;

        } else {

            String id = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_MRN,"");

            if (id == null)
                return null;

            current = Patient.getByNumber(id);


            if (current != null) {

                this.patientRecord = current.getPatientRecord();

            }

            return current;

        }

    }

    public static void updateCurrentPatient() {

        int patientId = ai.medicus.utils.SharedPreferencesUtils.getPatientId();

        if (patientId != -1) {

            Patient updatedPatient = Patient.get(patientId);

            if (updatedPatient != null) {
                updatedPatient.refreshData();
                instance.current = updatedPatient;
                instance.current.syncWithServerAsync();
                instance.patientRecord = updatedPatient.getPatientRecord();
                SmartReport.patient = MCPatient.getInstance().get();
                MedicusInsights.setPatient(MCPatient.getInstance().get());
                MedicusUtilities.setPatient(MCPatient.getInstance().get());
            }

            instance.patientRecord = null;

        }

    }

    public void setCurrent(Patient current) {
        this.current = current;
    }

    public static boolean isLabUser() {
        return instance.current != null && instance.current.isOnlinePatient();
    }

    public static boolean isTokenUser() {
        return isLabUser() && !instance.getPatientRecord().getHasPassword();
    }

    public void destroy(){
        instance=null;
    }
}
