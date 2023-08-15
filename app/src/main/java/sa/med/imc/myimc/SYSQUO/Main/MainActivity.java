package sa.med.imc.myimc.SYSQUO.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.application.AlertDialogHandler;
import sa.med.imc.myimc.SYSQUO.Chat.application.SessionManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.ChatClientManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.listeners.TaskCompletionListener;
import sa.med.imc.myimc.SYSQUO.Selection.SelectionActivity;
import sa.med.imc.myimc.SYSQUO.util.Constants;
import sa.med.imc.myimc.SYSQUO.util.Progress;
import sa.med.imc.myimc.SYSQUO.util.Utility;


public class MainActivity extends AppCompatActivity {
    TextInputLayout TextInputLayout_Identity, TextInputLayout_Doctor;
    TextInputEditText TextInputEditText_Identity, TextInputEditText_Doctor;
    ColorStateList oldColors;

    final Context context = this;
    private final String USERNAME_FORM_FIELD = "username";
    private ProgressDialog progressDialog;

    private ChatClientManager clientManager;
    private String language;
    private SharedPreferences sharedPreferences;
    RelativeLayout englishLayout, arabicLayout;
    int count = 1;
    Progress progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new Progress(this);
        (Objects.requireNonNull(progress.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        clientManager = ImcApplication.getInstance().getChatClientManager();
        initializeUI();
    }
    public void initializeUI()
    {
        // get data from shared preferences
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE,MODE_PRIVATE);
        language = "en";
        setLocale(language);
//        if(count == 1) {
//            updateLanguage(language);
//        }
//        Utility.setLocale(this,language);

        englishLayout = findViewById(R.id.englishLayout);
        arabicLayout = findViewById(R.id.arabicLayout);

        /*if (language.matches("en")){
            englishLayout.setBackgroundResource(R.drawable.blue_strok_rect);
            arabicLayout.setBackgroundResource(R.drawable.white_rect);
            language = "en";
            setLocale(language);
        }else{
            arabicLayout.setBackgroundResource(R.drawable.blue_strok_rect);
            englishLayout.setBackgroundResource(R.drawable.white_rect);
            language = "ar";
            setLocale(language);
        }*/

        TextInputLayout_Identity = findViewById(R.id.TextInputLayout_Identity);
        TextInputLayout_Doctor = findViewById(R.id.TextInputLayout_Doctor);

        TextInputEditText_Identity = findViewById(R.id.TextInputEditText_Identity);
        TextInputEditText_Doctor = findViewById(R.id.TextInputEditText_Doctor);
        oldColors =  TextInputLayout_Identity.getHintTextColor();

    }

    public void onClickLogin(View view) {
        int i = 0;
        if(TextInputEditText_Identity.getText().toString().isEmpty())
        {
            TextInputLayout_Identity.setError(getResources().getString(R.string.identityError));
            TextInputLayout_Identity.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            i = 0;
            return;
        }
        else
        {
            TextInputLayout_Identity.setError(null);
            TextInputLayout_Identity.setHintTextColor(oldColors);
            i = 1;
        }
        if(TextInputEditText_Doctor.getText().toString().isEmpty())
        {
            TextInputLayout_Doctor.setError(getResources().getString(R.string.doctorError));
            TextInputLayout_Doctor.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            i = 0;
            return;
        }
        else
        {
            TextInputLayout_Doctor.setError(null);
            TextInputLayout_Doctor.setHintTextColor(oldColors);
            i = 1;
        }

        if(i == 1)
        {
            Map<String, String> formInput = getFormInput();
            if (formInput.size() < 1) {
                displayAllFieldsRequiredMessage();
                return;
            }
            startStatusDialogWithMessage(getStringResource(R.string.login_user_progress_message));
            SessionManager.getInstance().createLoginSession(formInput.get(USERNAME_FORM_FIELD));

            SessionManager.getInstance().setIdentity(TextInputEditText_Identity.getText().toString().toLowerCase().trim());
            SessionManager.getInstance().setDoctorId(TextInputEditText_Doctor.getText().toString().toLowerCase().trim());
            SessionManager.getInstance().setRoom(TextInputEditText_Identity.getText().toString().toLowerCase().trim()+"_"+TextInputEditText_Doctor.getText().toString());
            SessionManager.getInstance().setChannel(TextInputEditText_Identity.getText().toString().toLowerCase().trim()+"_"+TextInputEditText_Doctor.getText().toString());
            initializeChatClient();
        }
    }

    private Map<String, String> getFormInput() {
        String username = TextInputEditText_Identity.getText().toString();

        Map<String, String> formInput = new HashMap<>();

        if (username.length() > 0) {
            formInput.put(USERNAME_FORM_FIELD, username);
        }
        return formInput;
    }

    private void initializeChatClient() {
        clientManager.connectClient(new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMainChatActivity();
            }

            @Override
            public void onError(String errorMessage) {
                stopStatusDialog();
                showAlertWithMessage(errorMessage);
            }
        });
    }

    private void displayAllFieldsRequiredMessage() {
        String message = getStringResource(R.string.login_all_fields_required);
        showAlertWithMessage(message);
    }

    private void startStatusDialogWithMessage(String message) {
        setFormEnabled(false);
        showActivityIndicator(message);
    }

    private void stopStatusDialog() {
        /*if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }*/
        progress.dismiss();
        setFormEnabled(true);
    }

    private void setFormEnabled(Boolean enabled) {
        TextInputEditText_Identity.setEnabled(enabled);
    }

    private void showActivityIndicator(String message) {
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);*/
        progress.show();
    }

    private void showMainChatActivity() {
        Intent launchIntent = new Intent();
        launchIntent.setClass(getApplicationContext(), SelectionActivity.class);
        startActivity(launchIntent);

        finish();
    }

    private String getStringResource(int id) {
        Resources resources = getResources();
        return resources.getString(id);
    }

    private void showAlertWithMessage(String message) {
        AlertDialogHandler.displayAlertWithMessage(message, context);
    }



    public void englishClickM(View view) {
//        if (!language.equals("en")){
            englishLayout.setBackgroundResource(R.drawable.sysquo_blue_strok_rect);
            arabicLayout.setBackgroundResource(R.drawable.sysquo_white_rect);
            language = "en";
            updateLanguage(language);
//        }
    }

    public void arabicClickM(View view) {
//        if (!language.equals("ar")){
            arabicLayout.setBackgroundResource(R.drawable.sysquo_blue_strok_rect);
            englishLayout.setBackgroundResource(R.drawable.sysquo_white_rect);
            language = "ar";
            updateLanguage(language);
//        }
    }

    private void updateLanguage(String language){
        count = 2;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_LANGUAGE,language);
        editor.commit();
        Utility.setLocale(this,language);
        Intent refresh = new Intent(this, MainActivity.class);
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