package sa.med.imc.myimc.SignUp.presenter;


import android.graphics.Bitmap;

import java.io.File;

/*
 * SignUp Presenter interface to call API in SignUpImpl.
 */
public interface SignUpPresenter {
    void callSinUpAPI(String token,
                      String docNumber,
                      String docType,
                      String docExpiryDate,
                      String patName,
                      String patNameAr,
                      String familyName,
                      String familyNameAr,
                      String fatherName,
                      String fatherNameAr,
                      String grandfatherName,
                      String grandfatherNameAr,
                      String gender,
                      String telMobile,
                      String telHome,//
                      String addressDistrict,//
                      String addressCity,//
                      String addressCountry,//
                      String nationality,//
                      String dob,
                      String lang,
                      String erFirstName,
                      String erFamilyName,
                      String erPhone,
                      String comments,
                      String hasInsurance,
                      String insuranceCompany,
                      String insuranceCompanyCode,
                      String idCard,
                      String policyNumber,
                      String insuranceMemberId,
                      String insuranceExpDate,
                      String insuranceClass,
                      String insuranceAttachment,
                      String submitterName,
                      String relationToRegistrant,
                      Bitmap idCard_path,
                      Bitmap insuranceAttachmentPath,
                      String matritalStatus, int hosp);
    void callGetOtpApi(String phoneNumber,String iqama_id,String typ, int hosp);
    void callGetCompaniesApi(int hosp);
    void callGetNationalityApi();
    void callGetCityApi();
    void callGetDistrictApi();
    void callGetInformationApi(String token,String id,String dob);

}


