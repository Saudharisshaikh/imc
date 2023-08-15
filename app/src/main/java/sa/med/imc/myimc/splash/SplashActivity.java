package sa.med.imc.myimc.splash;

import static sa.med.imc.myimc.Network.Constants.ENGLISH;
import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.LocaleHelper;
import sa.med.imc.myimc.WelcomeActivity;
import sa.med.imc.myimc.Adapter.SplashAdapter;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

public class SplashActivity extends AppCompatActivity {

//    HospitalAdapter hospitalAdapter;
//    List<HospitalModel.HospitalList> hospitalListList=new ArrayList<>();
//    RecyclerView hospital_recycler;
  private AppUpdateManager mAppUpdateManager;
  ArrayList<HashMap<String, String>> locationsList;
  private ListView lv;

    private static final int RC_APP_UPDATE = 11;


    @Override
    protected void onStart() {
        super.onStart();



        if (!BuildConfig.DEBUG) {

            mAppUpdateManager = AppUpdateManagerFactory.create(this);

            mAppUpdateManager.registerListener(installStateUpdatedListener);

            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/)) {

                    Log.e("abcd", "update available");
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/, SplashActivity.this, RC_APP_UPDATE);

                    } catch (IntentSender.SendIntentException e) {
                        Log.e("abcd", "update error" + e.toString());

                    }

                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                    popupSnackbarForCompleteUpdate();
                } else {
                    Log.e("abcd", "checkForAppUpdateAvailability: something else");
                }
            });
        }
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (mAppUpdateManager != null){
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i("abcd", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadHospital();
        if (!isTaskRoot()) {
            finish();
            return;
        }
        SharedPreferencesUtils.getInstance(SplashActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
        String lang = SharedPreferencesUtils.getInstance(SplashActivity.this).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);

        SharedPreferencesUtils.getInstance(SplashActivity.this).setValue(Constants.KEY_LANGUAGE, lang);
        LocaleHelper.setLocale(this, lang);

        /*
        findViewById(R.id.imcHospital).setOnClickListener(view -> {
            moveToNextScreen(1,"IMC");
        });

        findViewById(R.id.firstClinicHospital).setOnClickListener(view -> {
            moveToNextScreen(4,"FC");
        });*/

    }

    private void loadHospital() {

    // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new UrlWithURLDecoder().getHospital_list_v2();
        //String url = "https://vdh.noj.mybluehost.me/list.php?baddd";
        Log.e("url",url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                            Log.e("response",response);
                            JSONArray JSONObject;
                            locationsList = new ArrayList<>();

                            JSONObject  = new JSONArray(response);
                            HashMap<String, String> card = new HashMap<>();
                            for (int i = 0; i < JSONObject.length(); i++) {
                                org.json.JSONObject c = JSONObject.getJSONObject(i);
                                String hospitalId = c.getString("hospitalId");
                                String hospitalCode = c.getString("hospitalCode");
                                String hospitalName = c.getString("hospitalName");
                                String hospitalName_ar = c.getString("hospitalName_ar");
                                String hospitalLogo = c.getString("hospitalLogo");
                                String hospitalLargeImage = c.getString("hospitalLargeImage");
                                String hospitalLogo_white = c.getString("hospitalLogo_white");
                                String specialMessage = c.getString("specialMessage");
                                String specialMessage_ar = c.getString("specialMessage_ar");

                                card = new HashMap<>();
                                card.put("hospitalId", hospitalId);
                                card.put("hospitalCode", hospitalCode);
                                card.put("hospitalName", hospitalName);
                                card.put("hospitalName_ar", hospitalName_ar);
                                card.put("hospitalLogo", hospitalLogo);
                                card.put("hospitalLargeImage", hospitalLargeImage);
                                card.put("hospitalLogo_white", hospitalLogo_white);
                                card.put("specialMessage", specialMessage);
                                card.put("specialMessage_ar", specialMessage_ar);

                                locationsList.add(card);
                            }

                            SharedPreferences sp = getSharedPreferences("location", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("hospitals_locations", response);
                            editor.commit();

                            listLocations();

                        } catch (final JSONException e) {
                            // Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("abcd",error.getMessage());
                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(stringRequest);

    }





    public void updateSelection(int selectedHospital,String hospitalCode){

        SharedPreferencesUtils.getInstance(this).setValue(Constants.SELECTED_HOSPITAL, selectedHospital);
        SharedPreferencesUtils.getInstance(this).setValue(HOSPITAL_CODE, hospitalCode);

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void listLocations(){

        lv = (ListView) findViewById(R.id.list);

        SplashAdapter adapter = new SplashAdapter(SplashActivity.this, locationsList,
                R.layout.list_item_splash, new String[] {}, new int[] {});
        //perform listView item click event
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setAdapter(adapter);

    }


    private void moveToNextScreen(int selectedHospital,String hospital_code) {

        SharedPreferencesUtils.getInstance(this).setValue(Constants.SELECTED_HOSPITAL, selectedHospital);
        SharedPreferencesUtils.getInstance(this).setValue(HOSPITAL_CODE, hospital_code);
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.content),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.green));
        snackbar.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("abcd", "onActivityResult: app download failed");
            }
        }
    }

}