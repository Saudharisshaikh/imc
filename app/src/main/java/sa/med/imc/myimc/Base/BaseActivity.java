package sa.med.imc.myimc.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import sa.med.imc.myimc.Utils.LocaleHelper;

import static sa.med.imc.myimc.Utils.LocaleHelper.onAttach;


/**
 * Base for all activities.
 * Required methods to use in every activity are defined here.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        super.onCreate(savedInstanceState, persistentState);
    }

    protected void makeToast(String msg) {

        if (msg != null && getApplicationContext() != null)
            if (msg.toLowerCase().contains("Session expired") || msg.toLowerCase().contains("لقد تم إنهاء الجلسة أو تم تسجيل الدخول من جهاز مختلف.")) {
                Common.sessionExpired(this);
//                SharedPreferencesUtils.getInstance(this).clearAll(this);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                View view = toast.getView();

                if(view != null) {
                    TextView view1 = (TextView) view.findViewById(android.R.id.message);
                    view1.setPadding(5, 5, 5, 5);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(16);
                    view.setBackgroundResource(R.drawable.toast_bg);
                    // Setting the Text Appearance
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        view1.setTextAppearance(R.style.toastTextStyle);
                    }
                }
                toast.show();
            }
    }

    protected void showSnackBar(String msg) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, msg, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setDuration(Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    protected void showSnackBarMsg(String msg) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void hideStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, SharedPreferencesUtils.getInstance(base).getValue(Constants.KEY_LANGUAGE, "")));
    }


    protected <T extends AppCompatActivity> void startActivity(final Class<T> destination) {
        startActivity(new Intent(getActivityContext(), destination));
    }

    protected void fillLayoutStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }


    protected void showStatusBar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected boolean isVersionMarsh() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    protected void hideKeyboard() {
        Common.hideSoftKeyboard((Activity) getActivityContext());

    }

    protected boolean isNetAvail() {
        return Common.isNetworkAvail(getApplicationContext());
    }


    protected boolean isPermissionGranted(@NonNull String whichPermission) {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), whichPermission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public void logger(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }


    protected abstract Context getActivityContext();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPrefHelper.init(getApplicationContext());

    }

    private String getUser() {
//        return SharedPrefHelper.read(SharedPrefHelper.KEY_USER_ID, "");
        return "";
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
