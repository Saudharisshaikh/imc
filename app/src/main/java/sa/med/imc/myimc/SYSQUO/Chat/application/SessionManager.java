package sa.med.imc.myimc.SYSQUO.Chat.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import sa.med.imc.myimc.Network.ImcApplication;

public class SessionManager {
  public static final String KEY_USERNAME = "username";
  public static final String KEY_IDENTITY = "identity";
  public static final String KEY_ROOM = "roomName";
  public static final String KEY_CHANNEL = "channelID";
  public static final String KEY_ROOM_EMER = "roomNameEmergency";
  public static final String KEY_CHANNEL_EMER = "channelIDEmergency";
  public static final String KEY_DOCTORE = "doctorId";
  private static final String PREF_NAME = "TWILIOCHAT";
  private static final String IS_LOGGED_IN = "IsLoggedIn";
  private static SessionManager instance =
      new SessionManager(ImcApplication.getInstance().getApplicationContext());
  SharedPreferences pref;
  Editor editor;
  Context context;
  int PRIVATE_MODE = 0;

  private SessionManager(Context context) {
    this.context = context;
    pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
  }

  public static SessionManager getInstance() {
    return instance;
  }

  public void createLoginSession(String username) {
    editor.putBoolean(IS_LOGGED_IN, true);
    editor.putString(KEY_USERNAME, username);
    // commit changes
    editor.commit();
  }

  public HashMap<String, String> getUserDetails() {
    HashMap<String, String> user = new HashMap<String, String>();
    user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

    return user;
  }
  public void setIdentity(String identity) {
    editor.putString(KEY_IDENTITY, identity);
    // commit changes
    editor.commit();
  }
  public void setRoom(String room) {
    editor.putString(KEY_ROOM, room);
    // commit changes
    editor.commit();
  }
  public void setChannel(String channel) {
    editor.putString(KEY_CHANNEL, channel);
    // commit changes
    editor.commit();
  }
  public void setRoomEmergency(String room) {
    editor.putString(KEY_ROOM_EMER, room);
    // commit changes
    editor.commit();
  }
  public void setChannelEmergency(String channel) {
    editor.putString(KEY_CHANNEL_EMER, channel);
    // commit changes
    editor.commit();
  }
  public void setDoctorId(String doctorId) {
    editor.putString(KEY_DOCTORE, doctorId);
    // commit changes
    editor.commit();
  }

  public String getUsername() {
    return pref.getString(KEY_USERNAME, null);
  }
  public String getIdentity() {
    return pref.getString(KEY_IDENTITY, null);
  }
  public String getRoom() {
    return pref.getString(KEY_ROOM, null);
  }
  public String getChannel() {
    return pref.getString(KEY_CHANNEL, null);
  }
  public String getRoomEmergency() {
    return pref.getString(KEY_ROOM_EMER, null);
  }
  public String getChannelEmergency() {
    return pref.getString(KEY_CHANNEL_EMER, null);
  }
  public String getDoctorId() {
    return pref.getString(KEY_DOCTORE, null);
  }

  public void logoutUser() {
    editor = editor.clear();
    editor.commit();
  }

  public boolean isLoggedIn() {
    return pref.getBoolean(IS_LOGGED_IN, false);
  }

}

