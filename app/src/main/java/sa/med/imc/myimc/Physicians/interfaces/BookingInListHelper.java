package sa.med.imc.myimc.Physicians.interfaces;

import static sa.med.imc.myimc.Network.Constants.ARABIC;
import static sa.med.imc.myimc.Network.Constants.ENGLISH;
import static sa.med.imc.myimc.Network.Constants.PATIENT_ID;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked;
import static sa.med.imc.myimc.Network.Constants.booking_successfully_booked_code;
import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import sa.med.imc.myimc.Appointmnet.model.BookResponse;
import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Base.BaseBottomSheet;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.custom_dailog.CustomDailogBuilder;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.SessionExpired;
import sa.med.imc.myimc.globle.interfaces.ServicePriceListner;
import sa.med.imc.myimc.globle.model.RequestBodyForPaymentUrlGeneration;
import sa.med.imc.myimc.globle.model.ServicePrice;

public class BookingInListHelper {

    Activity context;
    String token;
    RequestQueue requestQueue;
    CustomDailogBuilder progressDialog;

    public BookingInListHelper(Activity context) {
        this.context = context;
        token= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_ACCESS_TOKEN, "");
        requestQueue = Volley.newRequestQueue(context);
        progressDialog = new CustomDailogBuilder(context);
    }

    public void fetchServiceList(String providercode, String specialityCode,GetDoctorService getDoctorService) {

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<DrServiceResponse> xxx = webService.getDoctorsService(providercode, specialityCode);


        String Url = xxx.request().url().toString();
        Log.e("abcd",Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("abcd", response);
                DrServiceResponse response1 = new Gson().fromJson(response, new TypeToken<DrServiceResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("Success")) {
                        getDoctorService.onGetServiceList(response1);
                    } else
                        getDoctorService.onFail(response1.getStatus());
                } else {
                    getDoctorService.onFail(context.getResources().getString(R.string.plesae_try_again));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SessionExpired((Activity) context,error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                getDoctorService.onFail(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void fetchDrTimeSlot(String datefrom, String dateto, String providercode,
                                String servicecode, String specialityCode,GetTimeSlot getTimeSlot) {

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<DrTimeSlot> xxx = webService.getDoctorsTimeSlot(datefrom, dateto, providercode, servicecode, specialityCode);

        Log.e("abcd",xxx.request().url().toString());


        String Url = xxx.request().url().toString();
        Log.e("abcd",Url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abcd", response);
                DrTimeSlot response1 = new Gson().fromJson(response, new TypeToken<DrTimeSlot>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("Success")) {
                        getTimeSlot.onGetTimeSlot(response1);
                    } else
                        getTimeSlot.onFail(response1.getStatus());
                } else {
                    getTimeSlot.onFail(context.getResources().getString(R.string.plesae_try_again));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SessionExpired((Activity) context,error);

                Log.e("abcd", error.toString());
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                getTimeSlot.onFail(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void callBookingApt(PhysicianResponse.ProviderList physician,String token, String PatientID, String SlotId,
                               String providerCode,
                               String serviceCode,
                               String specialityCode,
                               String hospitalCode,
                               String slotDate,
                               String slotFromTime,
                               int hosp,String providerDescription,
                               String arabicProviderDescription,String type,
                               String sessionId,String specialityDescription,String fromDate,String bookingslotTime,
                               String consultationtype,String serviceName,String timeStamp) {
        showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("patientId", PatientID);
            object.put("slotBookingId", SlotId);
            object.put("providerDescription", providerDescription);
            object.put("arabicProviderDescription", arabicProviderDescription);
            object.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("abcd",object.toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookResponse> xxx = webService.callBookingAPT(body, providerCode, serviceCode, specialityCode, hospitalCode
                ,slotDate);

        String url=xxx.request().url().toString()+"&slotFromTime="+slotFromTime;

        Log.e("abcd",url);

        final String mRequestBody =object.toString();
        Log.e("abcd",mRequestBody);

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("abcd",response);
                hideLoading();
                String lang= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").trim().equalsIgnoreCase("forbidden")){
                        if (lang.equalsIgnoreCase(Constants.ENGLISH)){
                            onFail(jsonObject.getString("englishMessage"));
                        }else {

                            onFail(jsonObject.getString("arabicMessage"));
                        }
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {

                            try {
                                if (response1.getPaymentStatus().equalsIgnoreCase("FALSE")){
                                    Intent in = new Intent(context, MainActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                                    context.startActivity(in);
                                    context.finish();
                                }else {
                                    onNewBook(physician,response1,token,PatientID,SlotId,providerCode,serviceCode,specialityCode,
                                            hospitalCode,slotDate,slotFromTime,hosp,providerDescription,arabicProviderDescription,
                                            type,sessionId,specialityDescription,fromDate,bookingslotTime,consultationtype,serviceName,timeStamp);
                                }
                            } catch (Exception exception) {
                                Intent in = new Intent(context, MainActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                                context.startActivity(in);
                                context.finish();
                            }


                        } else
                            onFail(response1.getMessage());
                    } else {
                        onFail(context.getResources().getString(R.string.plesae_try_again));
                    }
                    return;
                }
                BookResponse response1 = new Gson().fromJson(response, new TypeToken<BookResponse>() {}.getType());
                if (response1 != null) {
                    if (response1.getStatus().equalsIgnoreCase("true")) {
                        try {
                            if (response1.getPaymentStatus().equalsIgnoreCase("FALSE")){
                                Intent in = new Intent(context, MainActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                                context.startActivity(in);
                                context.finish();
                            }else {
                                onNewBook(physician,response1,token,PatientID,SlotId,providerCode,serviceCode,specialityCode,
                                        hospitalCode,slotDate,slotFromTime,hosp,providerDescription,arabicProviderDescription,
                                        type,sessionId,specialityDescription,fromDate,bookingslotTime,consultationtype,serviceName,timeStamp);
                            }
                        } catch (Exception e) {

                            Intent in = new Intent(context, MainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.putExtra(booking_successfully_booked,booking_successfully_booked_code);
                            context.startActivity(in);
                            context.finish();
                        }

                    } else
                        onFail(response1.getMessage());
                } else {
                    onFail(context.getResources().getString(R.string.plesae_try_again));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SessionExpired((Activity) context,error);

                hideLoading();
                String message = error.toString();
                onFail(message);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                headers.put(Constants.PLATFORM, "Android");

                headers.put(Constants.ACCEPT_LANGUAGE, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE,ENGLISH));
                headers.put(Constants.HOSPITAL_CODE_FOR_HEADER, SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE,""));
                headers.put(Constants.MRN_KEY, SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_MRN,""));

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(stringRequest);

    }

    private void onFail(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


  private void
  onNewBook(PhysicianResponse.ProviderList physician,BookResponse response1,String token, String PatientID, String SlotId,
                           String providerCode,
                           String serviceCode,
                           String specialityCode,
                           String hospitalCode,
                           String slotDate,
                           String slotFromTime,
                           int hosp,String providerDescription,
                           String arabicProviderDescription,String type,
                           String sessionId,String specialityDescription,String fromDate,String bookingslotTime,
                           String consultationtype,String serviceName,String timeStamp) {


        MyhttpMethod myhttpMethod = new MyhttpMethod(context);

        myhttpMethod.getServicePrice(sessionId, true, new ServicePriceListner() {
            @Override
            public void onSuccess(ServicePrice servicePrice) {
                double patientShare = Double.parseDouble(servicePrice.getPatientShare());
                if (patientShare < 1) {
                    Intent in = new Intent(context, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra(booking_successfully_booked, booking_successfully_booked_code);
                    context.startActivity(in);
                    context.finish();
                    return;
                }

                ViewGroup viewGroup = context.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.confirm_booking, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                TextView text_clinic = dialogView.findViewById(R.id.text_clinic);
                String clinicName = specialityDescription;
                text_clinic.setText(clinicName);

                TextView text_phisician = dialogView.findViewById(R.id.text_phisician);
                String fullName = physician.getProviderDescription();

                text_phisician.setText(fullName);

                TextView text_date = dialogView.findViewById(R.id.text_date);
                text_date.setText(fromDate);

                TextView text_time = dialogView.findViewById(R.id.text_time);
                text_time.setText(bookingslotTime);

                TextView text_consultent_type = dialogView.findViewById(R.id.text_consultent_type);
                text_consultent_type.setText(consultationtype);

                TextView text_patient_pay = dialogView.findViewById(R.id.patient_pay);
                text_patient_pay.setText(servicePrice.getPatientShare());

//                TextView text_discount = dialogView.findViewById(R.id.text_discount);
//                text_discount.setText(servicePrice.getPayorShare());

//                TextView text_vat = dialogView.findViewById(R.id.text_vat);
//                text_vat.setText(servicePrice.getPayorShare());


                TextView text_total_amount = dialogView.findViewById(R.id.text_total_amount);
                text_total_amount.setText(servicePrice.getOrderPrice());

                TextView service_price = dialogView.findViewById(R.id.service_price);
                service_price.setText(servicePrice.getOrderPrice());

                TextView patient_pay = dialogView.findViewById(R.id.patient_pay);
                patient_pay.setText(servicePrice.getPatientShare());


                ImageView clear_dailog = dialogView.findViewById(R.id.clear_dailog);
                clear_dailog.setOnClickListener(view -> alertDialog.dismiss());

                AppCompatButton paynow = dialogView.findViewById(R.id.paynow);
                AppCompatButton pay_later = dialogView.findViewById(R.id.pay_later);

                pay_later.setOnClickListener(view -> {

                    Intent in = new Intent(context, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra(booking_successfully_booked, booking_successfully_booked_code);
                    context.startActivity(in);
                    context.finish();
                });
                if (servicePrice.getIs_final_price()) {
                    paynow.setVisibility(View.VISIBLE);
                }


                paynow.setOnClickListener(view -> {
                    ValidateResponse user = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_USER);

                    String slotBookingId = sessionId;
                    String patientId = user.getPatientId();
                    String payerForname = user.getFirstName();
                    String payerSurname = user.getLastName();
                    String docCode = physician.getProviderCode();
                    String docName = physician.getProviderDescription();
                    String payerEmail = user.getEmail(context);

                    String appointmentDate = timeStamp;

                    String payerAddress = user.getAddress();
                    String amount = servicePrice.getPatientShare();
                    String payerTitle = user.getFirstName();
                    String mrn = user.getMrn();

                    RequestBodyForPaymentUrlGeneration requestBodyForPaymentUrlGeneration = new RequestBodyForPaymentUrlGeneration();
                    requestBodyForPaymentUrlGeneration.setServiceCode(serviceCode);
                    requestBodyForPaymentUrlGeneration.setSlotBookingId(slotBookingId);
                    requestBodyForPaymentUrlGeneration.setPatientId(patientId);
                    requestBodyForPaymentUrlGeneration.setPayerForname(payerForname);
                    requestBodyForPaymentUrlGeneration.setPayerSurname(payerSurname);
                    requestBodyForPaymentUrlGeneration.setDocCode(docCode);
                    requestBodyForPaymentUrlGeneration.setDocName(docName);
                    requestBodyForPaymentUrlGeneration.setPayerEmail(payerEmail);
                    requestBodyForPaymentUrlGeneration.setAppointmentDate(appointmentDate);
                    requestBodyForPaymentUrlGeneration.setPayerAddress(payerAddress);
                    requestBodyForPaymentUrlGeneration.setAmount(amount);
                    requestBodyForPaymentUrlGeneration.setServiceName(serviceName);
                    requestBodyForPaymentUrlGeneration.setPayerTitle(payerTitle);
                    requestBodyForPaymentUrlGeneration.setMrn(mrn);
                    myhttpMethod.generatePaymentUrl(context, requestBodyForPaymentUrlGeneration);
                });

                alertDialog.setCancelable(false);
                alertDialog.show();
























































































      }

        });


    }

    private void hideLoading() {
        progressDialog.dismiss();
    }

    private void showLoading() {
        progressDialog.show();
    }


    public void showConfirmDailog(PhysicianResponse.ProviderList physician,String apType,
                                  String specialityDescription,String fromDate,String bookingslotTime,
                                  String consultationtype,String SelectedServiceCode,String specialityCode,
                                  String sessionId,String selectedDat,String selectedTime2,String serviceName,String timeStamp) {

        ViewGroup viewGroup = context.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.confirm_appointment, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text_clinic = dialogView.findViewById(R.id.text_clinic);
        String clinicName = specialityDescription;
        text_clinic.setText(clinicName);

        TextView text_phisician = dialogView.findViewById(R.id.text_phisician);
        String fullName = physician.getProviderDescription();

        text_phisician.setText(fullName);

        TextView text_date = dialogView.findViewById(R.id.text_date);
        text_date.setText(fromDate);

        TextView text_time = dialogView.findViewById(R.id.text_time);
        text_time.setText(bookingslotTime);

        TextView text_consultent_type = dialogView.findViewById(R.id.text_consultent_type);
        text_consultent_type.setText(consultationtype);

        ImageView clear_dailog = dialogView.findViewById(R.id.clear_dailog);
        clear_dailog.setOnClickListener(view -> alertDialog.dismiss());

        AppCompatButton btn_continue = dialogView.findViewById(R.id.btn_continue);
        AppCompatButton btn_no = dialogView.findViewById(R.id.btn_no);


        btn_no.setOnClickListener(view -> alertDialog.dismiss());
        btn_continue.setOnClickListener(view -> {
            callBookingApt(physician,SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance().getValue(Constants.KEY_PATIENT_ID, ""),
                    sessionId
                    , physician.getProviderCode(),
                    SelectedServiceCode, specialityCode,
                    SharedPreferencesUtils.getInstance(context).getValue(Constants.HOSPITAL_CODE, "IMC"),
                    selectedDat,
                    selectedTime2,
                    SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0),
                    physician.getProviderDescription(),physician.getArabicProviderDescription(),apType,sessionId,
                    specialityDescription,fromDate,bookingslotTime,consultationtype,serviceName,timeStamp
            );
            alertDialog.dismiss();
        });

        alertDialog.setCancelable(false);
        alertDialog.show();


    }

}
