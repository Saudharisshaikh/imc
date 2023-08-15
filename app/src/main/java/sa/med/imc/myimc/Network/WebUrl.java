package sa.med.imc.myimc.Network;

/**
 * All API names, Base urls, headers
 */

public @interface WebUrl {
    String WellnessEnglish = "https://www.imc.med.sa/en/wellness";
    String WellnessArabic = "https://www.imc.med.sa/ar/wellness";
    String MAYO_CLINIC_URL = "https://healthtips.imc.med.sa/wellness/#/home/en";
    String MAYO_CLINIC_AR_URL = "https://healthtips.imc.med.sa/wellness/#/home/ar";
    String VIRTUAL_TOUR_LINK = "https://my.matterport.com/show/?m=L8BKBzZx2QG";
    String COUNTRY_CODE = "";
    String CONSENT_SIGNUP_LIVE = "https://patientportal.imc.med.sa/imcportal/assets/documents/ConsentforGeneralTreatment.pdf";
    String CONSENT_TELEMED = "https://www.imc.med.sa/TeleHealth/consent.html?lang=en";
    String CONSENT_TELEMED_AR = "https://www.imc.med.sa/TeleHealth/consent.html?lang=ar";
    String Telr_Payment_Url ="https://secure.telr.com/gateway/process.html?o=5CBAC2FCEE6173EF00C2D5F8AD8C0C38946DC0492129B1DD744EFBC4D0B36489";

    //  =======================Base url==========================
    //TODO: To be confirmed
//     String BASE_URL = "http://192.168.1.47:8080/imc_portal/";
    String BASE_URL_JD = "http://10.1.1.164:8081/";
    // String BASE_URL_LINKS = "https://www.imc.med.sa/";//
    // String ASSESSMENT_BASE_URL = "https://www.assessmentcenter.net/ac_api/2014-01/";
    // String TEMP_BASE_URL = "http://192.168.1.47:8080/imc_portal_selfreg/";
    String BASE_URL_LINKS = "https://www.imc.med.sa/";//
    String TEMP_BASE_URL = "http://192.168.1.47:8080/imc_portal_selfreg/";

//  ========================Auth APIs===========================

    String AUTH_LOGIN = "auth-v2/login/";
    String  AUTH_VALIDATE = "auth-v2/login/validate/";
    String AUTH_RESEND_OTP = "auth/resendotp/";
    String AUTH_UPDATE_CONSENT = "auth-v2/update/consent/{mrNumber}/{constantValue}";
//    String REG_VALIDATE = "selfReg/validate/";
    String REG_VALIDATE = "auth-v2/registration/validate";
    String REG_LOGIN = "auth-v2/registration/login/";
    String GET_DISTRICT = "web/registration/getDistrictList";
    String GET_CITY = "web/registration/getCityList";
    String GET_NATIONALITY = "web/registration/getNationalityList";

    //  ==========================APIs===============================

    String WEB_REGISTER = "api/patient/task/registration/v2";
//    String WEB_REGISTER = "api/registration/register/";
    String GET_USER_INFO_WITH_ID = "web/extpatinfo/getextpatinfo";
    String API_GET_USER = "api/user/getDetails/{userid}/{hosp}";
    String API_UPDATE_USER = "api/user/updateInfo";
    String ALL_CLINICS = "api/clinics/find/";
    String ALL_CLINICS_FILTER = "api/clinics/all";

//    String ALL_DOCTORS = "api/physician/findall/";
    String ALL_DOCTORS = "api/bookings/get-care-providers";
    String ALL_DOCTORS_BY_HOSPITAL_CODE = "api/careProvidersList/all";
    String ALL_DOCTORS_FILTER = "api/physician/all/";
//    String FETCH_DOCTOR_INFO = "api/doctors/finddoc";

    String FETCH_DOCTORS_OF_CLINIC = "api/physician/find/{clinic_id}/";

    String ALL_BOOKINGS = "api/bookings/all";
    String CANCEL_BOOKING = "api/bookings/cancel/{bookingid}/{mrnNumber}/{hosp}";
    String RESCHEDULE_BOOKING = "api/bookings/reschedule";

//    String FETCH_SESSION_DATES = "api/bookings/fetchsessiondates/";
//    PIYUSH TRACKER
    String FETCH_SESSION_DATES = "api/bookings/get-doctor-availability";
    String FETCH_TIME_SLOTS = "api/bookings/fetchtimeslots/";
    String BOOK_APPOINTMENT = "api/bookings/bookapt/";
    String DOWNLOAD_CONFIRMATION = "api/bookings/downloadConfirmation/";

    String FETCH_REPORTS_COUNT = "api/labreports/getCount/";

    String FETCH_LAB_REPORTS = "api/labreports/find";
    String GENERATE_LAB_REPORT = "api/labreports/generate";
    // http://192.168.1.49:8080/imc_portal/api/medicus/generatePdf
    String GENERATE_SMART_LAB_REPORT = "api/medicus/generatePdf";

    String FETCH_LAB_REPORTS_MEDICUS = "api/reports/laboratory-results?patientId=000213&page=1&oldData=1";


    String FETCH_MEDICAL_REPORTS = "api/medreports/find";
    String GENERATE_MEDICAL_REPORT = "api/medreports/generate";

    String FETCH_SICK_LEAVE_REPORTS = "api/sickleave/find";
    String GENERATE_SICK_LEAVE_REPORT = "api/sickleave/generate";

    String GENERATE_PAYMENT_REPORT = "api/pat/payment-report";

    String FETCH_OPERATIVE_REPORTS = "api/operativeReport/findAll";
    String GENERATE_OPERATIVE_REPORTS = "api/operativeReport/generate";

    String FETCH_DISCHARGE_REPORTS = "api/dischrageReport/findAll";
    String GENERATE_DISCHARGE_REPORTS = "api/dischrageReport/generate";

    String FETCH_CARD_REPORTS = "api/cardreports/find";
    String GENERATE_CARD_REPORTS = "api/cardreports/generate";

    String GET_MEDICATION = "api/pat/medication";
    String GET_PRESCRIPTION = "api/pat/prescription";
    String GENERATE_PRESCRIPTION = "api/pat/generate";

    String CHANGE_LANGUAGE = "api/user/changelang/{userid}/{lang}/{hosp}";
    String REQUEST_FOR_REPORT = "api/report-request";
    String GET_REPORT_TYPE = "api/codetable/reporttype";
    String GET_TRANSLATION_REPORTS = "api/reportType/translationReports/{hosp}";
    //TODO: hosp to be added
    String SEND_OTP = "api/verifyemail";   /* api/verifyemail*/
    String VERIFY_EMAIL = "api/validateemail";   /* api/verifyemail*/

    String GET_SELECT_VISIT = "api/health/patient/episode";

    String RATE_DOCTOR = "api/physician/rate";
    String LOG_OUT = "api/user/logoutByDeviceID/{userid}/{deviceToken}/{hosp}";
    String GET_ALLERGIES = "api/health/patient/allergy";
    String GET_READINGS = "api/health/patient/vitals/old";

    String GET_APPOINTMENT_PRICE = "api/bookings/check-price/{sessionId}/{apptDate}/{hosp}";
    String GET_SELFCHECKIN_PRICE = "api/selfcheckin/check-price/{sessionId}/{apptDate}";
    String VERIFY_PAYMENT = "api/bookings/verify-payment";
    String CONFIRM_ARRIVAL = "api/selfcheckin/updateArrival";
    String GET_NEXT_AVAILABLE_SLOT = "api/bookings/fetchnexttimeslots/v2";
    String FETCH_ALTERNATE_PHYSICIANS = "api/bookings/fetchnexttimeslotsDate";//fetchAlternatePhysicians


//  ==========================GUARDIAN APIs===============================

    String ADD_GUARDIAN = "api/add-guardian/";
    String GET_GUARDIAN = "api/guardian/getDetails/{MRN_NUMBER}/{hosp}";
    String REMOVE_GUARDIAN = "api/remove-guardian/";
    String LINK_UNLINK_GUARDIAN = "api/guardian/unlinkGuardian/";
    String EDIT_GUARDIAN = "api/guardian/editGuardian/";

//  ==========================Web  APIs===============================

    String DEPT_ALL = "web/department/all";
//    String ALL_DOCTORS_WEB = "web/physician/findall/";
    String ALL_DOCTORS_WEB = "api/bookings/get-care-providers";
    String ALL_DOCTORS_SERVICE = "api/bookings/get-doctor-services";
    String ALL_DOCTORS_SLOT = "api/bookings/get-doctor-availability";
    String BOOK_APPOINTMENT_NEW = "api/appointment/book";
    String ALL_LANGUAGES = "web/fetchlanguages/";
//    String FETCH_DOCTOR_INFO_WEB = "web/doctors/finddoc";
    String CONTACT_US_NEW = "api/contactus";
    String GET_PHONE_NUMBER = "web/getphonenumber/{hosp}";
//    String ALL_CLINICS_WEB = "web/clinics/find/";
    String ALL_CLINICS_WEB = "api/bookings/get-speciality";
    String GET_ALL_COMPANIES = "api/codetable/insurance";
//    String GET_ALL_COMPANIES = "web/registration/getInsuranceCompanies/{hosp}";

//  ==========================GUEST APIs===============================

    String GUEST_LOGIN = "guest/login/";
    String GUEST_VALIDATE = "guest/validate/";
    String GUEST_FETCH_SESSION_DATES = "api/guest/fetchsessiondates/";
    String GUEST_FETCH_TIME_SLOTS = "api/guest/fetchtimeslots/";
    String GUEST_BOOK_APPOINTMENT = "api/guest/bookApt/";

//  ============================LINKS  APIs=======================

    String CONTENT_LINK = "getpage/{id}";

//  ==========================Notification  APIs=======================

    String NOTIFICATION_ON_OFF = "api/user/change-ns/{mrn}/{on_off_param}/{hosp}";
    String GET_BOOKING_ID = "api/home/last-appt/{mrnumber}";
    String GET_ALL_NOTIFICATIONS = "api/notification/fetchall";
    String DELETE_NOTIFICATIONS = "api/notification/delete/{notification_id}/{hosp}";
    String CLEAR_ALL_NOTIFICATIONS = "api/notification/delete/all/{mrnumber}/{hosp}";
    String READ_NOTIFICATIONS = "api/notification/readNotification/{patid}/{notiId}/{hosp}";

//  ===================== CMS Departments and Doctors APIs =========================

    String DEPARTMENT_LIST = "deplist";
    String DEPARTMENT_DOC = "doctorlist/{id}";
    String DOCTOR_DETAIL = "doctorinfo/{id}";//"doctordetail/{id}";
    String LANGUAGES_LIST = "language";
    String HEALTH_CATEGORY = "healthcategory";
    String SEARCH_HEALTH_TIPS = "searchhealttips";

    //  ===================== Assessment APIs =========================
    String GET_TOKEN = "api/promisAssessment/getToken/{pat_ID}/{hosp}";
    // String START_ASSESSMENT_API = "api/promisAssessment/startAssessment/";
    // String SUBMIT_ASSESSMENT_API = "api/promisAssessment/assessmentResult/";
    //================IMC Promis APIs=========================

    String API_START_ASSESSMENT = "api/promis/assessmentStart";
    String API_FORM_ASSESSMENT = "api/promisAssessment/assessmentForm";
    String API_SCORE_ASSESSMENT = "api/promisAssessment/assessmentScore";
    String GET_COMPLETED_ASSESSMENT = "api/promisAssessment/completed";
    String GET_FORMS = "api/assessment/promisForm";
    String VIEW_RESULT = "api/promisAssessment/viewResult";

    //====================Check In=========================

    String CHECK_IN_PAY_ONLY = "api/selfcheckin/checkCurrentLocation";
    String CHECK_IN_PAYMENT = "api/selfcheckin/payment";
    String CHECK_IN_VERIFY_PAYMENT = "api/selfcheckin/verify-payment";


    //====================Medicus===================
    String GET_PIN = "api/medicus/getPin";

    /*Online Pharmacy*/
    String GET_PRESCRIPTION_MEDS = "api/pat/prescription/{patId}/{hosp}/{rxNo}/{episodeNo}";
    String VALIDATE_PHARM_D= "api/selfcheckin/payment/perscription";
    String VALIDATE_PHARM_D_PAYMENT_REF= "api/selfcheckin/verify-prescription-payment";

    /*Prescription Invocies*/

    String GET_INVOICES = "api/prescriptions/get-invoice-list";
    String GENERATE_PRESCRIPTION_INVOICE = "api/prescriptions/generate-invoice";
    String HEALTH_CARE_LIST = "web/hhc_form/question_list";

    String HEALTH_CARE_SUBMISSION = "web/hhc_form/submissions";


    String APPOINTMENT_Q_INFO ="web/er-waiting/info";
}
