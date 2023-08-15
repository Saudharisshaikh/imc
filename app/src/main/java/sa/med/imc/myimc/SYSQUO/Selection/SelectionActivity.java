package sa.med.imc.myimc.SYSQUO.Selection;

import static sa.med.imc.myimc.Network.Constants.HOSPITAL_CODE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Locale;

import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.chat.ChatClientManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.MainChatActivity_New;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.EmergencyCallActivity;
import sa.med.imc.myimc.SYSQUO.Selection.Model.CreateRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Selection.ViewModel.CreateRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.VideoActivity;
import sa.med.imc.myimc.SYSQUO.util.Constants;
import sa.med.imc.myimc.SYSQUO.util.Utility;


public class SelectionActivity extends AppCompatActivity {

    Context context;
    private String language;
    private SharedPreferences sharedPreferences;
    RelativeLayout englishLayout, arabicLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysquo_activity_selection);

        // get data from shared preferences
//        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE,MODE_PRIVATE);
//        language = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");

        englishLayout = findViewById(R.id.englishLayout);
        arabicLayout = findViewById(R.id.arabicLayout);

//        if (language.matches("en")){
//            englishLayout.setBackgroundResource(R.drawable.sysquo_blue_strok_rect);
//            arabicLayout.setBackgroundResource(R.drawable.sysquo_white_rect);
//            language = "en";
//            setLocale(language);
//        }else{
//            arabicLayout.setBackgroundResource(R.drawable.sysquo_blue_strok_rect);
//            englishLayout.setBackgroundResource(R.drawable.sysquo_white_rect);
//            language = "ar";
//            setLocale(language);
//        }

        MainChatActivity_New mainChatActivity_new = new MainChatActivity_New();
        clickToStartVideoCall();
//        mainChatActivity_new.checkTwilioClient();
    }

    public void clickToStartVideoCall() {
        try {
            String lang;
            if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_LOGIN_LANGUAGE, sa.med.imc.myimc.Network.Constants.ENGLISH);
            }
            else {
                lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(sa.med.imc.myimc.Network.Constants.KEY_LANGUAGE, sa.med.imc.myimc.Network.Constants.ENGLISH);
            }
            CreateRoomViewModel viewModel = ViewModelProviders.of(SelectionActivity.this).get(CreateRoomViewModel.class);
            viewModel.init();
            CreateRoomRequestModel createRoomRequestModel = new CreateRoomRequestModel();
            createRoomRequestModel.setPageNumber(0);
            createRoomRequestModel.setPageSize(0);
            String mrnNumber = SharedPreferencesUtils.getInstance(this).getValue(sa.med.imc.myimc.Network.Constants.KEY_MRN, null);
            String physician = SharedPreferencesUtils.getInstance(this).getValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_PHYSICIAN, null);
            createRoomRequestModel.setRoomName(mrnNumber + "_" + physician);
            createRoomRequestModel.setUserEmail("xyz@gmail.com");
            String token = SharedPreferencesUtils.getInstance(this).getValue(sa.med.imc.myimc.Network.Constants.KEY_ACCESS_TOKEN, null);
            viewModel.CreateRoom(token, lang,SharedPreferencesUtils.getInstance(SelectionActivity.this).getValue(HOSPITAL_CODE, "IMC"), createRoomRequestModel);
            viewModel.getVolumesResponseLiveData().observe(SelectionActivity.this, response -> {

                if (response != null) {
                    if (response.getStatus()) {
                        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Dexter.withContext(SelectionActivity.this).withPermission(Manifest.permission.RECORD_AUDIO).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        SharedPreferencesUtils.getInstance(SelectionActivity.this).setValue(sa.med.imc.myimc.Network.Constants.KEY_VIDEO_ACCESS_TOKEN, response.getData().getAccessToken());
                                        Intent in = new Intent(SelectionActivity.this, VideoActivity.class);
                                        startActivity(in);
//                                    finish();
//                                    startActivity(new Intent(SelectionActivity.this, VideoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                    }
                } else {
                    Toast.makeText(this, "Some thing went wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clickToStartChat(View view) {
        ChatClientManager clientManager = ImcApplication.getInstance().getChatClientManager();
        Intent in = new Intent(this, MainChatActivity_New.class);
        startActivity(in);
        finish();
    }

    public void clickToStartEmergencyCall(View view) {
        Intent in = new Intent(this, EmergencyCallActivity.class);
        startActivity(in);
        finish();
    }

    public void englishClick(View view) {
        if (!language.equals("en")){
            englishLayout.setBackgroundResource(R.drawable.sysquo_blue_strok_rect);
            arabicLayout.setBackgroundResource(R.drawable.sysquo_white_rect);
            language = "en";
            updateLanguage(language);
        }
    }

    public void arabicClick(View view) {
        if (!language.equals("ar")){
            arabicLayout.setBackgroundResource(R.drawable.sysquo_blue_strok_rect);
            englishLayout.setBackgroundResource(R.drawable.sysquo_white_rect);
            language = "ar";
            updateLanguage(language);
        }
    }

    private void updateLanguage(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_LANGUAGE,language);
        editor.commit();
        Utility.setLocale(this,language);
        Intent refresh = new Intent(this, SelectionActivity.class);
        finish();
        startActivity(refresh);
    }

    public  void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}