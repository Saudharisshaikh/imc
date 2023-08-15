package sa.med.imc.myimc.Network;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import sa.med.imc.myimc.AddGuardian.model.GuardianResponse;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Appointmnet.model.SessionDatesResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsNextResponse;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsResponse;
import sa.med.imc.myimc.Base.GenericResponse;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Departments.model.DepartmentDoctorResponse;
import sa.med.imc.myimc.Departments.model.DepartmentResponse;
import sa.med.imc.myimc.HealthSummary.model.AllergyResponse;
import sa.med.imc.myimc.HealthSummary.model.ReadingResponse;
import sa.med.imc.myimc.HealthTips.model.CategoryResponse;
import sa.med.imc.myimc.HealthTips.model.HealthTipsResponse;
import sa.med.imc.myimc.Home.model.ERInfoItem;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.PaymentResponse;
import sa.med.imc.myimc.Managebookings.model.VisitDetailReportResponse;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.Physicians.model.LanguageCMSResponse;
import sa.med.imc.myimc.Physicians.model.LanguageResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Profile.model.LastBookingResponse;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Questionaires.model.AssessmentResponse;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.Questionaires.model.FormsResponse;
import sa.med.imc.myimc.Questionaires.model.ResultResponse;
import sa.med.imc.myimc.Questionaires.model.ResultSingleResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Settings.model.ReportTypeResponse;
import sa.med.imc.myimc.Settings.model.Select_TypeResponse;
import sa.med.imc.myimc.SignUp.model.CityResponse;
import sa.med.imc.myimc.SignUp.model.CompaniesResponse;
import sa.med.imc.myimc.SignUp.model.DistrictResponse;
import sa.med.imc.myimc.SignUp.model.NationalityResponse;
import sa.med.imc.myimc.SignUp.model.UserDetailModel;
import sa.med.imc.myimc.UrlOpen.UrlContentResponse;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;
import sa.med.imc.myimc.healthcare.model.HealthCareListRequest;
import sa.med.imc.myimc.healthcare.model.HealthCareSubmission;
import sa.med.imc.myimc.medprescription.invoice.model.PrescriptiontInvoiceItem;
import sa.med.imc.myimc.medprescription.invoice.model.RequestPrescriptionInvoice;
import sa.med.imc.myimc.medprescription.model.PrescriptionResponse;
import sa.med.imc.myimc.pharmacy.model.PharmacyItem;
import sa.med.imc.myimc.pharmacy.model.PharmacyTelrResponse;
import sa.med.imc.myimc.pharmacy.model.ValidatePharmacyPaymentRequest;
import sa.med.imc.myimc.webView.model.ValidatePaymentRefRequest;

/**
 * Interface for all API call
 */

public interface WebService {

    @GET(WebUrl.GET_USER_INFO_WITH_ID)
    Call<UserDetailModel> getInformationFromID(@Query("idNumber") String idNumber,
                                               @Query("dateOfBirth") String dateOfBirth);


    @Headers({Constants.PLATFORM+": Android"})
    @POST(WebUrl.WEB_REGISTER)
    @Multipart
    Call<SimpleResponse> registerPatient(@Part("user") RequestBody user,
                                         @Part("idCard") RequestBody idCard);

    @Headers({Constants.PLATFORM+": Android"})
    @POST(WebUrl.WEB_REGISTER)
    @Multipart
    Call<SimpleResponse> registerPatientWithIsurance(@Part("user") RequestBody user,
                                                     @Part("idCard") RequestBody idCard,
                                                     @Part("insuranceAttachment") RequestBody insuranceAttachment);
//
//
//    @POST(WebUrl.WEB_REGISTER)
//    @Multipart
//    Call<SimpleResponse> registerPatient(@Header("Content-Type") String contentType,
//                                         @Header("Accept-Language") String acceptLanguage,
//                                         @Part("user") RequestBody body,
//                                         @Part MultipartBody.Part id_image);
//
//    @POST(WebUrl.WEB_REGISTER)
//    @Multipart
//    Call<SimpleResponse> registerPatientWithIsurance(@Header("Content-Type") String contentType,
//                                                     @Header("Accept-Language") String acceptLanguage,
//                                                     @Part("user") RequestBody body,
//                                                     @Part MultipartBody.Part id_image,
//                                                     @Part MultipartBody.Part insurance_image);

    @POST(WebUrl.REG_LOGIN)
    Call<LoginResponse> regLogin(@Body RequestBody body);

    @POST(WebUrl.REG_VALIDATE)
    Call<SimpleResponse> regValidate(@Body RequestBody body);

    @GET(WebUrl.GET_ALL_COMPANIES)
    Call<CompaniesResponse> getInsuranceCompanies();

    @GET(WebUrl.GET_NATIONALITY)
    Call<NationalityResponse> getNationalityCompanies();

    @GET(WebUrl.GET_CITY)
    Call<CityResponse> getCity();

    @GET(WebUrl.GET_DISTRICT)
    Call<DistrictResponse> getDistrict();

    @POST(WebUrl.AUTH_LOGIN)
    Call<LoginResponse> login(@Body RequestBody body);

    @POST(WebUrl.AUTH_RESEND_OTP)
    Call<LoginResponse> resendOtp(@Body RequestBody body);

    @POST(WebUrl.AUTH_VALIDATE)
    Call<ValidateResponse> loginValidate(@Body RequestBody body);

    @POST(WebUrl.AUTH_UPDATE_CONSENT)
    Call<SimpleResponse> updateConsent(@Path("mrNumber") String mrNumber, @Path("constantValue") String constantValue);

    @GET(WebUrl.API_GET_USER)
    Call<ProfileResponse> getUserInfo(@Path("userid") String userid, @Path("hosp") int hosp);

    @POST(WebUrl.API_UPDATE_USER)
    Call<SimpleResponse> updateUserInfo(@Body RequestBody body);

    @POST(WebUrl.ALL_CLINICS)
    Call<ClinicResponse> getAllClinics(@Body RequestBody body);

//    @POST(WebUrl.ALL_CLINICS_WEB)
//    Call<ClinicResponse> getAllWebClinics(@Body RequestBody body);

    @GET(WebUrl.ALL_CLINICS_WEB)
    Call<ClinicResponse> getAllWebClinics(@Query("hospitalCode") String hospitalCode);

    @POST(WebUrl.ALL_CLINICS_FILTER)
    Call<ClinicResponse> getAllClinicsFilter(@Body RequestBody body);

    @GET(WebUrl.ALL_DOCTORS)
    Call<PhysicianResponse> getAllDoctors(@Query("specialitycode") String specialitycode);

    @GET(WebUrl.ALL_DOCTORS_BY_HOSPITAL_CODE)
    Call<PhysicianResponse> getAllDoctorsByHospitalCode(@Query("hospitalCode") String hospitalCode);

    @GET(WebUrl.ALL_DOCTORS_WEB)
    Call<PhysicianResponse> getAllDoctorsWeb(@Query("specialitycode") String specialitycode);

    @GET(WebUrl.ALL_DOCTORS_SERVICE)
    Call<DrServiceResponse> getDoctorsService(@Query("providerCode") String providerCode, @Query("specialityCode") String specialityCode);

    @GET(WebUrl.ALL_DOCTORS_SLOT)
    Call<DrTimeSlot> getDoctorsTimeSlot(@Query("datefrom") String datefrom, @Query("dateto") String dateto, @Query("providercode") String providercode, @Query("servicecode") String servicecode, @Query("specialitycode") String specialitycode);

    @POST(WebUrl.BOOK_APPOINTMENT_NEW)
    Call<BookResponse> callBookingAPT(@Body RequestBody body,
                                      @Query("providerCode") String providerCode,
                                      @Query("serviceCode") String serviceCode,
                                      @Query("specialityCode") String specialityCode,
                                      @Query("hospitalCode") String hospitalCode,
                                      @Query("slotDate") String slotDate
    );

    @POST(WebUrl.ALL_DOCTORS_FILTER)
    Call<PhysicianResponse> getAllDoctorsFilters(@Body RequestBody body);

//    @POST(WebUrl.FETCH_DOCTOR_INFO)
//    Call<PhysicianDetailResponse> getDoctorInfo(@Body RequestBody body);

//    @POST(WebUrl.FETCH_DOCTOR_INFO_WEB)
//    Call<PhysicianDetailResponse> getDoctorInfoWeb(@Body RequestBody body);

    @GET(WebUrl.FETCH_DOCTORS_OF_CLINIC)
    Call<PhysicianResponse> getDoctorByClinicID(@Path("clinic_id") String clinic_id);

    @GET(WebUrl.ALL_LANGUAGES)
    Call<LanguageResponse> getLanguages();

    @GET(WebUrl.DEPT_ALL)
    Call<LanguageResponse> getDeptAll();

//    =======================Booking list

    @POST(WebUrl.ALL_BOOKINGS)
    Call<BookingResponse> getAllBookings(@Body RequestBody body);

    @POST(WebUrl.ALL_BOOKINGS)
    Call<BookingResponseNew> getAllBookingsNew(@Body RequestBody body);

    @POST(WebUrl.VERIFY_PAYMENT)
    Call<BookResponse> verifyPayment(@Body RequestBody body);


    @GET(WebUrl.GET_APPOINTMENT_PRICE)
    Call<PriceResponse> getBookingPrice(@Path("sessionId") String sessionId, @Path("apptDate") String apptDate, @Path("hosp") int hosp);

//    TODO check API
    @GET(WebUrl.GET_SELFCHECKIN_PRICE)
    Call<PriceResponse> getCheckInPrice(@Path("sessionId") String sessionId, @Path("apptDate") String apptDate);

    @GET(WebUrl.CANCEL_BOOKING)
    Call<SimpleResponse> cancelBooking(@Path("bookingid") String booking_id, @Path("mrnNumber") String mrnNumber, @Path("hosp") int hosp);

    @POST(WebUrl.RESCHEDULE_BOOKING)
    Call<BookResponse> rescheduleBooking(@Body RequestBody body);

    @POST(WebUrl.FETCH_REPORTS_COUNT)
    Call<VisitDetailReportResponse> getReportsCount(@Body RequestBody body);

    @POST(WebUrl.DOWNLOAD_CONFIRMATION)
    Call<ResponseBody> downloadConfirmBooking(@Body RequestBody body);

//========================Book Appointment APIs===============================

    @POST(WebUrl.FETCH_SESSION_DATES)
    Call<SessionDatesResponse> getSessionDates(@Body RequestBody body);

    @POST(WebUrl.FETCH_TIME_SLOTS)
    Call<TimeSlotsResponse> getTimeSlots(
                                         @Body RequestBody body);

    /* @Header("Accept-Language") String language,
                                 @Header("platform") String platform,
                                 @Header("Authorization") String token,
                                 @Header("isGuardian") String guardian,*/

    @POST(WebUrl.FETCH_TIME_SLOTS)
    Call<TimeSlotsResponse> getFirstTimeTimeSlots(  @Header("Accept-Language") String language,
                                                    @Header("platform") String platform,
                                                    @Header("Authorization") String token,
                                                    @Header("isGuardian") String guardian,
                                                    @Body RequestBody body);

    @POST(WebUrl.BOOK_APPOINTMENT)
    Call<BookResponse> bookAppointment(@Body RequestBody body);


//    ======================Reports====================================


    @POST(WebUrl.FETCH_LAB_REPORTS)
    Call<ResponseBody> fetchLabReports(@Body RequestBody body);

    @POST(WebUrl.FETCH_LAB_REPORTS_MEDICUS)
    Call<ResponseBody> fetchLabReportsMedicus();


    @POST(WebUrl.FETCH_MEDICAL_REPORTS)
    Call<ResponseBody> fetchMedReports(@Body RequestBody body);

    @POST(WebUrl.FETCH_SICK_LEAVE_REPORTS)
    Call<SickLeaveResponse> fetchSickLeaveReports(@Body RequestBody body);

    @POST(WebUrl.GENERATE_MEDICAL_REPORT)
    Call<ResponseBody> generateMedReport(@Body RequestBody body);

    @POST(WebUrl.GENERATE_LAB_REPORT)
    Call<ResponseBody> generateLabReport(@Body RequestBody body);


    @POST(WebUrl.GENERATE_SMART_LAB_REPORT)
    Call<ResponseBody> generateSmartLabReport(@Body RequestBody body);


    @POST(WebUrl.GENERATE_SICK_LEAVE_REPORT)
    Call<ResponseBody> generateSickLeaveReport(@Body RequestBody body);

    @POST(WebUrl.GENERATE_PAYMENT_REPORT)
    Call<ResponseBody> generatePaymentReport(@Body RequestBody body);

    @POST(WebUrl.FETCH_OPERATIVE_REPORTS)
    Call<ResponseBody> fetchOperativeReports(@Body RequestBody body);

    @POST(WebUrl.GENERATE_OPERATIVE_REPORTS)
    Call<ResponseBody> generateOperativeReport(@Body RequestBody body);

    @POST(WebUrl.FETCH_DISCHARGE_REPORTS)
    Call<ResponseBody> fetchDischargeReports(@Body RequestBody body);

    @POST(WebUrl.GENERATE_DISCHARGE_REPORTS)
    Call<ResponseBody> generateDischargeReport(@Body RequestBody body);

    @POST(WebUrl.FETCH_CARD_REPORTS)
    Call<ResponseBody> fetchCardioReports(@Body RequestBody body);

    @POST(WebUrl.GENERATE_CARD_REPORTS)
    Call<ResponseBody> generateCardioReport(@Body RequestBody body);
//=============================================================================

    @POST(WebUrl.GET_MEDICATION)
    Call<MedicationRespone> fetchMedication(@Body RequestBody body);

    @GET(WebUrl.GET_ALLERGIES)
    Call<AllergyResponse> fetchAllergies(@Query("patientId") String patientId, @Query("mrn") String mrn);


    @GET(WebUrl.GET_READINGS)
    Call<ReadingResponse> fetchAllReadings(@Query("mrn") String mrn, @Query("page") String page);

    @POST(WebUrl.GET_PRESCRIPTION)
    Call<PrescriptionResponse> fetchPrescription(@Body RequestBody body);

    @POST(WebUrl.GENERATE_PRESCRIPTION)
    Call<ResponseBody> generatePrescription(@Body RequestBody body);

//=======================Rating, contact us, change language========================

    @GET(WebUrl.CHANGE_LANGUAGE)
    Call<SimpleResponse> changeLanguage(@Path("userid") String userid, @Path("lang") String lang, @Path("hosp") int hosp);

    @POST(WebUrl.CONTACT_US_NEW)
    Call<SimpleResponse> contactUsWeb(@Body RequestBody body);

//    @POST(WebUrl.REQUEST_FOR_REPORT)
//    Call<SimpleResponse> requestForReport(@Body RequestBody body);
/*["pickupDob": , "copyType": email, "mrNumber": 000321, "reportTypeId": 30, "episodeType": OP,
 "pickupId": , "terms": 1, "message": Jk, "file": , "pickupName": , "episodeNo": 14143096, "infoCorrect": 1]*/
    @POST(WebUrl.REQUEST_FOR_REPORT)
    @Multipart
    Call<JSONObject> requestForReport(@Body RequestBody body);
    /*@Part MultipartBody.Part id_image);*/
    /*
  /*  @Multipart*//*@Header("Content-Type") String content,*/

    @POST(WebUrl.SEND_OTP)
    Call<SimpleResponse> sendOTP(
                                 @Header("Accept-Language") String language,
                                 @Header("platform") String platform,
                                 @Header("Authorization") String token,
                                 @Header("isGuardian") String guardian,
                                 @Body RequestBody body);
                                /* @Part("patid") RequestBody patient_id,
                                 @Part("email") RequestBody email_value*///);
                                 //@Body RequestBody body);

    @POST(WebUrl.VERIFY_EMAIL)
    /*  @Multipart*//*@Header("Content-Type") String content,*/
    Call<SimpleResponse>verifyOTP(
            @Header("Accept-Language") String language,
            @Header("platform") String platform,
            @Header("Authorization") String token,
            @Header("isGuardian") String guardian,
            @Body RequestBody body);

    @POST(WebUrl.REQUEST_FOR_REPORT)
    @Multipart
    Call<SimpleResponse> requestForReport(@Part("mrNumber") RequestBody mrNumber,
                                          @Part("episode") RequestBody episode,
                                          @Part("appointmentDate") RequestBody appointmentDate,
                                          @Part("reportType") RequestBody reportType,
                                          @Part("email") RequestBody email,
                                          @Part("subject") RequestBody subject,
                                          @Part("copyType") RequestBody copyType,
                                          @Part("message") RequestBody message,
                                          @Part("isGeneral") RequestBody isGeneral,
                                          @Part("phoneNumber") RequestBody phoneNumber,
                                          @Part("reportTypeId") RequestBody reportTypeId);

    @GET(WebUrl.GET_PHONE_NUMBER)
    Call<SimpleResponse> getPhoneNumber(@Path("hosp") int hosp);

    @POST(WebUrl.RATE_DOCTOR)
    Call<SimpleResponse> rateDoctor(@Body RequestBody body);

    @GET(WebUrl.LOG_OUT)
    Call<SimpleResponse> logOut(@Path("userid") String userid, @Path("deviceToken") String deviceToken,@Path("hosp") int hosp);

    @GET(WebUrl.CONTENT_LINK)
    Call<UrlContentResponse> getContents(@Path("id") String id);

    @GET(WebUrl.GET_REPORT_TYPE)
    Call<ReportTypeResponse> getReportTypes();

    @GET(WebUrl.GET_SELECT_VISIT)
    Call<Select_TypeResponse> getSelecteVisit(@Query("patientId") String patientId, @Query("mrnNumber") String mrnNumber);

    @GET(WebUrl.GET_TRANSLATION_REPORTS)
    Call<ReportTypeResponse> getTransaltionReportTypes(@Path("hosp") int hosp);

    //=======================Notifications================================

    @POST(WebUrl.GET_ALL_NOTIFICATIONS)
    Call<NotificationResponse> getAllNotifications(@Body RequestBody body);

    @GET(WebUrl.DELETE_NOTIFICATIONS)
    Call<SimpleResponse> deleteNotification(@Path("notification_id") String notification_id, @Path("hosp") int hosp);

    @GET(WebUrl.NOTIFICATION_ON_OFF)
    Call<SimpleResponse> onOffNotification(@Path("mrn") String mrn,
                                           @Path("on_off_param") String on_off_param, @Path("hosp") int hosp);

    @GET(WebUrl.GET_BOOKING_ID)
    Call<LastBookingResponse> getBookingId(@Path("mrnumber") String mrnumber);

    @GET(WebUrl.CLEAR_ALL_NOTIFICATIONS)
    Call<SimpleResponse> clearAll(@Path("mrnumber") String mrnumber, @Path("hosp") int hosp);

    @GET(WebUrl.READ_NOTIFICATIONS)
    Call<SimpleResponse> readNotification(@Path("patid") String patid, @Path("notiId") String notiId,  @Path("hosp") int hosp);

    //=======================Departments================================

    @POST(WebUrl.DEPARTMENT_LIST)
    @FormUrlEncoded
    Call<DepartmentResponse> getAllDepartments(@Query("page") String page, @Field("name") String name);

    @GET(WebUrl.DEPARTMENT_DOC)
    Call<DepartmentDoctorResponse> getDepartmentDoctors(@Path("id") String id);

    @GET(WebUrl.DOCTOR_DETAIL)
    Call<PhysicianCompleteDetailCMSResponse> getDoctorFullDetailCMS(@Path("id") String id);

    @GET(WebUrl.LANGUAGES_LIST)
    Call<LanguageCMSResponse> getAllLanguagesCMS();


    @GET(WebUrl.HEALTH_CATEGORY)
    Call<CategoryResponse> getAllCategoriesCMS();


    @POST(WebUrl.SEARCH_HEALTH_TIPS)
    @FormUrlEncoded
    Call<HealthTipsResponse> getAllHealthTips(@Query("page") String page, @Field("name") String name, @Field("category_id") String category);


//    ===================================Add/Remove Guardian==============================

    @POST(WebUrl.ADD_GUARDIAN)
    Call<SimpleResponse> addGuardian(@Body RequestBody body);

    @GET(WebUrl.GET_GUARDIAN)
    Call<GuardianResponse> getGuardian(@Path("MRN_NUMBER") String mrNumber, @Path("hosp") int hosp);

    @POST(WebUrl.REMOVE_GUARDIAN)
    Call<SimpleResponse> removeGuardian(@Body RequestBody body);

    @POST(WebUrl.LINK_UNLINK_GUARDIAN)
    Call<SimpleResponse> linkUnlinkGuardian(@Body RequestBody body);

    @POST(WebUrl.EDIT_GUARDIAN)
    Call<GuardianResponse> updateGuardian(@Body RequestBody body);


//    ==================================Guest API calls==============================

    @POST(WebUrl.GUEST_LOGIN)
    Call<LoginResponse> guestLogin(@Body RequestBody body);

    @POST(WebUrl.GUEST_VALIDATE)
    Call<SimpleResponse> guestValidate(@Body RequestBody body);

    @POST(WebUrl.GUEST_FETCH_SESSION_DATES)
    Call<SessionDatesResponse> getGuestSessionDates(@Body RequestBody body);

    @POST(WebUrl.GUEST_FETCH_TIME_SLOTS)
    Call<TimeSlotsResponse> getGuestTimeSlots(@Body RequestBody body);

    @POST(WebUrl.GUEST_BOOK_APPOINTMENT)
    Call<BookResponse> bookAppointmentGuest(@Body RequestBody body);


    //    ==================================FORMS API calls==============================


    @GET(WebUrl.GET_TOKEN)
    Call<SimpleResponse> getTokens(@Path("pat_ID") String pat_ID, @Path("hosp") int hosp);
    
    @POST(WebUrl.API_START_ASSESSMENT)
    Call<AssessmentResponse> startAssessment(@Body RequestBody body);

    @POST(WebUrl.API_FORM_ASSESSMENT)
    Call<AssessmentResponse> nextAssessment(@Body RequestBody body);

    @POST(WebUrl.API_SCORE_ASSESSMENT)
    Call<ResultResponse> getAssessmentResult(@Body RequestBody body);

    @POST(WebUrl.GET_COMPLETED_ASSESSMENT)
    Call<CompletedResponse> getListCompletedAssessment(@Body RequestBody body);

    @POST(WebUrl.GET_FORMS)
    Call<FormsResponse> getListAssessment(@Body RequestBody body);

    @POST(WebUrl.VIEW_RESULT)
    Call<ResultSingleResponse> viewResult(@Body RequestBody body);


//    =============================Check In APIs==================================

    @POST(WebUrl.CHECK_IN_PAY_ONLY)
    Call<SimpleResponse> checkInPayOnly(@Body RequestBody body);

    @POST(WebUrl.CHECK_IN_PAYMENT)
    Call<PaymentResponse> checkInPayment(@Body RequestBody body);

    @POST(WebUrl.CONFIRM_ARRIVAL)
    Call<SimpleResponse> confirmArrival(@Body RequestBody body);

    @POST(WebUrl.CHECK_IN_VERIFY_PAYMENT)
    Call<BookResponse> verifyPaymentCheckIn(@Body RequestBody body);

    @POST(WebUrl.GET_NEXT_AVAILABLE_SLOT)
    Call<NextTimeResponse> getNextAvailableTimeSlot(@Body RequestBody body);

    @POST(WebUrl.FETCH_ALTERNATE_PHYSICIANS)
    Call<TimeSlotsNextResponse> fetchAlternatePhysicians(@Body RequestBody body);


    //    =============================Medicus APIs==================================

    @GET(WebUrl.GET_PIN)
    Call<PinResponse> getPinForMedicus(@Query("mrnNumber") String mrnNumber);


    //  ===============================Parvinder DEv APis=============================
    @POST(WebUrl.ALL_DOCTORS)
    Single<PhysicianResponse> getAllDoctorss(@Body RequestBody body);

    @POST(WebUrl.ALL_DOCTORS_WEB)
    Single<PhysicianResponse> getAllDoctorsWebb(@Body RequestBody body);

    @POST(WebUrl.GUEST_FETCH_TIME_SLOTS)
    Single<TimeSlotsResponse> getGuestTimeSlotss(@Body RequestBody body);

    @POST(WebUrl.FETCH_TIME_SLOTS)
    Single<TimeSlotsResponse> getTimeSlotss(@Body RequestBody body);


    @POST(WebUrl.FETCH_ALTERNATE_PHYSICIANS)
    Observable<TimeSlotsNextResponse> fetchAlternatePhysicianss(@Body RequestBody body);

    @Headers("content-type:application/json")
    @POST(WebUrl.HEALTH_CARE_LIST)
    Call<GenericResponse<List<HealthCareItem>>> fetchHealthCareList(@Body HealthCareListRequest request);

    @Headers("content-type:application/json")
    @POST(WebUrl.HEALTH_CARE_SUBMISSION)
    Call<GenericResponse<String>> healthCareSubmission(@Body HealthCareSubmission request);

    @GET(WebUrl.GET_PRESCRIPTION_MEDS)
    Call<List<PharmacyItem>> fetchPrescriptionMeds(@Path("patId") String patId,
                                                   @Path("hosp") String hosp,
                                                   @Path("rxNo") String rxNo,
                                                   @Path("episodeNo") String episodeNo);

    @Headers("Content-Type:application/json")
    @POST(WebUrl.VALIDATE_PHARM_D)
    Call<PharmacyTelrResponse> validatePharmD(@Body ValidatePharmacyPaymentRequest request);

    @Headers("Content-Type:application/json")
    @POST(WebUrl.VALIDATE_PHARM_D_PAYMENT_REF)
    Call<JsonObject> validatePaymentRef(@Body ValidatePaymentRefRequest request);

    @Headers("Content-Type:application/json")
    @GET(WebUrl.APPOINTMENT_Q_INFO)
    Call<GenericResponse<List<ERInfoItem>>> appointmentInfo();

    @Headers("Content-Type:application/json")
    @POST(WebUrl.GET_INVOICES)
    Call<GenericResponse<List<PrescriptiontInvoiceItem>>> getPrescriptionInvoices(@Body RequestPrescriptionInvoice request);

    @Headers("Content-Type:application/json")
    @Streaming
    @POST(WebUrl.GENERATE_PRESCRIPTION_INVOICE)
    Call<ResponseBody> generatePrescriptionInvoice(@Body RequestPrescriptionInvoice request);

}


