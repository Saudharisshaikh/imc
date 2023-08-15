package sa.med.imc.myimc.SignUp.presenter;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;
import static sa.med.imc.myimc.globle.UrlWithURLDecoder.registration2;

import android.app.Activity;
import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.CadiologyReportOldModel;
import sa.med.imc.myimc.SignUp.model.CityResponse;
import sa.med.imc.myimc.SignUp.model.CompaniesResponse;
import sa.med.imc.myimc.SignUp.model.DistrictResponse;
import sa.med.imc.myimc.SignUp.model.NationalityResponse;
import sa.med.imc.myimc.SignUp.model.UserDetailModel;
import sa.med.imc.myimc.SignUp.view.SignUpViews;
import sa.med.imc.myimc.Utils.FileUtils;

/**
 * Sign Up API implementation class.
 */

public class SignUpImpl implements SignUpPresenter {

    private Activity activity;
    private SignUpViews views;

    public SignUpImpl(SignUpViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }



    public void register(String user, String idCard,String insuranceAttachment){
//        views.showLoading();

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        Log.e("abcd",user);
        Log.e("abcd",idCard);
        Log.e("abcd",insuranceAttachment);
        Log.e("abcd",registration2);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, registration2,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        views.hideLoading();

                        Log.e("abcd",response);
                        SimpleResponse response1 =new Gson().fromJson(response, new TypeToken<SimpleResponse>() {
                        }.getType());

                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onSignUp(response1);
                        } else
                            views.onFail(response1.getMessage());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("abcd",error.toString());

                        views.hideLoading();
                        views.hideLoading();
                        String message = error.getMessage();
                        views.onFail(message);

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user",user);
                params.put("idCard",idCard);
                params.put("insuranceAttachment", insuranceAttachment);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void callSinUpAPI(String token, String docNumber,
                             String docType,
                             String docExpiryDate,
                             String patName, String patNameAr,
                             String familyName, String familyNameAr,
                             String fatherName,
                             String fatherNameAr,
                             String grandfatherName,
                             String grandfatherNameAr, String gender,
                             String telMobile, String telHome,
                             String addressDistrict,
                             String addressCity,
                             String addressCountry,
                             String nationality,
                             String dob, String lang,
                             String erFirstName,
                             String erFamilyName, String erPhone,
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
                             String matritalStatus, int hosp) {
        views.showLoading();
        telMobile = WebUrl.COUNTRY_CODE + telMobile;

        if (telHome.length() > 0)
            telHome = WebUrl.COUNTRY_CODE + telHome;

        JSONObject object = new JSONObject();
        try {
            object.put("docNumber", docNumber);
            object.put("docType", docType);
            object.put("docExpiryDate", docExpiryDate);

            object.put("firstName", patName);
            object.put("firstNameAr", patNameAr);
            object.put("grandfatherName", grandfatherName);
            object.put("grandfatherNameAr", grandfatherNameAr);
            object.put("familyName", familyName);
            object.put("familyNameAr", familyNameAr);

            object.put("fatherName", fatherName);
            object.put("fatherNameAr", fatherNameAr);

            object.put("gender", gender);
            object.put("language", lang);
            object.put("dob", dob);

            object.put("addressDistrict", addressDistrict);
            object.put("addressCity", addressCity);
            object.put("addressCountry", addressCountry);
            object.put("nationality", nationality);

            object.put("visitReason", comments);
            object.put("telMobile", telMobile);
            object.put("telHome", telHome);

            object.put("erFirstName", erFirstName);
            object.put("erFamilyName", erFamilyName);
            object.put("erPhone", erPhone);

            object.put("hasInsurance", hasInsurance);
            object.put("idCard", idCard);
            object.put("insuranceCompany", insuranceCompany);
            object.put("insuranceCompanyCode", insuranceCompanyCode);
            object.put("insurancePolicyNumber", policyNumber);
            object.put("insuranceMemberId", insuranceMemberId);
            object.put("insuranceExpDate", insuranceExpDate);
            object.put("insuranceClass", insuranceClass);
            object.put("insuranceAttachment", insuranceAttachment);
            object.put("relationToRegistrant", relationToRegistrant);
            object.put("submitterName", submitterName);
            object.put("maritalStatus", matritalStatus);

            object.put("accept", true);
            object.put("consentChecked", true);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        MultipartBody.Part idCardPart = prepareImgFilePart(idCard_path, "idCard");
//        RequestBody user = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

//        register(object.toString(),convertToString(idCard_path),convertToString(insuranceAttachmentPath));
        WebService webService = ServiceGenerator.createService(WebService.class, token);
//        MultipartBody.Part insurancePart = null;
        Call<SimpleResponse> xxx;


        try {
            if (insuranceAttachmentPath!=null) {
                RequestBody user = RequestBody.create(MediaType.parse("text/plain"), object.toString());
                RequestBody idCardB = RequestBody.create(MediaType.parse("text/plain"),  convertToString(idCard_path));
                RequestBody insuranceAttachmentB = RequestBody.create(MediaType.parse("text/plain"),  convertToString(insuranceAttachmentPath));

//                insurancePart = prepareImgFilePart(insuranceAttachmentPath, "insuranceAttachment");
                xxx = webService.registerPatientWithIsurance(user,idCardB,insuranceAttachmentB);

            } else {
                RequestBody user = RequestBody.create(MediaType.parse("text/plain"), object.toString());
                RequestBody idCardB = RequestBody.create(MediaType.parse("text/plain"),  convertToString(idCard_path));


                xxx = webService.registerPatient(user,idCardB);
            }
        } catch (Exception e) {

            RequestBody user = RequestBody.create(MediaType.parse("text/plain"), object.toString());
            RequestBody idCardB = RequestBody.create(MediaType.parse("text/plain"),  convertToString(idCard_path));

            xxx = webService.registerPatient(user,idCardB);
        }

//        UploadFormData uploadFormData=new UploadFormData();
//        JSONObject jsonObject=uploadFormData.uploadImage(xxx.request().url().toString(),object.toString(),idCard_path,insuranceAttachmentPath);
////        Log.e("abcd",jsonObject.toString());
//
        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onSignUp(response1);
                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                views.hideLoading();
                String message = t.getMessage();

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    // message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

    String convertToString(Bitmap bitmap1){

        Bitmap bitmap=getResizedBitmap(bitmap1,480,640);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream .toByteArray();


        Log.e("abcd", String.valueOf(byteArray.length));

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    public static byte[] getCompressedBitmapData(Bitmap bitmap, int maxFileSize, int maxDimensions) {
        Bitmap resizedBitmap;
        if (bitmap.getWidth() > maxDimensions || bitmap.getHeight() > maxDimensions) {
            resizedBitmap = getResizedBitmap(bitmap,
                    maxDimensions);
        } else {
            resizedBitmap = bitmap;
        }

        byte[] bitmapData = getByteArray(resizedBitmap);

        while (bitmapData.length > maxFileSize) {
            bitmapData = getByteArray(resizedBitmap);
        }
        return bitmapData;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,
                width,
                height,
                true);
    }

    private static byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,
                80,
                bos);

        return bos.toByteArray();
    }


    public byte[] read(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        }finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }





    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void callGetOtpApi(String phoneNumber, String iqama_id, String typ, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);
        phoneNumber = WebUrl.COUNTRY_CODE + phoneNumber;

        JSONObject object = new JSONObject();
        try {
            object.put("mobileNumber", phoneNumber);
            object.put("docNumber", iqama_id);
            object.put("docType", typ);
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        Call<LoginResponse> xxx = webService.regLogin(body);
        xxx.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    LoginResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus()) {
                            views.onSuccessOTP(response1);
                        }

                        views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });

    }

    @Override
    public void callGetNationalityApi() {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        Call<NationalityResponse> xxx = webService.getNationalityCompanies();
        xxx.enqueue(new Callback<NationalityResponse>() {
            @Override
            public void onResponse(Call<NationalityResponse> call, Response<NationalityResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    NationalityResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onGetNationality(response1);
                        } else

                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<NationalityResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });

    }

    @Override
    public void callGetCompaniesApi(int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        Call<CompaniesResponse> xxx = webService.getInsuranceCompanies();
        xxx.enqueue(new Callback<CompaniesResponse>() {
            @Override
            public void onResponse(Call<CompaniesResponse> call, Response<CompaniesResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    CompaniesResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("Success")) {
                            List<CompaniesResponse.InsuranceList> insurance_list=new ArrayList<>();

                            response1.insurance_list.forEach(insuranceList -> {
                                if (!insuranceList.getInsurance_description().contains("Self Pay"))
                                    insurance_list.add(insuranceList);
                            });

                            response1.setInsurance_list(insurance_list);

                            views.onGetComapanies(response1);
                        } else

                            views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<CompaniesResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });

    }

    @Override
    public void callGetCityApi() {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        Call<CityResponse> xxx = webService.getCity();
        xxx.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    CityResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onGetCity(response1);
                        } else

                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetDistrictApi() {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class);

        Call<DistrictResponse> xxx = webService.getDistrict();
        xxx.enqueue(new Callback<DistrictResponse>() {
            @Override
            public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    DistrictResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onGetDistrict(response1);
                        } else

                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<DistrictResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetInformationApi(String token, String id, String dob) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.TEMP_BASE_URL, token);

        Call<UserDetailModel> xxx = webService.getInformationFromID(id, dob);
        xxx.enqueue(new Callback<UserDetailModel>() {
            @Override
            public void onResponse(Call<UserDetailModel> call, Response<UserDetailModel> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    UserDetailModel response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onGetInfo(response1);
                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<UserDetailModel> call, Throwable t) {
                views.hideLoading();
                String message = t.getMessage();

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    // message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareImgFilePart(String filee, String name) {



        File file = new File(filee);
        String mime = FileUtils.getMimeType(file);
        Log.e("mime", "" + mime);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mime), file);
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);

    }
}
