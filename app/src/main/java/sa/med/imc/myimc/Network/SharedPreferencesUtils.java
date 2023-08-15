package sa.med.imc.myimc.Network;

import android.app.Activity;
import android.app.NotificationManager;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.splash.SplashActivity;
import sa.med.imc.myimc.Utils.LocaleHelper;

import static androidx.room.Room.databaseBuilder;


/**
 * Shared data to use within application like user id required in whole app then
 * It will be saved in Shared preferences.
 */

public class SharedPreferencesUtils {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static SharedPreferencesUtils utils;

    @Deprecated
    public static SharedPreferencesUtils getInstance(Context context) {
        if (utils == null) {
            utils = new SharedPreferencesUtils();
            preferences = ImcApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return utils;
    }

    public static SharedPreferencesUtils getInstance() {
        if (utils == null) {
            utils = new SharedPreferencesUtils();
            preferences = ImcApplication.getInstance().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return utils;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setValue(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public void setPatientId(int isInserted) {

        editor.putInt(Constants.PATIENT_ID, isInserted);
        editor.apply();
    }

    public void clearSomeValues(Activity activity) {
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_ACCESS_TOKEN, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_MRN, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_USER_ID, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_GUARDIAN, "no");

        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.ACCESS_TOKEN, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.IQAMA_ID, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.FIRST_NAME, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.FIRST_NAME_AR, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.FATHER_NAME, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.FATHER_NAME_AR, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.FAMILY_NAME, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.FAMILY_NAME_AR, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.GENDER, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.PHONE, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.DOB, "");
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.GUEST.REASON_VISIT, "");
    }

    public void clearAppointmentDate(){
        editor.remove("toDate").commit();
        editor.remove("fromDate").commit();
    }


    public void setValue(String key, boolean value) {
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setValue(String key, int value) {
        editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getValue(String key, String value) {
        return preferences.getString(key, "");
    }

    public boolean getValue(String key, boolean value) {
        return preferences.getBoolean(key, value);
    }

    public int getValue(String key, int value) {
        return preferences.getInt(key, 0);
    }

    // Clearing local stored data............
    public void clearAll(Activity activity) {
//        String lang = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
//        String dev_id = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_DEVICE_ID, "");
//
//        String mrn = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_MRN, "");
//
//        boolean re = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_REMEMBER, false);
//        String rem_vel = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_REMEMBER_MRN, "");
//        String rem_ty = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_REMEMBER_TYPE, "");
//        String cach = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_CACHE_TIME, "");
//        int patId = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_PATIENT_ID, 0);
//        db.clearAllTables();
//        clearDB(activity);

        String lang=SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        editor = preferences.edit();
        editor.clear().commit();

        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_LOGIN_LANGUAGE, lang);
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_LANGUAGE, lang);

//        editor.apply();
//
//        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_LANGUAGE, lang);
//        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_LOGIN_LANGUAGE, lang);
//        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_DEVICE_ID, dev_id);
//        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_CACHE_TIME, cach);
//        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_MRN, mrn);
//        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_PATIENT_ID, patId);
//
//        if (re) {
//            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_REMEMBER, true);
//            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_REMEMBER_MRN, rem_vel);
//            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_REMEMBER_TYPE, rem_ty);
//        }
//
//        LocaleHelper.setLocale(activity, lang);
//
//        NotificationManager notifManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//        notifManager.cancelAll();
//
//        SharedPreferencesUtils.getInstance(activity).clearSomeValues(activity);

        Intent i1 = new Intent(activity, SplashActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i1);
        activity.finish();
    }

    // Clearing local stored data............
    public void clearAllSignUp(Activity activity) {
        String lang = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        String dev_id = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_DEVICE_ID, "");
        boolean re = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_REMEMBER, false);
        String rem_vel = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_REMEMBER_MRN, "");
        String rem_ty = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_REMEMBER_TYPE, "");
        String cach = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_CACHE_TIME, "");
        int patId = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_PATIENT_ID, 0);
        int patIdForMedicus = SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_PATIENT_ID_FOR_MEDICUS, 0);


//        db.clearAllTables();
        clearDB(activity);

        editor = preferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_LANGUAGE, lang);
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_LOGIN_LANGUAGE, lang);
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_DEVICE_ID, dev_id);
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_CACHE_TIME, cach);
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_PATIENT_ID, patId);
        SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_PATIENT_ID_FOR_MEDICUS, patIdForMedicus);

        if (re) {
            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_REMEMBER, true);
            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_REMEMBER_MRN, rem_vel);
            SharedPreferencesUtils.getInstance(activity).setValue(Constants.KEY_REMEMBER_TYPE, rem_ty);
        }

        LocaleHelper.setLocale(activity, lang);

        NotificationManager notifManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();

        SharedPreferencesUtils.getInstance(activity).clearSomeValues(activity);


        Intent i1 = new Intent(activity, SplashActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i1);
        activity.finish();
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void clearDB(Activity activity) {
        ImcDatabase db;
        if (BuildConfig.DEBUG) {
            db = databaseBuilder(activity, ImcDatabase.class, "imc_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_2_3_Debug)
                    .addMigrations(MIGRATION_3_4)
                    .build();
        } else {
            db = databaseBuilder(activity, ImcDatabase.class, "imc_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
//        db.clearAllTables();
    }

    private static Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.e("migration  ", "Done============");
            database.execSQL("CREATE TABLE IF NOT EXISTS 'MedicineReminderModel' ('med_dispense_id' TEXT PRIMARY KEY NOT NULL,'patient_name' TEXT," +
                    "'medication_name' TEXT,'start_date' TEXT,'end_date' TEXT,'day_time_one' TEXT," +
                    "'day_time_two' TEXT,'day_time_three' TEXT,'is_on_going' INTEGER NOT NULL DEFAULT 0,'is_reminder' INTEGER NOT NULL DEFAULT 0," +
                    "'hours' INTEGER NOT NULL DEFAULT 0,'freq' TEXT,'schedule_time' TEXT,'last_sent_date' TEXT,'last_sent_time' TEXT," +
                    "'week_days' TEXT,'day_time_four' TEXT,'freq_desp' TEXT,'total_count_of_notifications' INTEGER NOT NULL DEFAULT 0)");

            // need to move in 4 version
            database.execSQL("ALTER TABLE 'Booking' ADD COLUMN 'hrId' TEXT");
            database.execSQL("ALTER TABLE 'Booking' ADD COLUMN 'teleHealthLink' TEXT");
            database.execSQL("ALTER TABLE 'Booking' ADD COLUMN 'teleBooking' TEXT");

        }
    };

    private static Migration MIGRATION_2_3_Debug = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.e("migration  ", "Done============");
            database.execSQL("CREATE TABLE IF NOT EXISTS 'MedicineReminderModel' ('med_dispense_id' TEXT PRIMARY KEY NOT NULL,'patient_name' TEXT," +
                    "'medication_name' TEXT,'start_date' TEXT,'end_date' TEXT,'day_time_one' TEXT," +
                    "'day_time_two' TEXT,'day_time_three' TEXT,'is_on_going' INTEGER NOT NULL DEFAULT 0,'is_reminder' INTEGER NOT NULL DEFAULT 0," +
                    "'hours' INTEGER NOT NULL DEFAULT 0,'freq' TEXT,'schedule_time' TEXT,'last_sent_date' TEXT,'last_sent_time' TEXT," +
                    "'week_days' TEXT,'day_time_four' TEXT,'freq_desp' TEXT,'total_count_of_notifications' INTEGER NOT NULL DEFAULT 0)");

        }
    };
    //
    private static Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.e("migration  ", "Done============");
            database.execSQL("ALTER TABLE 'Booking' ADD COLUMN 'hrId' TEXT");
            database.execSQL("ALTER TABLE 'Booking' ADD COLUMN 'teleHealthLink' TEXT");
            database.execSQL("ALTER TABLE 'Booking' ADD COLUMN 'teleBooking' TEXT");
        }
    };

    public void setValue(String key, ValidateResponse value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);

        editor = preferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public ValidateResponse getValue(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, "");
        return gson.fromJson(json, ValidateResponse.class);
    }

}
