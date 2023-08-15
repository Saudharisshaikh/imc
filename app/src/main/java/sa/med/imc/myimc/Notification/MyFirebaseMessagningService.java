package sa.med.imc.myimc.Notification;

import static sa.med.imc.myimc.Utils.Common.simpleDailog;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.WelcomeActivity;
import sa.med.imc.myimc.globle.activity.NotificationWebActivity;
import sa.med.imc.myimc.globle.activity.WebViewNotificationActivity;
import sa.med.imc.myimc.splash.SplashActivity;


/**
 * Firebase service to generate token and get notifications
 */

public class MyFirebaseMessagningService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagningService.class.getSimpleName();
    List<ActivityManager.RunningTaskInfo> taskInfo;
    ActivityManager am;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        componentInfo.getPackageName();

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Data: " + remoteMessage.getNotification());

        //sendNotification(remoteMessage.getData().toString(), new Intent(), "IMC");
        Log.e("abcd",new Gson().toJson(remoteMessage.getNotification()));
        Log.e("abcd",new Gson().toJson(remoteMessage.getData()));
        String maindata,title;
//        Log.e("abcd", jsonObject.toString());
        try {
//            maindata = jsonObject.getString("message");
//            String title = jsonObject.getString("title");
//            String type = jsonObject.getString("type");

            maindata = remoteMessage.getNotification().getTitle();
            title = remoteMessage.getNotification().getBody();
            String type = remoteMessage.getNotification().getImageUrl().toString();

            Log.e("abcd",type);
            if (type.equalsIgnoreCase("SE")) {//&& taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("MainActivity")) {
                try {
                    Intent refresh = new Intent(Constants.Filter.REFRESH_MAIN);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(refresh);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i1 = new Intent(this, SplashActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                sendNotification(maindata, i1, title);

            } else if (type.equalsIgnoreCase("L") || type.equalsIgnoreCase("M")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, "1");
//                Intent i1 = new Intent(this, MainActivity.class);
//                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                i1.putExtra(Constants.TYPE.RECORD_LAB, "dsfdsfsd");
                sendNotification(maindata, new Intent(), title);

            } else if (type.equalsIgnoreCase("B")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, "1");
//                Intent i1 = new Intent(this, ManageBookingsActivity.class);
//                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                sendNotification(maindata, new Intent(), title);

            } else if (type.equalsIgnoreCase("S")) {
                try {
                    Intent chat = new Intent(Constants.Filter.CHAT_NOTIFICATION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(chat);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (type.equalsIgnoreCase("REJ")) {
                try {
                    Intent video_Rej = new Intent(Constants.Filter.VIDEO_REJECT);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(video_Rej);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (type.equalsIgnoreCase("BC")){
                String lang = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

                String htmlCode=remoteMessage.getData().get("htmlCode");
                if (htmlCode.trim().isEmpty()){
                    Intent intent=new Intent(this, WelcomeActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("message",maindata);
                    intent.putExtra("isPopUp",true);
                    sendNotification(maindata, intent, title);
                }else {
                    Intent intent=new Intent(this, NotificationWebActivity.class)
                            .putExtra("html",htmlCode);
                    sendNotification(maindata, intent, title);
                }

            } else {
                Intent i1=new Intent();
                if (title.contains("http")){
                    i1 = new Intent(this, WebViewNotificationActivity.class);
                    i1.putExtra("url",title);
                }



                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NOTIFICATION_UNREAD, "1");
                sendNotification(maindata, i1, title);
            }

            try {
                Intent refresh = new Intent(Constants.Filter.REFRESH_MAIN_NOTIFICATION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(refresh);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

//        SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_DEVICE_ID, s);
        Log.e("abcd", " FCM Token : " + s);
        try {
            String s2 = FirebaseInstanceId.getInstance().getToken("208609700112", FirebaseMessaging.INSTANCE_ID_SCOPE);
            SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_DEVICE_ID, s2);
            Log.e("abcd", " FCM Token2 : " + s2);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        sendNotification("I am testing small icon", new Intent(), "Hello");
    }


    private void sendNotification(String message, Intent intent, String title) {

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.imc_logo);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel mChannel = null;

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.imc_logo)
                .setLargeIcon(image)
                .setColor(getResources().getColor(android.R.color.white))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.small_imc_icon);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.small_imc_icon);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(mChannel);

        notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(), notificationBuilder.build());

    }
}
