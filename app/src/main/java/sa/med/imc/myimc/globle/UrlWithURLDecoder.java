package sa.med.imc.myimc.globle;

import static sa.med.imc.myimc.BuildConfig.BASE_URL_FOR_IMAGE;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;

public class UrlWithURLDecoder {
    public static final String base_url = BuildConfig.BASE_URL;
    final String base_url_for_image = BASE_URL_FOR_IMAGE;

    final String service_price = base_url + "api/bookings/get-service-price";
    final String generate_payment_url = base_url + "api/payment/generate-payment-url";
    final String verify_payment = base_url + "api/payment/verify-payment";
    final String settle_bill = base_url + "api/bookings/settle-bill";
    final String bookings_all = base_url + "api/bookings/all";
    final String bookings_old = base_url + "api/bookings/old";
    final String dashboard = base_url + "api/bookings/dashboard";

    final String cancel_appointment = base_url + "api/bookings/cancel";
    final String cancel_old_appointment = base_url + "api/bookings/cancel";

    final String laboratory_results = base_url + "api/reports/laboratory-results";
    final String radiology_results = base_url + "api/reports/radiology-results";
    final String sick_leave_reports = base_url + "api/reports/sick-leave-reports";
    final String laboratory_pdf_url = base_url + "api/reports/laboratory-report";
    final String sick_leave_pdf_url = base_url + "api/reports/sick-leave-report-print";
    final String radiology_pdf_url = base_url + "api/reports/radiology-report";
    final String discharge_summary_pdf_url = base_url + "api/reports/discharge-summary-report-print";
    final String operative_report_pdf_url = base_url + "api/reports/operative-report-print";
    final String discharge_summary_reports = base_url + "api/reports/discharge-summary-reports";
    final String operative_reports = base_url + "api/reports/operative-reports";
    final String cardiology_results = base_url + "api/reports/cardiology-results";
    final String reports_cardiology = base_url + "api/reports/cardiology-report";
    final String get_profile_detail = base_url + "api/user/getDetails";
    final String physician_details = base_url + "api/physician/details";
    final String medication_list = base_url + "api/health/patient/medication";
    final String registration = base_url + "api/patient/task/registration";
    final String registration_otp = base_url + "auth-v2/registration/login";
    final String registration_otp_verification = base_url + "auth-v2/registration/validate";
    final String checkCurrentLocation = base_url + "api/selfCheckIn/checkCurrentLocation";
    final String arrive = base_url + "api/bookings/arrive";
    final String notificationCount = base_url + "api/notification/unread/count/";
    final String contactus = base_url + "api/contactus";
    final String report_request = base_url + "api/tc/patient/task/";
    final String laboratory_results_old_data = base_url + "api/reports/laboratory-results";
    final String radiology_results_old = base_url + "api/reports/radiology-results";
    final String sick_leave_reports_old = base_url + "api/reports/sick-leave-reports";
    final String operative_reports_old = base_url + "api/reports/operative-reports";
    final String discharge_summary_reports_old = base_url + "api/reports/discharge-summary-reports";
    final String cardiology_results_old = base_url + "api/reports/cardiology-results";
    final String guardian_getDetails = base_url + "api/guardian/getDetails";
    final String new_vital = base_url + "api/health/patient/vitals";
    final String hospital_list = base_url + "web/appointment/facilities";
    final String get_all_doctor = base_url + "api/careProvidersList/all";
    final String insurance_list = base_url + "api/codetable/insurance";
    public static final String registration2 = base_url + "api/patient/task/registration/v2";
    public static final String update_info = base_url + "api/user/updateInfo";

    public static final String hospitals_list_v2 = base_url + "api/hospitals/list";

    final String buffer_time = base_url + "api/buffertime";

    final String full_doctor_details = base_url + "api/doctors/find";
    public static final String generate_smart_pdf = base_url + "api/medicus/generatePdf";

    final String doctor_profile = base_url_for_image + "uploads/docImg";
    final String clinic_profile = base_url_for_image + "uploads/clinic/";





    public String getService_price(String slotBookingId, String patientInsuranceId, boolean is_final_price) throws UnsupportedEncodingException {
        String ecodedUrl = service_price + "?slotBookingId=" + URLEncoder.encode(slotBookingId, "UTF-8") + "&patientInsuranceId="
                + URLEncoder.encode(patientInsuranceId, "UTF-8") + "&is_final_price=" + is_final_price;

        Log.e("abcd", ecodedUrl);
        return ecodedUrl;
    }

    public String getGenerate_payment_url() {
        Log.e("abcd", generate_payment_url);
        return generate_payment_url;
    }

    public String getVerify_payment() {
        Log.e("abcd", verify_payment);
        return verify_payment;
    }

    public String getSettle_bill() {
        Log.e("abcd", settle_bill);
        return settle_bill;
    }

    public String getBookings_all() {
        Log.e("abcd", bookings_all);
        return bookings_all;
    }

    public String getBookings_old() {
        Log.e("abcd", bookings_old);
        return bookings_old;
    }

    public String getDashboard() {
        Log.e("abcd", dashboard);
        return dashboard;
    }

    public String getCancel_appointment(String slotBookingId) throws UnsupportedEncodingException {
        String encodedUrl = cancel_appointment + "/" + URLEncoder.encode(slotBookingId, "UTF-8");
        Log.e("abcd", encodedUrl);
        return encodedUrl;
    }

    public String getCancel_old_appointment(String bookingId) {
        String encodedUrl = cancel_old_appointment + "?bookingId=" + bookingId;
        Log.e("abcd", encodedUrl);
        return encodedUrl;
    }

    public String getLaboratory_results(String patientId, String page, String hospitalCode) {
        String url = laboratory_results + "?patientId=" + patientId + "&page=" + page + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getRadiology_results(String patientId, String page, String hospitalCode) {
        String url = radiology_results + "?patientId=" + patientId + "&page=" + page + "&hosp=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getSick_leave_reports(String patientId, String page, String hospitalCode) {
        String url = sick_leave_reports + "?patientId=" + patientId + "&page=" + page + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getLaboratory_pdf_url(String orderId, String mrn, int hosp, String oldData) {
        String url = "";
        try {
            url = laboratory_pdf_url + "?orderId=" + URLEncoder.encode(orderId, "UTF-8") + "&mrn=" + mrn + "&hosp=" + hosp + "&oldData=" + oldData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("abcd", url);
        return url;
    }

    public String getSick_leave_pdf_url(String documentId, String oldData, String mrn, String daig, String hosp) {
        String url = sick_leave_pdf_url + "?documentId=" + documentId + "&oldData=" + oldData + "&mrn=" + mrn + "&diag=" + daig + "&hosp=" + hosp;
        Log.e("abcd", url);
        return url;
    }

    public String getRadiology_pdf_url(String mrn, String orderId, String orderLineNo, String hosp, String oldData) {
        String url = radiology_pdf_url + "?mrn=" + mrn + "&orderId=" + orderId + "&orderLineNo=" + orderLineNo +
                "&hosp=" + hosp + "&oldData=" + oldData;
        Log.e("abcd", url);
        return url;
    }

    public String getDischarge_summary_pdf_url(String documentId, String oldData, String mrn, String reportType, String hosp) {
        String url = discharge_summary_pdf_url + "?documentId=" + documentId + "&oldData=" + oldData +
                "&mrn=" + mrn + "&reportType=" + reportType + "&hosp=" + hosp;
        Log.e("abcd", url);
        return url;
    }

    public String getOperative_report_pdf_url(String documentId, String oldData, String mrn, String reportType, String hosp) {
        String url = operative_report_pdf_url + "?documentId=" + documentId + "&oldData=" + oldData + "&mrn="
                + mrn + "&reportType=" + reportType + "&hosp=" + hosp;
        Log.e("abcd", url);
        return url;
    }

    public String getDischarge_summary_reports(String patientId, String page, String hospitalCode) {
        String url = discharge_summary_reports + "?patientId=" + patientId + "&page=" + page + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getOperative_reports(String patientId, String page, String hospitalCode) {
        String url = operative_reports + "?patientId=" + patientId + "&page=" + page + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getCardiology_results(String patientId, String page, String hospitalCode) {
        String url = cardiology_results + "?patientId=" + patientId + "&page=" + page + "&hospitalCode=" + hospitalCode;

        Log.e("abcd", url);
        return url;
    }

    public String getReports_cardiology(String orderId, String mrn, int hosp, String oldData, String attendanceId) {
        String url = reports_cardiology + "?orderId=" + orderId + "&mrn=" + mrn + "&hosp=" + hosp + "&oldData=" + oldData + "&attendanceId=" + attendanceId;
        Log.e("abcd", url);
        return url;
    }

    public String getGet_profile_detail(String user_id, int hosp) {
        String url = get_profile_detail + "/" + user_id + "/" + hosp;
        Log.e("abcd", url);
        return url;
    }

    public String getPhysician_details(String code) {
        String url = physician_details + "?code=" + code;
        Log.e("abcd", url);
        return url;
    }

    public String getMedication_list(String patientId, String mrn, String page) {
        String url = medication_list + "?patientId=" + patientId + "&mrn=" + mrn + "&page=" + page;
        Log.e("abcd", url);
        return url;
    }

    public String getRegistration() {
        String url = registration;
        Log.e("abcd", url);
        return url;
    }

    public String getRegistration_otp() {
        String url = registration_otp;
        Log.e("abcd", url);
        return url;
    }

    public String getRegistration_otp_verification() {
        String url = registration_otp_verification;
        Log.e("abcd", url);
        return url;
    }

    public String getcheckCurrentLocation() {
        String url = checkCurrentLocation;
        Log.e("abcd", url);
        return url;
    }

    public String getArrive() {
        String url = arrive;
        Log.e("abcd", url);
        return url;
    }

    public String getNotificationCount(String mrnNumber,String hosp) {
        String url = notificationCount+mrnNumber+"/"+hosp;
        Log.e("abcd", url);
        return url;
    }

    public String getContactus() {
        String url = contactus;
        Log.e("abcd", url);
        return url;
    }

    public String getReport_request() {
        String url = report_request;
        Log.e("abcd", url);
        return url;
    }

    public String getLaboratory_results_old_data(String patientId, String page, int hospitalCode) {
        String url = laboratory_results_old_data + "?patientId=" + patientId + "&page=" + page + "&oldData=1" + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getRadiology_results_old(String patientId, String page, int hospitalCode) {
        String url = radiology_results_old + "?patientId=" + patientId + "&page=" + page + "&oldData=1" + "&hosp=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getSick_leave_reports_old(String patientId, String page, int hospitalCode) {
        String url = sick_leave_reports_old + "?patientId=" + patientId + "&page=" + page + "&oldData=1" + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getOperative_reports_old(String patientId, String page, int hospitalCode) {
        String url = operative_reports_old + "?patientId=" + patientId + "&page=" + page + "&oldData=1" + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getDischarge_summary_reports_old(String patientId, String page, int hospitalCode) {
        String url = discharge_summary_reports_old + "?patientId=" + patientId + "&page=" + page + "&oldData=1" + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getCardiology_results_old(String patientId, String page, int hospitalCode) {
        String url = cardiology_results_old + "?patientId=" + patientId + "&page=" + page + "&oldData=1" + "&hospitalCode=" + hospitalCode;
        Log.e("abcd", url);
        return url;
    }

    public String getGuardian_getDetails(String mrn, int hosp) {
        String url = guardian_getDetails + "/" + mrn + "/" + hosp;
        Log.e("abcd", url);
        return url;
    }

    public String getNew_vital(String patientId, String page) {
        String url = new_vital + "?patientId=" + patientId + "&page=" + page;
        Log.e("abcd", url);
        return url;
    }

    public String getHospital_list() {
        Log.e("abcd", hospital_list);
        return hospital_list;
    }

    public String getHospital_list_v2() {
        return hospitals_list_v2;
    }

    public String getBufferTime() {
        return buffer_time;
    }

    public String getGet_all_doctor(String hospitalCode) {
        return get_all_doctor + "?hospitalCode=" + hospitalCode;
    }

    public String getFull_doctor_details(String hospitalCode, String doctorCode) {
        String url = full_doctor_details + "?hospitalCode=" + hospitalCode + "&doctorCode=" + doctorCode;
        Log.e("abcd", url);
        return url;
    }

    public String getInsurance_list() {
        String url = insurance_list;
        Log.e("abcd", url);
        return url;
    }

    public String getDoctor_profile(Context context, String doctorID) {
        String hospital_code = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");
        String url = doctor_profile + "/" + hospital_code + "/" + doctorID + ".jpg";
        Log.e("abcd", url);
        return url;
    }

    public String getClinic_profile(Context context, String clincID) {
        String hospital_code = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");
        String url = clinic_profile + "/" + clincID + ".png";
        Log.e("abcd", url);
        return url;
    }


    public String getDoctor_profile_placeholder_male(Context context, String doctorID) {
        String hospital_code = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");
        String url = doctor_profile + "/docImg/" + hospital_code + "/fdoc-placeholder.png";
        Log.e("abcd", url);
        return url;
    }

    public String getDoctor_profile_placeholder_female(Context context, String doctorID) {
        String hospital_code = SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC");
        String url = doctor_profile + "/docImg/" + hospital_code + "/mdoc-placeholder.png";
        Log.e("abcd", url);
        return url;
    }
}

