package sa.med.imc.myimc.Network;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import ai.medicus.medicuscore.AppointmentManager;
import ai.medicus.medicuscore.Coaching;
import ai.medicus.medicuscore.EngineRunType;
import ai.medicus.medicuscore.HeartRateSdk;
import ai.medicus.medicuscore.MedicationsReminders;
import ai.medicus.medicuscore.MedicusAI;
import ai.medicus.medicuscore.Metro;
import ai.medicus.medicuscore.MetroEnvironmentType;
import ai.medicus.medicuscore.Patient;
import ai.medicus.medicuscore.PersonaInterface;
import ai.medicus.medicuscore.PlatformType;
import ai.medicus.medicuscore.ScheduledNotificationsInterface;
import ai.medicus.medicuscore.WomenHealth;
import ai.medicus.views.fontExtendedViews.FontManager;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.chat.ChatClientManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.MainChatActivity_New;
import sa.med.imc.myimc.Utils.LocaleHelper;
import timber.log.Timber;

import static java.util.Locale.getDefault;
import static java.util.Locale.setDefault;

/**
 * Main application class to handle whole app activity life methods.
 */

public class ImcApplication extends MultiDexApplication {
    private static ImcApplication currentApplication = null;
    private static final String META_DATA_BASES = "MetadataDatabases_1.0.zip";
    private static final String COACHING_META_DATA_BASES = "CoachingMetadataDatabases.zip";
    private static final String WELLBEING_META_DATA_BASES = "WellbeingMetadata.zip";
    private static final String LOCALIZATION_BASES = "MedicusLocalization.db";
    private static final String ENGINE_DATA = "EngineData.zip";

    private static EngineRunType engineRunType;


    //4jan
    static {
        System.loadLibrary("MedicusCore_jni");
    }


    //    SysQuo
    private ChatClientManager basicClient;
    private MainChatActivity_New mainChatActivity;

    public ChatClientManager getChatClientManager() {
        return this.basicClient;
    }

    public MainChatActivity_New getMainChatActivity() {
        return this.mainChatActivity;
    }

    public static final String TAG = "TwilioChat";
    //    SysQuo


    // region Lifecycle Methods
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        currentApplication = this;
        initializeSDKConfigures();

        AppSignatureHelper appSignature = new AppSignatureHelper(this);
        appSignature.getAppSignatures();
        trackingFlayer();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));
        FacebookSdk.setAutoLogAppEventsEnabled(false);
        FacebookSdk.setAutoInitEnabled(false);
//        if (BuildConfig.DEBUG){
//            Timber.plant(new Timber.DebugTree());
//        }
        FacebookSdk.fullyInitialize();
//        SysQuo
        basicClient = new ChatClientManager(getApplicationContext());
//        SysQuo
    }

    public static ImcApplication getInstance() {
        return currentApplication;
    }


    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(this, SharedPreferencesUtils.getInstance(getApplicationContext()).getValue(Constants.KEY_LANGUAGE, ""));
    }

    public static void updateLanguage(Activity activity){
        String lang=SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

        Log.e("abcd","Lang updated "+lang);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    void trackingFlayer() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {


            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.e("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.e("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.e("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.e("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(getString(R.string.app_flyer_dev_key), conversionListener, currentApplication);
        AppsFlyerLib.getInstance().start(this);
    }


    private void initializeSDKConfigures() {


        // external path
        String metaDataPath = this.getExternalFilesDir(null).getPath() + "/";


        copyMetaDataFromAssets(false);


        // SDK configure
        MedicusAI.configure("MEDICUS-CORE-DEMO-FREE", metaDataPath,
                metaDataPath, "PatientDB.db", PlatformType.ANDROID_USER);
// Reminder configure
        MedicationsReminders.configure(metaDataPath,
                "MedicationsReminders.db");
// Appointment configure
        AppointmentManager.configure("MEDICUS-CORE-DEMO-FREE",
                metaDataPath, "Appointment.db");
// Metro configure
        Metro.configure(getDefault().getLanguage(),
                MetroEnvironmentType.STAGING, metaDataPath, false, null);
        Coaching.configure(metaDataPath, metaDataPath);
        WomenHealth.configure(metaDataPath);

        HeartRateSdk.configure(metaDataPath);
        ScheduledNotificationsInterface.configure(metaDataPath
                + "/Notifications");
        PersonaInterface.configure(metaDataPath);
        Patient.setServerParameters(BuildConfig.MEDICUS_SERVER_URL,
                "cerbo", 5);

        //"http://192.168.1.47:8080/imc_portal/web/medicus/"
//        patient.setServerParameter(BuildConfig.BASE_URL, "cerbo", 5);
        ai.medicus.utils.SharedPreferencesUtils.setContext(getInstance().getApplicationContext());

        FontManager.init(getAssets());


        try {
            MedicusAI.runEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // SDK configure
        //4 jan

//        MedicusAI.configure(BuildConfig.LICENSE_KEY, metaDataPath, metaDataPath, "PatientDB.db", PlatformType.ANDROID_USER);

        // Reminder configure 4 jan
//        MedicationsReminders.configure(metaDataPath, "MedicationsReminders.db");

        // Appointment configure
        //4 jan
        //  AppointmentManager.configure(BuildConfig.LICENSE_KEY, metaDataPath, "Appointment.db");

        // Metro configure
        // Metro.configure(Locale.getDefault().getLanguage(), MetroEnvironmentType.STAGING, metaDataPath,false,null);//4 jan


        //    Patient.setServerParameters("https://imc-cerbo.dev.medicus.ai/", "cerbo",  1);
        //  Patient.setServerParameters("http://192.168.1.49:8080/imc_portal_medicus/web/medicus/", "cerbo",  1); 4 jan
        // http://192.168.1.49:8080/imc_portal_medicus/web/medicus/cerbo
        //http://192.168.1.49:8080/imc_portal_medicus/web/medicus/
        //http://med-engine/
        //4jan
        //  FontManager.init(getAssets());
// 4 jan
       /* try {
            MedicusAI.runEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void copyMetaDataFromAssets(boolean checkIfExist) {

        copyAssets(META_DATA_BASES, checkIfExist);
        copyAssets(COACHING_META_DATA_BASES, checkIfExist);
        copyAssets(WELLBEING_META_DATA_BASES, checkIfExist);
        copyAssets(LOCALIZATION_BASES, checkIfExist);
        copyAssets(ENGINE_DATA, checkIfExist);

    }

    private static void copyAssets(String filePath, boolean checkIfExist) {

        InputStream in = null;
        OutputStream out = null;
        try {
            if (checkIfExist && new File(ImcApplication.getInstance().getExternalFilesDir(null), filePath).exists())
                return;
            try {
                in = ImcApplication.getInstance().getResources().getAssets().open(filePath);
                File outFile = new File(ImcApplication.getInstance().getExternalFilesDir(null), filePath);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                Log.i("tag", "copy asset file: " + filePath);
            } catch (Resources.NotFoundException exp) {
                exp.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filePath, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }

            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    //4 jan
    public static EngineRunType getEngineRunType() {
        return engineRunType;
    }

    public static boolean isRunningEngine() {

        return getEngineRunType() != null;
    }

}
