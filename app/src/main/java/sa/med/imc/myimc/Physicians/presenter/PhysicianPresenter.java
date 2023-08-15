package sa.med.imc.myimc.Physicians.presenter;


/*
 * Physician Presenter interface to call API in PhysicianImpl.
 */
public interface PhysicianPresenter {

    void callGetAllDoctors(String token, String mrn, String search_txt, String lang, String item_count, String page, int hosp);

    void callGetSearchDoctors(String token, String mrn, String clinic_id, String search_txt,
                              String lang, String rating, String item_count, String page,String type, int hosp);

    void callGetDoctorsInfo(String token, String doctor_id, String mrn, String clinic_id, int hosp);

    void callGetCMSDoctorProfileData(String doctor_id);

    void callGetNextAvailableDateTime(String token,String physicianId,String clinicId,String serviceId,String deptCode,int pos, int hosp);

    void callGetDoctorServiceList(String token, String providerCode, String specialityCode);

}


