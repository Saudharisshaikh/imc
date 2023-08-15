package sa.med.imc.myimc.Login.Validate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import sa.med.imc.myimc.BuildConfig;

public class IncomingSms extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        if (message != null) {
                            message = message.split(":")[1];
                            message = message.trim();
                            message = message.substring(0, 4);
                            if (BuildConfig.DEBUG)
                                Log.e("message", "=====" + message);
                            Intent myIntent = new Intent("otp");
                            myIntent.putExtra("message", message);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        break;
                }
            }
        }

        // Retrieves a map of extended data from the intent.
//        final Bundle bundle = intent.getExtras();
//
//        try {
//
//            if (bundle != null) {
//
//                final Object[] pdusObj = (Object[]) bundle.get("pdus");
//
//                if (pdusObj != null) {
//                    for (Object o : pdusObj) {
//
//                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) o);
//
//                        String senderNum;
//                        senderNum = currentMessage.getDisplayOriginatingAddress();
//                        if (currentMessage.getDisplayMessageBody().contains(":")) {
//                            String message = currentMessage.getDisplayMessageBody().trim().split(":")[1];
//                            Log.e("message", "===== " + currentMessage.getDisplayMessageBody());
//                            //message = message.substring(0, message.length() - 1);
//                            Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
//
//                            Intent myIntent = new Intent("otp");
//                            myIntent.putExtra("message", message.trim());
//                            LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
//                        }
//                        // Show Alert
//
//                    } // end for loop
//                }
//            } // bundle is null
//
//        } catch (Exception e) {
//            Log.e("SmsReceiver", "Exception smsReceiver" + e);
//        }
    }
}