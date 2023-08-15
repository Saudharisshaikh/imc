package sa.med.imc.myimc.Network;

public @interface Constants {
    String REFRESH = "refreshData";
    //Phone Local storage folder name where reports will be saved.
    String FOLDER_NAME = "/IMC Reports/";

    // PREFERENCE=============================================================

    String PREFERENCE_NAME = "IMC";
    String PREFERENCE_CONFIG = "config_data";

    String PATIENT_ID = "PATIENT_ID";

    String SELECTED_HOSPITAL = "selected_hospital";
    String HOSPITAL_CODE = "hospital_code";
    String HOSPITAL_CODE_FOR_HEADER = "HospitalID";
    String KEY_LANGUAGE = "key_language";
    String KEY_GUARDIAN = "key_guardian";

    String KEY_LOGIN_LANGUAGE = "key_login_language";

    String ENGLISH = "en";
    String ARABIC = "ar";

    String KEY_PHONE = "key_phone";
    String KEY_USER_ID = "key_user_id";

    String KEY_ACCESS_TOKEN = "key_access_token";
    String KEY_USER = "key_user";
    String KEY_PATIENT_EMAIL = "key_patient_email";

    String IS_EMAIL_VERIFIED = "isEmailVerified";
    String KEY_USER_MAIN = "key_user_main";
    String VERSION = "key_version";
    String EMAIL = "email";
    String patientInsuranceId = "patientInsuranceId";


    String BASE_ACTIVITY_FOR_ACCESSMENT = "base_activity_for_accessment";

    String KEY_USER_TYPE = "key_user_type";

    String KEY_NOTIFICATION = "key_notification";
    String KEY_NOTIFICATION_UNREAD = "key_count";

    String IS_BEFORE_LOGIN = "is_before_login";
    String MRN_KEY = "MRN";
    String PLATFORM = "Platform";
    String ACCEPT_LANGUAGE = "Accept-Language";
    String KEY_MRN = "key_mrn";
    String KEY_MRN_FOR_HHC = "key_mrn_for_hhc";
    String KEY_DEVICE_ID = "key_device_id";
    String KEY_REMEMBER = "key_remember";
    String KEY_REMEMBER_MRN = "key_remember_mrn";
    String KEY_REMEMBER_TYPE = "key_remember_type";
    String SESSION_ID = "session_id";

    String KEY_LAT = "key_lat";
    String KEY_LONG = "key_long";
    String KEY_CLINIC_ID = "key_clinic_id";
    String KEY_CLINIC_NAME = "key_clinic_name";
    String KEY_CLINIC_NAME_AR = "key_clinic_name_ar";
    String KEY_CACHE_TIME = "key_cache_time";
    String KEY_CHATBOT_LINK = "key_chatbot_link";

    String KEY_VIDEO_PHYSICIAN = "key_video_physician";
    String KEY_VIDEO_PHYSICIAN_NAME = "key_video_physician_name";
    String KEY_VIDEO_ROOM_ID = "key_video_room_id";
    String KEY_VIDEO_ACCESS_TOKEN = "key_video_access_token";
    String KEY_VIDEO_BACK_CLASS = "key_video_back_class";
    String KEY_EMERGENCY_CALL = "key_emergency_call";
    String KEY_VIDEO_ROOMNAME = "key_video_roomname";
    String KEY_CHAT_CHANNELNAME = "key_chat_channelname";
    String KEY_NAV_CLASS = "key_nav_class";


    String physician_search = "physician_search";
    String profile_update = "profile_update";

    String booking_successfully_booked = "booking_successfully_booked";
    int booking_successfully_booked_code = 883;


    @interface IntentKey {
        String INTENT_TYPE = "intent_type";
        String INTENT_LINK = "intent_link";
        String INTENT_REPORT_TYPE = "intent_report_type";

        String INTENT_PRIVACY = "intent_privacy";
        String INTENT_TERMS = "intent_terms";
        String INTENT_ABOUT_US = "intent_about_us";
        String INTENT_FILE = "intent_file";
    }

    String RECORD_SET = "10";
    String RECORD_SET_HUNDRED = "100";

    @interface BookingStatus {
        String UPCOMING = "2";
        String COMPLETED = "4";
        String CANCEL = "3";
        String NO_SHOW = "5";
    }

    interface Bundle{
        String episodeType = "episodeType";
        String rxNo = "rxNo";
        String episodeNo = "episodeNo";
        String orderNo = "orderNo";
        String orderId = "orderId";
    }

    String USER_TYPE = "USER";
    String GUEST_TYPE = "GUEST";
    String LOGIN_WITH_MRN = "1";
    String LOGIN_WITH_ID = "0";

    String NOTIFICATION_ON = "1";
    String NOTIFICATION_OFF = "0";

    @interface TYPE {
        String HHC = "hhc";
        String RECORD_MAIN = "main";
        String RECORD_LAB = "lab";
        String RECORD_RADIO = "radio";
        String RECORD_SICK = "sick";
        String RECORD_OPERATIVE = "operative";
        String RECORD_CARDIC = "cardic";
        String RECORD_DISCHARGE = "discharge";

        String HOME_PHYSICIAN = "home_physician";
        String REPORT_TYPE = "report";

    }

    @interface Filter {
        String REFRESH_HOME = "Refresh_Home";
        String REFRESH_MAIN = "Refresh_Main";
        String REFRESH_MAIN_NOTIFICATION = "Refresh_notification";
        String UPDATE_HOME = "UPDATE_HOME";
        String CHAT_NOTIFICATION = "CHAT_NOTIFICATION";
        String VIDEO_REJECT     = "VIDEO_REJECT";
        String REPORT_REQUEST     = "report_request";

    }

    @interface GUEST {
        String FIRST_NAME = "guest_first_name";
        String FAMILY_NAME = "guest_family_name";
        String FATHER_NAME = "guest_father_name";
        String GRAND_FATHER_NAME = "grand_father_name";

        String FIRST_NAME_AR = "guest_first_name_ar";
        String FAMILY_NAME_AR = "guest_family_name_ar";
        String FATHER_NAME_AR = "guest_father_name_ar";
        String GRAND_FATHER_NAME_AR = "grand_father_name_ar";

        String GENDER = "guest_gender";
        String LANG = "guest_lang";
        String DOB = "guest_dob";

        String IQAMA_ID = "guest_iqama_id";
        String PHONE = "guest_phone";
        String HOME_PHONE = "home_phone";

        String REASON_VISIT = "guest_reason_visit";
        String ACCESS_TOKEN = "access_token";

        String CITY = "city";
        String DISTRICT = "district";
        String COUNTRY = "country";
        String NATIONALITY = "nationality";

        String ER_FIRST_NAME = "er_first_name";
        String ER_FAMILY_NAME = "er_family_name";
        String ER_PHONE = "er_phone";
        String DOC_TYPE = "doc_type";
        String DOC_EXP_DATE = "doc_expiry_date";
        String MARTIAL_STATUS = "marital_status";

    }
    String KEY_PATIENT_ID = "key_patient_id";
    String KEY_PATIENT_ID_FOR_MEDICUS = "key_patient_id_for_medicus";

}
