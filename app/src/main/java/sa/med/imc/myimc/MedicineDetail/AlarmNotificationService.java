package sa.med.imc.myimc.MedicineDetail;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Random;

import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.MedicineDetail.model.MedicineReminderModel;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.splash.SplashActivity;
import sa.med.imc.myimc.Utils.Common;

public class AlarmNotificationService extends IntentService {

    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;

    public AlarmNotificationService() {
        super("AlarmNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        startForeground(NOTIFICATION_ID, sendNotification("My IMC"));

        ImcDatabase db = Room.databaseBuilder(getApplicationContext(), ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        List<MedicineReminderModel> list = db.medicationReminderDataDao().loadMedReminderData();

        LocalDate current_date = LocalDate.now();

        for (int i = 0; i < list.size(); i++) {

            String start_date = list.get(i).getStart_date();
            String end_date = list.get(i).getEnd_date();
            String last_update_date = list.get(i).getLast_sent_date();

            LocalDate start_date_c = LocalDate.of(Common.getConvertToYeaReminderrInt(start_date),
                    Common.getConvertToMonthReminderInt(start_date),
                    Common.getConvertToDayReminderInt(start_date));

            LocalDate end_date_c = LocalDate.of(Common.getConvertToYeaReminderrInt(end_date),
                    Common.getConvertToMonthReminderInt(end_date),
                    Common.getConvertToDayReminderInt(end_date));

            LocalDate last_update_date_c = LocalDate.of(Common.getConvertToYeaReminderrInt(last_update_date),
                    Common.getConvertToMonthReminderInt(last_update_date),
                    Common.getConvertToDayReminderInt(last_update_date));

            Log.e("sdsd"," ---" + start_date_c.getDayOfMonth() + "/" + start_date_c.getMonth() + "/" + start_date_c.getYear());
            Log.e("reminder end date ", " ---" + end_date_c.getDayOfMonth() + "/" + end_date_c.getMonth() + "/" + end_date_c.getYear());
            Log.e("reminder current date ", " ---" + current_date.getDayOfMonth() + "/" + current_date.getMonth() + "/" + current_date.getYear());
            Log.e("reminder last updtedate", " ---" + last_update_date_c.getDayOfMonth() + "/" + last_update_date_c.getMonth() + "/" + last_update_date_c.getYear());

//            if (end_date_c.getDayOfMonth() >= current_date.getDayOfMonth()
//                    && end_date_c.getMonthValue() >= current_date.getMonthValue()
//                    && end_date_c.getYear() >= current_date.getYear()) {
            if (current_date.isBefore(end_date_c) || current_date.equals(end_date_c)) {
                Log.e("reminder ", " set on worker is before");
//                if (last_update_date_c.getDayOfMonth() <= current_date.getDayOfMonth()
//                        && last_update_date_c.getMonthValue() >= current_date.getMonthValue()
//                        && last_update_date_c.getYear() <= current_date.getYear()) {

                if (last_update_date_c.isBefore(current_date) || last_update_date_c.equals(current_date)) {
                    Log.e("reminder ", " set on worker 2");

                    if (list.get(i).getFreq() != null)
                        if (list.get(i).getFreq().contains("WK"))
                            checksOnTime(list.get(i), last_update_date_c, current_date);
                        else
                            checksOnTime(list.get(i), last_update_date_c, current_date, "");

                } else {
                    Log.e("reminder ", " set off worker 2");
                }

            } else {
                db.medicationReminderDataDao().updateStatusOnly(false, false, list.get(i).getMed_dispense_id());
                Log.e("reminder ", " set off worker status in DB");
            }
        }
    }


    private void sendNotification(String message, Intent intent) {

        Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.imc_logo);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel mChannel = null;

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getApplicationContext().getString(R.string.channel_name);// The user-visible name of the channel.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.imc_logo)
                .setLargeIcon(image)
                .setColor(getApplicationContext().getResources().getColor(android.R.color.white))
                .setContentTitle(getApplicationContext().getString(R.string.medicine_reminder))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.small_imc_icon);
            notificationBuilder.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.small_imc_icon);
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(mChannel);

        notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(), notificationBuilder.build());
    }


    // for daily medication reminders
    private void checksOnTime(MedicineReminderModel model, LocalDate last_update_date_c, LocalDate current_date, String n) {
        ImcDatabase db = Room.databaseBuilder(getApplicationContext(), ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        String freq = model.getFreq();
        String update_date = model.getLast_sent_date();

        if (freq.contains("OD") || freq.contains("TID") || freq.contains("BID") || freq.contains("QID")) {

            String current_hour = Common.getCurrentTimeWithLastUpdate();

            // Reminder condition for medication to take once daily.
            if (freq.contains("OD")) {
                String reminder_set_hour = Common.getConvertTimeTo24Hour(model.getDay_time_one());

                // Reminder condition for time one.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour, db, model, last_update_date_c, current_date, "First");
            }

            // Reminder condition for medication to take twice daily.
            if (freq.contains("BID")) {

                String reminder_set_hour1 = Common.getConvertTimeTo24Hour(model.getDay_time_one());
                String reminder_set_hour2 = Common.getConvertTimeTo24Hour(model.getDay_time_two());

                // Reminder condition for time one.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour1, db, model, last_update_date_c, current_date, "First");
                // Reminder condition for time two.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour2, db, model, last_update_date_c, current_date, "Second");
            }

            // Reminder condition for medication to take three time daily.
            if (freq.contains("TID")) {

                String reminder_set_hour1 = Common.getConvertTimeTo24Hour(model.getDay_time_one());
                String reminder_set_hour2 = Common.getConvertTimeTo24Hour(model.getDay_time_two());
                String reminder_set_hour3 = Common.getConvertTimeTo24Hour(model.getDay_time_three());

                // Reminder condition for time one.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour1, db, model, last_update_date_c, current_date, "First");
                // Reminder condition for time two.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour2, db, model, last_update_date_c, current_date, "Second");
                // Reminder condition for time three.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour3, db, model, last_update_date_c, current_date, "Third");

            }

            // Reminder condition for medication to take four time daily.
            if (freq.contains("QID")) {
                String reminder_set_hour1 = Common.getConvertTimeTo24Hour(model.getDay_time_one());
                String reminder_set_hour2 = Common.getConvertTimeTo24Hour(model.getDay_time_two());
                String reminder_set_hour3 = Common.getConvertTimeTo24Hour(model.getDay_time_three());
                String reminder_set_hour4 = Common.getConvertTimeTo24Hour(model.getDay_time_four());

                // Reminder condition for time one.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour1, db, model, last_update_date_c, current_date, "First");
                // Reminder condition for time two.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour2, db, model, last_update_date_c, current_date, "Second");
                // Reminder condition for time three.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour3, db, model, last_update_date_c, current_date, "Third");
                // Reminder condition for time four.
                sendNotificationForTimeBasis(current_hour, reminder_set_hour4, db, model, last_update_date_c, current_date, "Four");
            }
        }

        // Reminder condition for medication to take hourly basis.
        if (freq.contains("Q") && freq.contains("H")) {
            int hour = model.getHours();//freq.replace("Q", "") + freq.replace("H", "");
            if (!model.getLast_sent_time().equalsIgnoreCase("00:00")) {
                String last_sent_hour = Common.getHourAheadTime(model.getLast_sent_time(), hour);// Common.getConvertTimeToHour(
                String current_hour = Common.getCurrentTimeWithLastUpdateAM();
                Log.e("reminder", "QH > " + current_hour + "   hour > " + hour);

                if (last_sent_hour != null) {// && Integer.parseInt(last_sent_hour) == 0
                    String oneMinuteAheadTime = "";

                    if (last_sent_hour.length() > 0)
                        oneMinuteAheadTime = Common.getOneMinuteAheadTimeH(last_sent_hour);

                    Log.e("reminder", "current > " + current_hour + "   time > " + last_sent_hour + "   last_update > " + model.getLast_sent_time() + "   >> 1minutr ahead" + oneMinuteAheadTime);
                    if (current_hour != null)
                        if (last_sent_hour.equalsIgnoreCase(current_hour) || oneMinuteAheadTime.equalsIgnoreCase(current_hour)) {

                            String message = getApplicationContext().getString(R.string.reminder_noti_message, model.getPatient_name(), model.getMedication_name());
                            sendNotification(message, new Intent(getApplicationContext(), SplashActivity.class));
                            // save last date when notification sent
                            db.medicationReminderDataDao().updateLastSentDate(Common.getCurrentDateWithLastUpdate(), Common.getCurrentTimeWithLastUpdate(), model.getMed_dispense_id());
                        }
//                Calendar calendar = Calendar.getInstance();
//
//                if (Integer.parseInt(last_sent_hour) != 0)
//                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(last_sent_hour));
//
//                calendar.add(Calendar.HOUR_OF_DAY, hour);
//                String message = getApplicationContext().getString(R.string.reminder_noti_message, model.getPatient_name(), model.getMedication_name());
//                Log.e("reminder", "current_hour > " + current_hour + "   time > " + calendar.get(Calendar.HOUR_OF_DAY));
//
//                if (current_hour != null && calendar.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(current_hour) ||
//                        Integer.parseInt(last_sent_hour) == 0) {
//                    // send reminder
//                    Log.e("reminder", "current_hour inside> " + current_hour + "   time > " + calendar.get(Calendar.HOUR_OF_DAY));
//
//                    sendNotification(message, new Intent(getApplicationContext(), WelcomeActivity.class));
//                    // save last date when notification sent
//                    db.medicationReminderDataDao().updateLastSentDate(Common.getCurrentDateWithLastUpdate(), Common.getCurrentTimeWithLastUpdate(), model.getMed_dispense_id());
//                }
                }
            } else {
                Log.e("reminder", "hourly  last_update > " + model.getLast_sent_time());
                db.medicationReminderDataDao().updateLastSentDate(Common.getCurrentDateWithLastUpdate(),
                        Common.getCurrentTimeWithLastUpdate(),
                        model.getMed_dispense_id());
            }
        }

        // medication only for once check if last date updated means notification already sent
        if (freq.contains("ONCE")) {

            String current_hour = Common.getCurrentTimeWithLastUpdate();

            // Reminder condition for medication to take once only.
            String reminder_set_hour = Common.getConvertTimeTo24Hour(model.getDay_time_one());

            // Reminder condition for time one.
            sendNotificationForTimeBasis(current_hour, reminder_set_hour, db, model, last_update_date_c, current_date, "Once");


//            if (update_date != null && update_date.equalsIgnoreCase(Common.getCurrentDateWithLastUpdate())) {
//                String message = getApplicationContext().getString(R.string.reminder_noti_message, model.getPatient_name(), model.getMedication_name());
//                sendNotification(message, new Intent(getApplicationContext(), WelcomeActivity.class));
//            }
        }
    }

    // for weekly medication reminders
    private void checksOnTime(MedicineReminderModel model, LocalDate last_update_date_c, LocalDate current_date) {
        String message = getApplicationContext().getString(R.string.reminder_noti_message, model.getPatient_name(), model.getMedication_name());
        ImcDatabase db = Room.databaseBuilder(getApplicationContext(), ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();

//        if (last_update_date_c.getDayOfMonth() < current_date.getDayOfMonth()
//                && last_update_date_c.getMonthValue() <= current_date.getMonthValue()
//                && last_update_date_c.getYear() <= current_date.getYear()) {

        if (last_update_date_c.isBefore(current_date)) {

            String days = model.getWeek_days();
            String cur_day = Common.getCurrentWeekDay().toLowerCase();
            Log.e("reminder ", " WK  " + days + "    > " + cur_day);

            if (days.contains(cur_day)) {
                // send reminder
                sendNotification(message, new Intent(getApplicationContext(), SplashActivity.class));
                // save last date when notification sent
                db.medicationReminderDataDao().updateLastSentDate(Common.getCurrentDateWithLastUpdate(), Common.getCurrentTimeWithLastUpdate(), model.getMed_dispense_id());
            }
        }
    }

    // send notification on basis of times
    private void sendNotificationForTimeBasis(String current_hour, String time_set, ImcDatabase db, MedicineReminderModel model, LocalDate last_update_date_c, LocalDate current_date, String time) {
        String oneMinuteAheadTime = "";

        if (time_set.length() > 0)
            oneMinuteAheadTime = Common.getOneMinuteAheadTime(time_set);

        Log.e("reminder", "current > " + current_hour + "   time > " + time_set + "   last_update > " + model.getLast_sent_time() + "   >> 1minutr ahead" + oneMinuteAheadTime);
        // if (last_update_date_c.isBefore(current_date)) {
        // current hour is equal to set time hour
        if (current_hour != null && time_set != null)
            if (time_set.equalsIgnoreCase(current_hour) || oneMinuteAheadTime.equalsIgnoreCase(current_hour)) {

//                if (Integer.parseInt(Common.getConvertTimeToHour(time_set)) == Integer.parseInt(Common.getConvertTimeToHour(current_hour)) &&
//                        Integer.parseInt(Common.getConvertTimeToMintue(time_set)) == Integer.parseInt(Common.getConvertTimeToMintue(current_hour))) {

                String message = getApplicationContext().getString(R.string.reminder_noti_message, model.getPatient_name(), model.getMedication_name());

                // send reminder
                sendNotification(message, new Intent(getApplicationContext(), SplashActivity.class));
                // save last date when notification sent
                db.medicationReminderDataDao().updateLastSentDate(Common.getCurrentDateWithLastUpdate(), Common.getCurrentTimeWithLastUpdate(), model.getMed_dispense_id());
            }
//        } else if (model.getLast_sent_time().equalsIgnoreCase("00:00") || Integer.parseInt(Common.getConvertTimeToHour(model.getLast_sent_time())) < Integer.parseInt(Common.getConvertTimeToHour(current_hour))) {
//
//            // current hour is equal to set time hour
//            if (current_hour != null && time_set != null)
//                if (Integer.parseInt(Common.getConvertTimeToHour(time_set)) == Integer.parseInt(Common.getConvertTimeToHour(current_hour)) &&
//                        Integer.parseInt(Common.getConvertTimeToMintue(time_set)) == Integer.parseInt(Common.getConvertTimeToMintue(current_hour))) {
//                    String message = getApplicationContext().getString(R.string.reminder_noti_message, model.getPatient_name(), model.getMedication_name());
//
//                    // send reminder
//                    sendNotification(message, new Intent(getApplicationContext(), WelcomeActivity.class));
//                    // save last date when notification sent
//                    db.medicationReminderDataDao().updateLastSentDate(Common.getCurrentDateWithLastUpdate(), Common.getCurrentTimeWithLastUpdate(), model.getMed_dispense_id());
//                }
//        }
    }


    private Notification sendNotification(String message) {

        Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.imc_logo);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel mChannel = null;

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getApplicationContext().getString(R.string.channel_name);// The user-visible name of the channel.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.imc_logo)
                .setLargeIcon(image)
                .setColor(getApplicationContext().getResources().getColor(android.R.color.white))
                .setContentTitle(getApplicationContext().getString(R.string.medicine_reminder))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.small_imc_icon);
            notificationBuilder.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.small_imc_icon);
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(mChannel);

//        notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(), notificationBuilder.build());

        return notificationBuilder.build();
    }

}

