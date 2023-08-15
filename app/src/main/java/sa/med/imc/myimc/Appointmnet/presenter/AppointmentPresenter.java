package sa.med.imc.myimc.Appointmnet.presenter;


import java.util.List;

import sa.med.imc.myimc.Physicians.model.PhysicianResponse;

/*
 * Appointment Presenter interface to call API in AppointmentImpl.
 * For Guest and Logged In users
 */
public interface AppointmentPresenter {

    void callGetAllDates(String token, String clinic_id, String doctor_id, String service_id, String type, int hosp);

    void callGetAllTimeSlots(String token, String clinic_id, String doctor_id, String service_id, String date, String type, int hosp);

    void callBookAppointment(String token, String mrNumber, String sessionId, String timeSlot, String date, String telehealth, int hosp);

    void callBookAppointmentGuest(String token, String iqumaId, String sessionId, String timeSlot, String date,
                                  String patName, String patNameAr, String familyName, String familyNameAr,
                                  String fatherName, String fatherNameAr, String gender, String mobile,
                                  String phone, String userDob, String lang, String comments);


    void callRescheduleAppointment(String token, String mrNumber, String sessionId, String timeSlot, String date, String bookingId, int hosp);

    void callGetAppointmentPrice(String token, String mrNumber, String sessionId, String date, int hosp);

    void callGetSearchDoctors(String token, String mrn, String clinic_id, String search_txt, String lang, String rating, String item_count, String page, String type, int hosp);

    void fetchAlternatePhysicians(String token, String physicianId, String clinic_id, String serviceId, String deptCode, String sessionType, String startDate, String endDate, int pos, int hosp);

    void fetchServiceList(String token, String providercode, String specialityCode);

    void fetchDrTimeSlot(String token, String datefrom, String dateto, String providercode, String servicecode, String specialityCode);

    void callBookingApt(String token, String PatientID, String SlotId,
                        String providerCode,
                        String serviceCode,
                        String specialityCode,
                        String hospitalCode,
                        String slotDate,
                        String slotFromTime,
                        int hosp,String providerDescription,
                        String arabicProviderDescription,String type);

    void fetchAlternatePhysiciansNew(String value, List<PhysicianResponse.Physician> physicians, String selectedDate);

}


