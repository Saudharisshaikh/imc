package sa.med.imc.myimc.SignUp.activity;

import static sa.med.imc.myimc.Utils.Common.TIME_OUT_DURATION;
import static sa.med.imc.myimc.Utils.Common.messageDailogForCompleteActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.FindDoctorActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.SignUp.model.SelfRegistrationOtpResponse;
import sa.med.imc.myimc.custom_dailog.CustomDailogBuilder;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.SessionExpired;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;
import sa.med.imc.myimc.globle.interfaces.SelfRegistrationListner;
import sa.med.imc.myimc.globle.interfaces.SelfRegistrationOtp;

public class SelfRegistrationActivity extends AppCompatActivity {

    EditText first_name, mid_name, last_name, phone_number, id_number;
    Spinner id_type, gender, insurance_spinner;
    LinearLayout sign_up_btn, id_number_layout;
    TextView id_exp_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_registration);
        initView();
        sign_up_btn.setOnClickListener(view -> {
            if (isValid()) {
                String details = "First Name: " + first_name.getText().toString().trim() +
                        " - Middle Name: " + mid_name.getText().toString().trim() +
                        " - Last Name: " + last_name.getText().toString().trim() +
                        " – Gender: " + gender.getSelectedItem().toString() +
                        " – Id Type: " + id_type.getSelectedItem().toString() +
                        " - Mobile Number: " + phone_number.getText().toString().trim() +
                        " - Home Phone Number: " + phone_number.getText().toString().trim() +
                        " - Id Number: " + id_number.getText().toString().trim() +
                        " - Id Expiry Date: " + id_exp_date.getText().toString().trim()+
                        " - Insurance: " + insurance_spinner.getSelectedItem().toString();


                new MyhttpMethod(SelfRegistrationActivity.this).selfRegistrationOtp(phone_number.getText().toString().trim(), new SelfRegistrationOtp() {
                    @Override
                    public void onSucces(SelfRegistrationOtpResponse response) {
                        if (response.status) {
                            Intent intent = new Intent(SelfRegistrationActivity.this, SelfRegistrationOtpVerificationActivity.class);
                            intent.putExtra("mobileNumber", phone_number.getText().toString().trim());
                            intent.putExtra("details", details);
                            intent.putExtra("otp", new Gson().toJson(response));
                            startActivity(intent);
                            finish();


                        } else {
                            messageDailogForCompleteActivity(SelfRegistrationActivity.this, response.message);
                        }

                    }
                });

            }
        });
    }

    private void initView() {
        first_name = findViewById(R.id.first_name);
        mid_name = findViewById(R.id.mid_name);
        last_name = findViewById(R.id.last_name);
        phone_number = findViewById(R.id.phone_numbers);
        id_number = findViewById(R.id.id_number);
        id_exp_date = findViewById(R.id.id_exp_date);
        id_type = findViewById(R.id.id_type);
        gender = findViewById(R.id.gender);
        insurance_spinner = findViewById(R.id.insurance_spinner);
        sign_up_btn = findViewById(R.id.lay_btn_register);
        id_number_layout = findViewById(R.id.id_number_layout);


        loadInsuranceList();

        findViewById(R.id.iv_back).setOnClickListener(view -> finish());



        id_exp_date.setOnClickListener(view -> {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                    DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date date = null;
                    try {
                        int newMonth = months;
//                        if (months == 12) {
//                            newMonth = 1;
//                        } else {
//                            newMonth = newMonth + 1;
//                        }
                        Log.e("abcd",years+"-"+newMonth + "-" +days);
                        id_exp_date.setText(years+"-"+newMonth + "-" +days);

                        date = originalFormat.parse(days + "-" + newMonth + "-" + years);
                    } catch (ParseException e) {
                        Log.e("abcd",e.toString());
                    }
                    String formattedDate = originalFormat.format(date);
//                    id_exp_date.setText(formattedDate);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        id_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    id_number_layout.setVisibility(View.VISIBLE);
                } else {
                    id_number_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadInsuranceList() {
        CustomDailogBuilder progressDialog = new CustomDailogBuilder(SelfRegistrationActivity.this);

        progressDialog.show();

        List<String> stringList = new ArrayList<>();
        stringList.add(getString(R.string.select));

        String Url = new UrlWithURLDecoder().getInsurance_list();

        Log.e("abcd", Url);

        RequestQueue requestQueue = Volley.newRequestQueue(SelfRegistrationActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                InsuranceListResponse insuranceListResponse = new Gson().fromJson(response, new TypeToken<InsuranceListResponse>() {
                }.getType());
                List<InsuranceList> insurance_list = insuranceListResponse.insurance_list;

                for (InsuranceList insuranceList : insurance_list) {
                    stringList.add(insuranceList.insurance_description);
                }
                ArrayAdapter<String> a =new ArrayAdapter<String>(SelfRegistrationActivity.this,R.layout.spinner_item, stringList);

                insurance_spinner.setAdapter(a);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    public class InsuranceListResponse {
        public String status;
        public List<InsuranceList> insurance_list;
    }


    public class InsuranceList {
        public String insurance_code;
        public String insurance_description;
        public String date_from;
    }

    private boolean isValid() {
        if (first_name.getText().toString().trim().isEmpty()) {
            first_name.setError("Please provide First Name");
            first_name.requestFocus();
            return false;
        } else {
            if (mid_name.getText().toString().trim().isEmpty()) {
                mid_name.setError("Please provide Middle Name");
                mid_name.requestFocus();
                return false;
            } else {
                if (last_name.getText().toString().trim().isEmpty()) {
                    last_name.setError("Please provide Last Name");
                    last_name.requestFocus();
                    return false;
                } else {
                    char firstChar = phone_number.getText().toString().charAt(0);
                    String firstString = String.valueOf(firstChar);

                    if (phone_number.getText().toString().trim().length() != 9 | !firstString.equalsIgnoreCase("5")) {
                        phone_number.setError("mobile number should be of 9 digits and should start with 5");
                        phone_number.requestFocus();
                        return false;
                    } else {
                        if (id_type.getSelectedItemPosition() == 0) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                    "Please select id type", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            id_type.requestFocus();

                            return false;
                        } else {
                            if (id_exp_date.getText().toString().trim().isEmpty()) {
                                id_exp_date.setError("Please provide Id Expiry Date");
                                id_exp_date.requestFocus();
                                return false;
                            } else {
                                if (id_number.getText().toString().trim().isEmpty()) {
                                    id_number.setError("Please provide Id Number");
                                    id_number.requestFocus();
                                    return false;
                                } else {
                                    if (gender.getSelectedItemPosition() == 0) {
                                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                                "Please select id type", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        gender.requestFocus();
                                        return false;
                                    } else {
                                        if (insurance_spinner.getSelectedItemPosition() == 0) {
                                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                                    "Please select insurance", Snackbar.LENGTH_SHORT);
                                            snackbar.show();
                                            insurance_spinner.requestFocus();
                                            return false;
                                        } else {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}