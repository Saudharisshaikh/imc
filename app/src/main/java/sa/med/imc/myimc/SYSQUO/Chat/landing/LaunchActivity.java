package sa.med.imc.myimc.SYSQUO.Chat.landing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.Locale;

import sa.med.imc.myimc.SYSQUO.Chat.application.SessionManager;
import sa.med.imc.myimc.SYSQUO.Main.MainActivity;
import sa.med.imc.myimc.SYSQUO.Selection.SelectionActivity;
import sa.med.imc.myimc.SYSQUO.util.Constants;


public class LaunchActivity extends Activity {

  private String language;
  private SharedPreferences sharedPreferences;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_LANGUAGE,MODE_PRIVATE);
    language = sharedPreferences.getString(Constants.KEY_LANGUAGE,"");
    if (language.matches("en")){
      language = "en";
      setLocale(language);
    }else{
      language = "ar";
      setLocale(language);
    }
    Intent launchIntent = new Intent();
    Class<?> launchActivity;

    launchActivity = getLaunchClass();
    launchIntent.setClass(getApplicationContext(), launchActivity);
    startActivity(launchIntent);

    finish();
  }

  private Class<?> getLaunchClass() {
    if (SessionManager.getInstance().isLoggedIn()) {
      return SelectionActivity.class;
    } else {
      return MainActivity.class;
    }
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
