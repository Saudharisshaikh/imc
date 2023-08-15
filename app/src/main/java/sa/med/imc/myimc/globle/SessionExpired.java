package sa.med.imc.myimc.globle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.WelcomeActivity;

public class SessionExpired {
    Activity activity;
    VolleyError error;
    Throwable retrofitError;

    public SessionExpired(Activity activity, VolleyError error) {
        this.activity = activity;
        this.error = error;
        checkSessionExpiration();
    }

    public SessionExpired(Activity activity, Throwable t) {
        this.activity = activity;
        this.retrofitError = t;
    }


    private void checkSessionExpiration() {
        if (error instanceof AuthFailureError){
            Log.e("bcd","Token Expire");
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.simple_alert, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);


            TextView contentTextView = dialogView.findViewById(R.id.content);
            TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
            contentTextView.setText(activity.getString(R.string.session_exp));
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            buttonOk.setOnClickListener(v -> {
                alertDialog.dismiss();
                SharedPreferencesUtils.getInstance(activity).clearAll(activity);

            });

            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }



    private void checkSessionExpirationRetrofit() {
        if (retrofitError instanceof AuthFailureError){
            Log.e("bcd","Token Expire");
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.simple_alert, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);


            TextView contentTextView = dialogView.findViewById(R.id.content);
            TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
            contentTextView.setText(activity.getString(R.string.session_exp));
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            buttonOk.setOnClickListener(v -> {
                alertDialog.dismiss();
                SharedPreferencesUtils.getInstance(activity).clearAll(activity);

            });

            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }


}
