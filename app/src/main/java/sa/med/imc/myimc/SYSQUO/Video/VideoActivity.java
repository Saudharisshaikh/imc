package sa.med.imc.myimc.SYSQUO.Video;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.ion.Ion;
import com.twilio.audioswitch.AudioDevice;
import com.twilio.audioswitch.AudioSwitch;
import com.twilio.video.AudioCodec;
import com.twilio.video.ConnectOptions;
import com.twilio.video.EncodingParameters;
import com.twilio.video.G722Codec;
import com.twilio.video.H264Codec;
import com.twilio.video.IsacCodec;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.NetworkQualityConfiguration;
import com.twilio.video.NetworkQualityLevel;
import com.twilio.video.NetworkQualityVerbosity;
import com.twilio.video.OpusCodec;
import com.twilio.video.PcmaCodec;
import com.twilio.video.PcmuCodec;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.ScreenCapturer;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoCodec;
import com.twilio.video.VideoFormat;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;
import com.twilio.video.Vp8Codec;
import com.twilio.video.Vp9Codec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import kotlin.Unit;
import sa.med.imc.myimc.Login.Validate.DataModel.ValidateResponse;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Managebookings.view.ManageBookingsActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.Chat.application.SessionManager;
import sa.med.imc.myimc.SYSQUO.Chat.chat.MainChatActivity_New;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.Model.LeaveEmergencyCallRequestModel;
import sa.med.imc.myimc.SYSQUO.EmergencyCall.ViewModel.LeaveEmergencyCallViewModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.DisconnectRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.Model.ExitRoomRequestModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.ViewModel.DisconnectRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.ExitRoom.ViewModel.ExitRoomViewModel;
import sa.med.imc.myimc.SYSQUO.Video.JoningRequest.ConferenceJoiningReqViewModel;
import sa.med.imc.myimc.SYSQUO.Video.JoningRequest.ConferenceJoiningRequestMode;
import sa.med.imc.myimc.SYSQUO.Video.ScreenCapturer.ScreenCapturerManager;
import sa.med.imc.myimc.SYSQUO.util.CameraCapturerCompat;
import sa.med.imc.myimc.SYSQUO.util.Dialog;
import sa.med.imc.myimc.Utils.Common;
import tvi.webrtc.VideoSink;

public class VideoActivity extends AppCompatActivity {
    private static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_INDEX = 0;
    private static final int MIC_PERMISSION_INDEX = 1;
    private static final String TAG = "VideoActivity";

    /*
     * Audio and video tracks can be created with names. This feature is useful for categorizing
     * tracks of participants. For example, if one participant publishes a video track with
     * ScreenCapturer and CameraCapturer with the names "screen" and "camera" respectively then
     * other participants can use RemoteVideoTrack#getName to determine which video track is
     * produced from the other participant's screen or camera.
     */
    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";

    /*
     * You must provide a Twilio Access Token to connect to the Video service
     */
    private static final String TWILIO_ACCESS_TOKEN = "d1NzxQ68q6aFYEogzjmksomjSCJCJmIJ";
    private static String ACCESS_TOKEN_SERVER = null;

    /*
     * Access token used to connect. This field will be set either from the console generated token
     * or the request to the token server.
     */
    private String accessToken;

    /*
     * A Room represents communication between a local participant and one or more participants.
     */
    public Room room;
    private LocalParticipant localParticipant;

    /*
     * AudioCodec and VideoCodec represent the preferred codec for encoding and decoding audio and
     * video.
     */
    private AudioCodec audioCodec;
    private VideoCodec videoCodec;

    /*
     * Encoding parameters represent the sender side bandwidth constraints.
     */
    private EncodingParameters encodingParameters;

    /*
     * A VideoView receives frames from a local or remote video track and renders them
     * to an associated view.
     */
    private VideoView primaryVideoView;
    private VideoView thumbnailVideoView;
    private VideoView Sharing_video_view;

    /*
     * Android shared preferences used for settings
     */
    private SharedPreferences preferences;

    /*
     * Android application UI elements
     */
    private CameraCapturerCompat cameraCapturerCompat;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private LocalVideoTrack screenVideoTrack;
    private Button /*connectActionFab,*/ disconnect_action_fab, Button_VoiceLowHigh, BUtton_ChatEnable, BUtton_ScreenCapture;
    private Button switchCameraActionFab;
    private Button localVideoActionFab;
    private Button muteActionFab;
    CoordinatorLayout LayoutVideoMain;
    private ProgressBar reconnectingProgressBar;
    private ProgressBar waitingProgressBar;
    private AlertDialog connectDialog;
    private String remoteParticipantIdentity;

    /*
     * Audio management
     */
    private AudioSwitch audioSwitch;
    private int savedVolumeControlStream;
    private MenuItem audioDeviceMenuItem;

    private VideoSink localVideoView;
    private boolean disconnectedFromOnDestroy;
    private boolean enableAutomaticSubscription;
    private boolean speakerOn = false;

    boolean USE_TOKEN_SERVER = false;
    private PictureInPictureParams.Builder pictureInPictureParams;
    boolean onStopCalled = false;
    boolean isEmergency = false;
    String roomName = null;
    AlertDialog alertDialog = null;
    boolean partconnect = false;
    boolean isInPictureInPicture = false;
    boolean homeButtonPressed = false;
    String physician, mrnNumber;
    int roomID = 0;
    public static VideoActivity videoActivity;
    boolean mBackstackLost = false;
    //    Network Quality Check
    NetworkQualityConfiguration configuration;
    ConnectOptions connectOptions;
    RemoteParticipant remoteParticipant_P;
    //    SCREEN CAPTURE
    private static final int REQUEST_MEDIA_PROJECTION = 100;
    private ScreenCapturer screenCapturer;
    private ScreenCapturerManager screenCapturerManager;
    boolean isScreenSharingStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysquo_activity_video);
        videoActivity = this;

        configuration =
                new NetworkQualityConfiguration(
                        NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL,
                        NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL);

        BUtton_ChatEnable = findViewById(R.id.BUtton_ChatEnable);
        BUtton_ScreenCapture = findViewById(R.id.BUtton_ScreenCapture);
        Bundle extras = getIntent().getExtras();
        String userName;

        isEmergency = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false);
        if (isEmergency) {
            roomName = SessionManager.getInstance().getRoomEmergency();
            BUtton_ChatEnable.setVisibility(View.GONE);
            mrnNumber = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_MRN, null);
            physician = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_PHYSICIAN, "");
            roomName = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_ROOMNAME, "");
        } else {
            BUtton_ChatEnable.setVisibility(View.VISIBLE);
            mrnNumber = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_MRN, null);
            physician = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_VIDEO_PHYSICIAN, null);
            ;
            roomName = mrnNumber + "_" + physician;
        }
        ServerToken();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParams = new PictureInPictureParams.Builder();
        }
//        Layout
        LayoutVideoMain = findViewById(R.id.LayoutVideoMain);

        primaryVideoView = findViewById(R.id.primary_video_view);
        thumbnailVideoView = findViewById(R.id.thumbnail_video_view);
        Sharing_video_view = findViewById(R.id.Sharing_video_view);
        reconnectingProgressBar = findViewById(R.id.reconnecting_progress_bar);
        waitingProgressBar = findViewById(R.id.waiting_progress_bar);

//        connectActionFab = findViewById(R.id.connect_action_fab);
        disconnect_action_fab = findViewById(R.id.disconnect_action_fab);
        Button_VoiceLowHigh = findViewById(R.id.Button_VoiceLowHigh);
        switchCameraActionFab = findViewById(R.id.switch_camera_action_fab);
        localVideoActionFab = findViewById(R.id.local_video_action_fab);
        muteActionFab = findViewById(R.id.mute_action_fab);

        /*
         * Get shared preferences to read settings
         */
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        /*
         * Setup audio management and set the volume control stream
         */
        audioSwitch = new AudioSwitch(getApplicationContext());
        savedVolumeControlStream = getVolumeControlStream();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        /*
         * Check camera and microphone permissions. Needed in Android M. Also, request for bluetooth
         * permissions for enablement of bluetooth audio routing.
         */
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraMicrophoneAndBluetooth();
        } else {
            audioSwitch.start(
                    (audioDevices, audioDevice) -> {
                        updateAudioDeviceIcon(audioDevice);
                        return Unit.INSTANCE;
                    });
            createAudioAndVideoTracks();
            setAccessToken();
        }

        /*
         * Set the initial state of the UI
         */
        showWaitingDialog();
        intializeUI();
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver_Session, new IntentFilter(Constants.Filter.REFRESH_MAIN));
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.Filter.CHAT_NOTIFICATION));
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver_Video, new IntentFilter(Constants.Filter.VIDEO_REJECT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        waitingProgressBar.setVisibility(View.GONE);

//        SCREEN CAPTURE
        if (Build.VERSION.SDK_INT >= 29) {
            screenCapturerManager = new ScreenCapturerManager(this);
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    public void onBackPressed() {
        if (room != null && room.getState() != Room.State.DISCONNECTED) {
//            room.disconnect();
        } else {
            room.disconnect();
            SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                LeaveEmergencyRoom();
            } else {
                //ExitRoom();
            }
            audioSwitch.deactivate();
//        PIYUSH-24-03-22

            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("1")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, ManageBookingsActivity.class);
                startActivity(in);
                finish();
            } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("2")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("3")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, ManageBookingsActivity.class);
                startActivity(in);
                finish();
            }
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void ServerToken() {
        ConferenceJoiningReqViewModel viewModel = ViewModelProviders.of(VideoActivity.this).get(ConferenceJoiningReqViewModel.class);
        viewModel.init();
        ConferenceJoiningRequestMode conferenceJoiningRequestMode = new ConferenceJoiningRequestMode();
        conferenceJoiningRequestMode.setDoctorId(physician);
        conferenceJoiningRequestMode.setPageNumber(0);
        conferenceJoiningRequestMode.setPageSize(0);
        conferenceJoiningRequestMode.setRoomName(roomName);
        conferenceJoiningRequestMode.setUserId(mrnNumber);
        String lang;
        if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        } else {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        }

        ValidateResponse userDetails = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER);
        String userName = userDetails.getFirstName() + " " + userDetails.getLastName();
//        if (lang.equalsIgnoreCase(Constants.ARABIC)) {
//            userName=
//            if (userDetails.getLastName() != null)
//                userName = userDetails.getFirst_name_ar() + " " + userDetails.getMiddle_name_ar() + " " + userDetails.getLast_name_ar() + " " + userDetails.getFamily_name_ar();
//            else
//                userName = userDetails.getFirst_name_ar() + " " + userDetails.getLast_name_ar() + " " + userDetails.getFamily_name_ar();
//
//        } else {
//            if (userDetails.getMiddleName() != null)
//                userName = userDetails.getFirstName() + " " + userDetails.getMiddleName() + " " + userDetails.getLastName() + " " + userDetails.getFamilyName();
//            else
//                userName = userDetails.getFirstName() + " " + userDetails.getLastName() + " " + userDetails.getFamilyName();
//
//        }

        conferenceJoiningRequestMode.setUserName(userName);
        String token = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, null);
        //conferenceJoiningRequestMode.setPatientDeviceId(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_DEVICE_ID, ""));
        conferenceJoiningRequestMode.setPatientDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID));
        String HospitalID = SharedPreferencesUtils.getInstance(this).getValue(Constants.HOSPITAL_CODE, "");
        viewModel.ConfRequ(token, HospitalID, lang, conferenceJoiningRequestMode);
        viewModel.getVolumesResponseLiveData().observe(VideoActivity.this, response -> {

            if (response != null) {
                if (response.getStatus()) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_VIDEO_ROOM_ID, String.valueOf(response.getData().getRoomId()));
                    roomID = response.getData().getRoomId();
                    ACCESS_TOKEN_SERVER = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_VIDEO_ACCESS_TOKEN, null);
                    accessToken = SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_VIDEO_ACCESS_TOKEN, null);
                    connectToRoom(roomName);
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sysquo_menu_video_activity, menu);
        audioDeviceMenuItem = menu.findItem(R.id.menu_audio_device);
        // AudioSwitch has already started and thus notified of the initial selected device
        // so we need to updates the UI
        updateAudioDeviceIcon(audioSwitch.getSelectedAudioDevice());
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_audio_device:
                showAudioDevices();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            /*
             * The first two permissions are Camera & Microphone, bluetooth isn't required but
             * enabling it enables bluetooth audio routing functionality.
             */
            boolean cameraAndMicPermissionGranted =
                    (PackageManager.PERMISSION_GRANTED == grantResults[CAMERA_PERMISSION_INDEX])
                            & (PackageManager.PERMISSION_GRANTED
                            == grantResults[MIC_PERMISSION_INDEX]);

            /*
             * Due to bluetooth permissions being requested at the same time as camera and mic
             * permissions, AudioSwitch should be started after providing the user the option
             * to grant the necessary permissions for bluetooth.
             */
            audioSwitch.start(
                    (audioDevices, audioDevice) -> {
                        updateAudioDeviceIcon(audioDevice);
                        return Unit.INSTANCE;
                    });

            if (cameraAndMicPermissionGranted) {
                createAudioAndVideoTracks();
                setAccessToken();
            } else {
                Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        onStopCalled = false;
        /*
         * Update preferred audio and video codec in case changed in settings
         */
        audioCodec =
                getAudioCodecPreference(
                        SettingsActivity.PREF_AUDIO_CODEC,
                        SettingsActivity.PREF_AUDIO_CODEC_DEFAULT);
        videoCodec =
                getVideoCodecPreference(
                        SettingsActivity.PREF_VIDEO_CODEC,
                        SettingsActivity.PREF_VIDEO_CODEC_DEFAULT);
        enableAutomaticSubscription =
                getAutomaticSubscriptionPreference(
                        SettingsActivity.PREF_ENABLE_AUTOMATIC_SUBSCRIPTION,
                        SettingsActivity.PREF_ENABLE_AUTOMATIC_SUBSCRIPTION_DEFAULT);
        /*
         * Get latest encoding parameters
         */
        final EncodingParameters newEncodingParameters = getEncodingParameters();

        /*
         * If the local video track was released when the app was put in the background, recreate.
         */
        if (localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {
            localVideoTrack =
                    LocalVideoTrack.create(this, true, cameraCapturerCompat, LOCAL_VIDEO_TRACK_NAME);
            localVideoTrack.addSink(localVideoView);

            /*
             * If connected to a Room then share the local video track.
             */
            if (localParticipant != null) {
                localParticipant.publishTrack(localVideoTrack);

                /*
                 * Update encoding parameters if they have changed.
                 */
                if (!newEncodingParameters.equals(encodingParameters)) {
                    localParticipant.setEncodingParameters(newEncodingParameters);
                }
            }
        }

        /*
         * Update encoding parameters
         */
        encodingParameters = newEncodingParameters;

        /*
         * Update reconnecting UI
         */
        if (room != null) {
            reconnectingProgressBar.setVisibility(
                    (room.getState() != Room.State.RECONNECTING) ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        /*
         * Release the local video track before going in the background. This ensures that the
         * camera can be used by other applications while this app is in the background.
         */
//        if (localVideoTrack != null) {
        /*
         * If this local video track is being shared in a Room, unpublish from room before
         * releasing the video track. Participants will be notified that the track has been
         * unpublished.
         */
//            if (localParticipant != null) {
//                localParticipant.unpublishTrack(localVideoTrack);
//            }
//
//            localVideoTrack.release();
//            localVideoTrack = null;
//        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver_Session);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver_Video);

        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
            SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);

            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                LeaveEmergencyRoom();
            } else {
                //ExitRoom();
            }
            disconnectedFromOnDestroy = true;
        }
        /*
         * Tear down audio management and restore previous volume stream
         */
        audioSwitch.stop();
        setVolumeControlStream(savedVolumeControlStream);

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }

        if (Build.VERSION.SDK_INT >= 29) {
            screenCapturerManager.unbindService();
        }
//        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    private boolean checkPermissions(String[] permissions) {
        boolean shouldCheck = true;
        for (String permission : permissions) {
            shouldCheck &=
                    (PackageManager.PERMISSION_GRANTED
                            == ContextCompat.checkSelfPermission(this, permission));
        }
        return shouldCheck;
    }

    private void requestPermissions(String[] permissions) {
        boolean displayRational = false;
        for (String permission : permissions) {
            displayRational |=
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (displayRational) {
            Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this, permissions, CAMERA_MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermissionForCameraAndMicrophone() {
        return checkPermissions(
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
    }

    @SuppressLint("ObsoleteSdkInt")
    private void requestPermissionForCameraMicrophoneAndBluetooth() {
        String[] permissionsList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissionsList =
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.BLUETOOTH
                    };
        } else {
            permissionsList =
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        }
        requestPermissions(permissionsList);
    }

    private void createAudioAndVideoTracks() {
        // Share your microphone
        localAudioTrack = LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME);

        // Share your camera
        cameraCapturerCompat =
                new CameraCapturerCompat(this, CameraCapturerCompat.Source.FRONT_CAMERA);
        localVideoTrack =
                LocalVideoTrack.create(this, true, cameraCapturerCompat, LOCAL_VIDEO_TRACK_NAME);
        /*primaryVideoView.setMirror(true);
        localVideoTrack.addSink(primaryVideoView);
        localVideoView = primaryVideoView;*/
        /**
         * PIYUSH
         * Comment below code and un-comment above code
         */
        primaryVideoView.setMirror(false);
        localVideoTrack.addSink(thumbnailVideoView);
        localVideoView = thumbnailVideoView;
        thumbnailVideoView.setVisibility(View.VISIBLE);
    }

    private void setAccessToken() {
        if (!USE_TOKEN_SERVER) {
            /*
             * OPTION 1 - Generate an access token from the getting started portal
             * https://www.twilio.com/console/video/dev-tools/testing-tools and add
             * the variable TWILIO_ACCESS_TOKEN setting it equal to the access token
             * string in your local.properties file.
             */
//            this.accessToken = TWILIO_ACCESS_TOKEN;
            this.accessToken = ACCESS_TOKEN_SERVER;
        } else {
            /*
             * OPTION 2 - Retrieve an access token from your own web app.
             * Add the variable ACCESS_TOKEN_SERVER assigning it to the url of your
             * token server and the variable USE_TOKEN_SERVER=true to your
             * local.properties file.
             */
            this.accessToken = ACCESS_TOKEN_SERVER;
//            retrieveAccessTokenfromServer();
        }
    }

    private void connectToRoom(String roomName) {
        try {
            audioSwitch.activate();
            ConnectOptions.Builder connectOptionsBuilder =
                    new ConnectOptions.Builder(accessToken).roomName(roomName).enableNetworkQuality(true).networkQualityConfiguration(configuration);

            /*
             * Add local audio track to connect options to share with participants.
             */
            if (localAudioTrack != null) {
                connectOptionsBuilder.audioTracks(Collections.singletonList(localAudioTrack));
            }

            /*
             * Add local video track to connect options to share with participants.
             */
            if (localVideoTrack != null) {
                connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));
            }

            /*
             * Set the preferred audio and video codec for media.
             */
            connectOptionsBuilder.preferAudioCodecs(Collections.singletonList(audioCodec));
            connectOptionsBuilder.preferVideoCodecs(Collections.singletonList(videoCodec));

            /*
             * Set the sender side encoding parameters.
             */
            connectOptionsBuilder.encodingParameters(encodingParameters);

            /*
             * Toggles automatic track subscription. If set to false, the LocalParticipant will receive
             * notifications of track publish events, but will not automatically subscribe to them. If
             * set to true, the LocalParticipant will automatically subscribe to tracks as they are
             * published. If unset, the default is true. Note: This feature is only available for Group
             * Rooms. Toggling the flag in a P2P room does not modify subscription behavior.
             */
            connectOptionsBuilder.enableAutomaticSubscription(enableAutomaticSubscription);

            room = Video.connect(this, connectOptionsBuilder.build(), roomListener());

//            setDisconnectAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * The initial state when there is no active room.
     */

    private void intializeUI() {

        configuration =
                new NetworkQualityConfiguration(
                        NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL,
                        NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL);


//        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_video_call_white_24dp));
//        connectActionFab.show();
//        connectActionFab.setOnClickListener(connectActionClickListener());
        disconnect_action_fab.setOnClickListener(disconnectClickListener());
//        switchCameraActionFab.show();
        switchCameraActionFab.setOnClickListener(switchCameraClickListener());
//        localVideoActionFab.show();
        localVideoActionFab.setOnClickListener(localVideoClickListener());
//        muteActionFab.show();
        muteActionFab.setOnClickListener(muteClickListener());
        Button_VoiceLowHigh.setOnClickListener(voiceHighLowClickListener());

        BUtton_ChatEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureInPictureMode();
            }
        });
        BUtton_ScreenCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isScreenSharingStart) {
                    if (Build.VERSION.SDK_INT >= 29) {
                        screenCapturerManager.startForeground();
                    }
                    if (screenCapturer == null) {
                        requestScreenCapturePermission();
                    } else {
                        startScreenCapture();
                    }
//                    isScreenSharingStart = true;
                } else {
                    if (Build.VERSION.SDK_INT >= 29) {
                        screenCapturerManager.endForeground();
                    }
                    stopScreenCapture();
                    isScreenSharingStart = false;
                }
            }
        });
//        pictureInPictureMode();
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void pictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Rational aspectRational = new Rational(primaryVideoView.getWidth(), primaryVideoView.getHeight());
            pictureInPictureParams.setAspectRatio(aspectRational).build();
            enterPictureInPictureMode(pictureInPictureParams.build());
        } else {
            Toast.makeText(this, "Exit to the room222222", Toast.LENGTH_LONG).show();
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        homeButtonPressed = true;
        if (!SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!isInPictureInPictureMode()) {
                    pictureInPictureMode();
                } else {
                    Toast.makeText(this, "Exit to the room111111", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        isInPictureInPicture = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            disconnect_action_fab.setVisibility(View.GONE);
            muteActionFab.setVisibility(View.GONE);
            Button_VoiceLowHigh.setVisibility(View.GONE);
            BUtton_ChatEnable.setVisibility(View.GONE);
            BUtton_ScreenCapture.setVisibility(View.GONE);
            localVideoActionFab.setVisibility(View.GONE);
            switchCameraActionFab.setVisibility(View.GONE);
            thumbnailVideoView.setVisibility(View.GONE);
            if (partconnect == false) {
                alertDialog.dismiss();
            }

            if (!SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                if (!homeButtonPressed) {
                    try {
                        Intent in = new Intent(VideoActivity.this, MainChatActivity_New.class);
//                        in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    homeButtonPressed = false;
                }
            } else {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
            }
//            finish();
        } else {
            if (onStopCalled) {
                if (room != null && room.getState() != Room.State.DISCONNECTED) {
                    room.disconnect();
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                }
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                    LeaveEmergencyRoom();
                } else {
                  //  ExitRoom();
                }
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("1")) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(this, ManageBookingsActivity.class);
                    startActivity(in);
                    finish();
                } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("2")) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("3")) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(this, ManageBookingsActivity.class);
                    startActivity(in);
                    finish();
                }
            } else {
                mBackstackLost = true;
                try {
//                    Intent in = new Intent(this, MainActivity.class);
//                    startActivity(in);
//                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                disconnect_action_fab.setVisibility(View.VISIBLE);
                muteActionFab.setVisibility(View.VISIBLE);
                Button_VoiceLowHigh.setVisibility(View.VISIBLE);
                BUtton_ScreenCapture.setVisibility(View.GONE);
                localVideoActionFab.setVisibility(View.VISIBLE);
                switchCameraActionFab.setVisibility(View.VISIBLE);
                if (isScreenSharingStart) {
                    thumbnailVideoView.setVisibility(View.GONE);
                } else {
                    thumbnailVideoView.setVisibility(View.VISIBLE);
                }
                if (partconnect == false) {
//                    alertDialog.show();
                }
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                    BUtton_ChatEnable.setVisibility(View.GONE);
                } else {
                    BUtton_ChatEnable.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                pictureInPictureMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    BroadcastReceiver broadcastReceiver_Session = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Intent startIntent = new Intent(VideoActivity.this, VideoActivity.class);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(startIntent);
                sessionExpired();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    private void sessionExpired() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.session_expired));
        builder.setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            SharedPreferencesUtils.getInstance(VideoActivity.this).clearAll(VideoActivity.this);
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F3E3E"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(VideoActivity.this, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show();
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    BroadcastReceiver broadcastReceiver_Video = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("1")) {
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(VideoActivity.this, ManageBookingsActivity.class);
                    startActivity(in);
                    finish();
                } else if (SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("2")) {
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(VideoActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else if (SharedPreferencesUtils.getInstance(VideoActivity.this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("3")) {
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(VideoActivity.this, ManageBookingsActivity.class);
                    startActivity(in);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    /*
     * Show the current available audio devices.
     */
    private void showAudioDevices() {
        AudioDevice selectedDevice = audioSwitch.getSelectedAudioDevice();
        List<AudioDevice> availableAudioDevices = audioSwitch.getAvailableAudioDevices();

        if (selectedDevice != null) {
            int selectedDeviceIndex = availableAudioDevices.indexOf(selectedDevice);

            ArrayList<String> audioDeviceNames = new ArrayList<>();
            for (AudioDevice a : availableAudioDevices) {
                audioDeviceNames.add(a.getName());
            }

            new AlertDialog.Builder(this)
                    .setTitle(R.string.room_screen_select_device)
                    .setSingleChoiceItems(
                            audioDeviceNames.toArray(new CharSequence[0]),
                            selectedDeviceIndex,
                            (dialog, index) -> {
                                dialog.dismiss();
                                AudioDevice selectedAudioDevice = availableAudioDevices.get(index);
                                updateAudioDeviceIcon(selectedAudioDevice);
                                audioSwitch.selectDevice(selectedAudioDevice);
                            })
                    .create()
                    .show();
        }
    }

    /*
     * Update the menu icon based on the currently selected audio device.
     */
    private void updateAudioDeviceIcon(AudioDevice selectedAudioDevice) {
        if (null != audioDeviceMenuItem) {
            int audioDeviceMenuIcon = R.drawable.sysquo_ic_phonelink_ring_white_24dp;

            if (selectedAudioDevice instanceof AudioDevice.BluetoothHeadset) {
                audioDeviceMenuIcon = R.drawable.sysquo_ic_bluetooth_white_24dp;
            } else if (selectedAudioDevice instanceof AudioDevice.WiredHeadset) {
                audioDeviceMenuIcon = R.drawable.sysquo_ic_headset_mic_white_24dp;
            } else if (selectedAudioDevice instanceof AudioDevice.Earpiece) {
                audioDeviceMenuIcon = R.drawable.sysquo_ic_phonelink_ring_white_24dp;
            } else if (selectedAudioDevice instanceof AudioDevice.Speakerphone) {
                audioDeviceMenuIcon = R.drawable.sysquo_ic_volume_up_white_24dp;
            }
            audioDeviceMenuItem.setIcon(audioDeviceMenuIcon);
        }
    }

    /*
     * Get the preferred audio codec from shared preferences
     */
    private AudioCodec getAudioCodecPreference(String key, String defaultValue) {
        final String audioCodecName = preferences.getString(key, defaultValue);

        switch (audioCodecName) {
            case IsacCodec.NAME:
                return new IsacCodec();
            case OpusCodec.NAME:
                return new OpusCodec();
            case PcmaCodec.NAME:
                return new PcmaCodec();
            case PcmuCodec.NAME:
                return new PcmuCodec();
            case G722Codec.NAME:
                return new G722Codec();
            default:
                return new OpusCodec();
        }
    }

    /*
     * Get the preferred video codec from shared preferences
     */
    private VideoCodec getVideoCodecPreference(String key, String defaultValue) {
        final String videoCodecName = preferences.getString(key, defaultValue);

        switch (videoCodecName) {
            case Vp8Codec.NAME:
                boolean simulcast =
                        preferences.getBoolean(
                                SettingsActivity.PREF_VP8_SIMULCAST,
                                SettingsActivity.PREF_VP8_SIMULCAST_DEFAULT);
                return new Vp8Codec(simulcast);
            case H264Codec.NAME:
                return new H264Codec();
            case Vp9Codec.NAME:
                return new Vp9Codec();
            default:
                return new Vp8Codec();
        }
    }

    private boolean getAutomaticSubscriptionPreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private EncodingParameters getEncodingParameters() {
        final int maxAudioBitrate =
                Integer.parseInt(
                        preferences.getString(
                                SettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE,
                                SettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT));
        final int maxVideoBitrate =
                Integer.parseInt(
                        preferences.getString(
                                SettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE,
                                SettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT));

        return new EncodingParameters(maxAudioBitrate, maxVideoBitrate);
    }

    /*
     * The actions performed during disconnect.
     */
    private void setDisconnectAction() {
        /*connectActionFab.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_call_end_white_24px));
        connectActionFab.show();
        connectActionFab.setOnClickListener(disconnectClickListener());*/
    }

    /*
     * Creates an connect UI dialog
     */
    private void showConnectDialog() {
        EditText roomEditText = new EditText(this);
        connectDialog =
                Dialog.createConnectDialog(
                        roomEditText,
//                        PIYUSH
//                        connectClickListener(roomEditText),
                        connectClickListener(roomEditText.getText().toString()),
                        cancelConnectDialogClickListener(),
                        this);
        connectDialog.show();
    }

    /*
     * Called when remote participant joins the room
     */
    @SuppressLint("SetTextI18n")
    private void addRemoteParticipant(RemoteParticipant remoteParticipant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
            BUtton_ScreenCapture.setVisibility(View.GONE);
            Snackbar.make(
                            disconnect_action_fab,
                            "Doctor is connected",
                            Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
//            return;
        }
        remoteParticipantIdentity = remoteParticipant.getIdentity();

        /*
         * Add remote participant renderer
         */
        alertDialog.dismiss();
        partconnect = true;
        if (!SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
            BUtton_ChatEnable.setVisibility(View.VISIBLE);
        } else {

            BUtton_ChatEnable.setVisibility(View.GONE);
        }
        waitingProgressBar.setVisibility(View.GONE);
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Only render video tracks that are subscribed to
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                addRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }

        /*
         * Start listening for participant events
         */
        remoteParticipant.setListener(remoteParticipantListener());
    }

    /*
     * Set primary view as renderer for participant video track
     */
    private void addRemoteParticipantVideo(VideoTrack videoTrack) {
        moveLocalVideoToThumbnailView();
        primaryVideoView.setMirror(false);
        videoTrack.addSink(primaryVideoView);
    }

    private void moveLocalVideoToThumbnailView() {
//        if (thumbnailVideoView.getVisibility() == View.GONE) {
        if (isInPictureInPicture) {
            thumbnailVideoView.setVisibility(View.GONE);
        } else {
            thumbnailVideoView.setVisibility(View.VISIBLE);
        }
        localVideoTrack.removeSink(primaryVideoView);
        localVideoTrack.addSink(thumbnailVideoView);
        localVideoView = thumbnailVideoView;
        thumbnailVideoView.setMirror(
                cameraCapturerCompat.getCameraSource()
                        == CameraCapturerCompat.Source.FRONT_CAMERA);
//        }
    }

    /*
     * Called when remote participant leaves the room
     */
    @SuppressLint("SetTextI18n")
    private void removeRemoteParticipant(RemoteParticipant remoteParticipant) {
        if (!remoteParticipant.getIdentity().equals(remoteParticipantIdentity)) {
            return;
        }

        /*
         * Remove remote participant renderer
         */
        if (!remoteParticipant.getRemoteVideoTracks().isEmpty()) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Remove video only if subscribed to participant track
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                removeParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }
        moveLocalVideoToPrimaryView();
    }

    private void removeParticipantVideo(VideoTrack videoTrack) {

        videoTrack.removeSink(primaryVideoView);
    }

    private void moveLocalVideoToPrimaryView() {
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
//            thumbnailVideoView.setVisibility(View.GONE);
            if (localVideoTrack != null) {
                localVideoTrack.removeSink(thumbnailVideoView);
                localVideoTrack.addSink(thumbnailVideoView);
            }
            localVideoView = thumbnailVideoView;
            primaryVideoView.setMirror(
                    cameraCapturerCompat.getCameraSource()
                            == CameraCapturerCompat.Source.FRONT_CAMERA);
            if (room != null && room.getState() != Room.State.DISCONNECTED) {
                room.disconnect();
                SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
            }
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                LeaveEmergencyRoom();
            } else {
                //ExitRoom();
            }
            intializeUI();
            audioSwitch.deactivate();
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("1")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, ManageBookingsActivity.class);
                startActivity(in);
                finish();
            } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("2")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("3")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, ManageBookingsActivity.class);
                startActivity(in);
                finish();
            }
            ;
        } else //if(isInPictureInPicture)
        {
            {
//            thumbnailVideoView.setVisibility(View.GONE);
                if (localVideoTrack != null) {
                    localVideoTrack.removeSink(thumbnailVideoView);
                    localVideoTrack.addSink(thumbnailVideoView);
                }
                localVideoView = thumbnailVideoView;
                primaryVideoView.setMirror(
                        cameraCapturerCompat.getCameraSource()
                                == CameraCapturerCompat.Source.FRONT_CAMERA);
                if (room != null && room.getState() != Room.State.DISCONNECTED) {
                    room.disconnect();
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                }
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                    LeaveEmergencyRoom();
                } else {
                    //ExitRoom();
                }
                intializeUI();
                audioSwitch.deactivate();
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("1")) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(this, ManageBookingsActivity.class);
                    startActivity(in);
                    finish();
                } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("2")) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(this, MainActivity.class);
                    startActivity(in);
                    finish();
                } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("3")) {
                    SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                    Intent in = new Intent(this, ManageBookingsActivity.class);
                    startActivity(in);
                    finish();
                }
                ;
            }
        }
    }

    /*
     * Room events listener
     */
    @SuppressLint("SetTextI18n")
    private Room.Listener roomListener() {
        return new Room.Listener() {

            @Override
            public void onConnected(Room room) {
                Log.e("abcd", "room onConnected");

                localParticipant = room.getLocalParticipant();
                setTitle(room.getName());

                for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                    addRemoteParticipant(remoteParticipant);
//                    remoteParticipantListener();
                    break;
                }
            }

            @Override
            public void onReconnecting(
                    @NonNull Room room, @NonNull TwilioException twilioException) {
                Log.e("abcd", "room onReconnecting");

                reconnectingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReconnected(@NonNull Room room) {
                Log.e("abcd", "room onReconnected");

                reconnectingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                Log.e("abcd", "room onConnectFailure");

//                Piyush Here
                audioSwitch.deactivate();
                intializeUI();
            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
                Log.e("abcd", "room onDisconnected");

                localParticipant = null;
                reconnectingProgressBar.setVisibility(View.GONE);
                VideoActivity.this.room = null;
                // Only reinitialize the UI if disconnect was not called from onDestroy()
                if (!disconnectedFromOnDestroy) {
                    audioSwitch.deactivate();
                    intializeUI();
                    moveLocalVideoToPrimaryView();
                }
            }

            @Override
            public void onParticipantConnected(Room room, RemoteParticipant remoteParticipant) {
                Log.e("abcd", "room onParticipantConnected");

                addRemoteParticipant(remoteParticipant);
                remoteParticipant.setListener(remoteParticipantListener());
            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant remoteParticipant) {
                Log.e("abcd", "room onParticipantDisconnected");

                removeRemoteParticipant(remoteParticipant);
//                remoteParticipant.setListener(remoteParticipantListener());
            }

            @Override
            public void onRecordingStarted(Room room) {
                Log.e("abcd", "room onRecordingStarted");

                /*
                 * Indicates when media shared to a Room is being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStarted");
            }

            @Override
            public void onRecordingStopped(Room room) {
                Log.e("abcd", "room onRecordingStopped");

                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStopped");
            }
        };
    }

    public NetworkQualityLevel getNetworkQualityLevel() {
        return localParticipant.getNetworkQualityLevel();
    }

    @SuppressLint("SetTextI18n")
    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.e("abcd", "onAudioTrackPublished");
                Log.i(
                        TAG,
                        String.format(
                                "onAudioTrackPublished: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteAudioTrackPublication: sid=%s, enabled=%b, "
                                        + "subscribed=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteAudioTrackPublication.getTrackSid(),
                                remoteAudioTrackPublication.isTrackEnabled(),
                                remoteAudioTrackPublication.isTrackSubscribed(),
                                remoteAudioTrackPublication.getTrackName()));
            }

            @Override
            public void onAudioTrackUnpublished(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.e("abcd", "onAudioTrackUnpublished");

                Log.i(
                        TAG,
                        String.format(
                                "onAudioTrackUnpublished: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteAudioTrackPublication: sid=%s, enabled=%b, "
                                        + "subscribed=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteAudioTrackPublication.getTrackSid(),
                                remoteAudioTrackPublication.isTrackEnabled(),
                                remoteAudioTrackPublication.isTrackSubscribed(),
                                remoteAudioTrackPublication.getTrackName()));
            }

            @Override
            public void onDataTrackPublished(
                    RemoteParticipant remoteParticipant,
                    RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.e("abcd", "onDataTrackPublished");

                Log.i(
                        TAG,
                        String.format(
                                "onDataTrackPublished: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteDataTrackPublication: sid=%s, enabled=%b, "
                                        + "subscribed=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteDataTrackPublication.getTrackSid(),
                                remoteDataTrackPublication.isTrackEnabled(),
                                remoteDataTrackPublication.isTrackSubscribed(),
                                remoteDataTrackPublication.getTrackName()));
            }

            @Override
            public void onDataTrackUnpublished(
                    RemoteParticipant remoteParticipant,
                    RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.e("abcd", "onDataTrackUnpublished");

                Log.i(
                        TAG,
                        String.format(
                                "onDataTrackUnpublished: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteDataTrackPublication: sid=%s, enabled=%b, "
                                        + "subscribed=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteDataTrackPublication.getTrackSid(),
                                remoteDataTrackPublication.isTrackEnabled(),
                                remoteDataTrackPublication.isTrackSubscribed(),
                                remoteDataTrackPublication.getTrackName()));
            }

            @Override
            public void onVideoTrackPublished(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.e("abcd", "onVideoTrackPublished");

                Log.i(
                        TAG,
                        String.format(
                                "onVideoTrackPublished: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteVideoTrackPublication: sid=%s, enabled=%b, "
                                        + "subscribed=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteVideoTrackPublication.getTrackSid(),
                                remoteVideoTrackPublication.isTrackEnabled(),
                                remoteVideoTrackPublication.isTrackSubscribed(),
                                remoteVideoTrackPublication.getTrackName()));
            }

            @Override
            public void onVideoTrackUnpublished(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.e("abcd", "onVideoTrackUnpublished");

                Log.i(
                        TAG,
                        String.format(
                                "onVideoTrackUnpublished: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteVideoTrackPublication: sid=%s, enabled=%b, "
                                        + "subscribed=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteVideoTrackPublication.getTrackSid(),
                                remoteVideoTrackPublication.isTrackEnabled(),
                                remoteVideoTrackPublication.isTrackSubscribed(),
                                remoteVideoTrackPublication.getTrackName()));
            }

            @Override
            public void onAudioTrackSubscribed(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication,
                    RemoteAudioTrack remoteAudioTrack) {
                Log.e("abcd", "onAudioTrackSubscribed");

                Log.i(
                        TAG,
                        String.format(
                                "onAudioTrackSubscribed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteAudioTrack.isEnabled(),
                                remoteAudioTrack.isPlaybackEnabled(),
                                remoteAudioTrack.getName()));
            }

            @Override
            public void onAudioTrackUnsubscribed(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication,
                    RemoteAudioTrack remoteAudioTrack) {
                Log.e("abcd", "onAudioTrackUnsubscribed");

                Log.i(
                        TAG,
                        String.format(
                                "onAudioTrackUnsubscribed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteAudioTrack.isEnabled(),
                                remoteAudioTrack.isPlaybackEnabled(),
                                remoteAudioTrack.getName()));
            }

            @Override
            public void onAudioTrackSubscriptionFailed(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication,
                    TwilioException twilioException) {
                Log.e("abcd", "onAudioTrackSubscriptionFailed");

                Log.i(
                        TAG,
                        String.format(
                                "onAudioTrackSubscriptionFailed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteAudioTrackPublication: sid=%b, name=%s]"
                                        + "[TwilioException: code=%d, message=%s]",
                                remoteParticipant.getIdentity(),
                                remoteAudioTrackPublication.getTrackSid(),
                                remoteAudioTrackPublication.getTrackName(),
                                twilioException.getCode(),
                                twilioException.getMessage()));
            }

            @Override
            public void onDataTrackSubscribed(
                    RemoteParticipant remoteParticipant,
                    RemoteDataTrackPublication remoteDataTrackPublication,
                    RemoteDataTrack remoteDataTrack) {
                Log.e("abcd", "onDataTrackSubscribed");

                Log.i(
                        TAG,
                        String.format(
                                "onDataTrackSubscribed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteDataTrack: enabled=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteDataTrack.isEnabled(),
                                remoteDataTrack.getName()));
            }

            @Override
            public void onDataTrackUnsubscribed(
                    RemoteParticipant remoteParticipant,
                    RemoteDataTrackPublication remoteDataTrackPublication,
                    RemoteDataTrack remoteDataTrack) {
                Log.e("abcd", "onDataTrackUnsubscribed");

                Log.i(
                        TAG,
                        String.format(
                                "onDataTrackUnsubscribed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteDataTrack: enabled=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteDataTrack.isEnabled(),
                                remoteDataTrack.getName()));
            }

            @Override
            public void onDataTrackSubscriptionFailed(
                    RemoteParticipant remoteParticipant,
                    RemoteDataTrackPublication remoteDataTrackPublication,
                    TwilioException twilioException) {
                Log.e("abcd", "onDataTrackSubscriptionFailed");

                Log.i(
                        TAG,
                        String.format(
                                "onDataTrackSubscriptionFailed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteDataTrackPublication: sid=%b, name=%s]"
                                        + "[TwilioException: code=%d, message=%s]",
                                remoteParticipant.getIdentity(),
                                remoteDataTrackPublication.getTrackSid(),
                                remoteDataTrackPublication.getTrackName(),
                                twilioException.getCode(),
                                twilioException.getMessage()));
            }

            @Override
            public void onVideoTrackSubscribed(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication,
                    RemoteVideoTrack remoteVideoTrack) {
                Log.e("abcd", "onVideoTrackSubscribed");

                Log.i(
                        TAG,
                        String.format(
                                "onVideoTrackSubscribed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteVideoTrack: enabled=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteVideoTrack.isEnabled(),
                                remoteVideoTrack.getName()));
                addRemoteParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackUnsubscribed(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication,
                    RemoteVideoTrack remoteVideoTrack) {
                Log.e("abcd", "onVideoTrackUnsubscribed");

                Log.i(
                        TAG,
                        String.format(
                                "onVideoTrackUnsubscribed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteVideoTrack: enabled=%b, name=%s]",
                                remoteParticipant.getIdentity(),
                                remoteVideoTrack.isEnabled(),
                                remoteVideoTrack.getName()));
                removeParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackSubscriptionFailed(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication,
                    TwilioException twilioException) {
                Log.e("abcd", "onVideoTrackSubscriptionFailed");

                Log.i(
                        TAG,
                        String.format(
                                "onVideoTrackSubscriptionFailed: "
                                        + "[RemoteParticipant: identity=%s], "
                                        + "[RemoteVideoTrackPublication: sid=%b, name=%s]"
                                        + "[TwilioException: code=%d, message=%s]",
                                remoteParticipant.getIdentity(),
                                remoteVideoTrackPublication.getTrackSid(),
                                remoteVideoTrackPublication.getTrackName(),
                                twilioException.getCode(),
                                twilioException.getMessage()));
                /*Snackbar.make(
                                connectActionFab,
                                String.format(
                                        "Failed to subscribe to %s video track",
                                        remoteParticipant.getIdentity()),
                                Snackbar.LENGTH_LONG)
                        .show();*/
            }

            @Override
            public void onAudioTrackEnabled(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.e("abcd", "onAudioTrackEnabled");

            }

            @Override
            public void onAudioTrackDisabled(
                    RemoteParticipant remoteParticipant,
                    RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.e("abcd", "onAudioTrackDisabled");

            }

            @Override
            public void onVideoTrackEnabled(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.e("abcd", "onVideoTrackEnabled");

                primaryVideoView.setVisibility(View.VISIBLE);
//                Common.makeToast(VideoActivity.this, "Video Enable");
            }

            @Override
            public void onVideoTrackDisabled(
                    RemoteParticipant remoteParticipant,
                    RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.e("abcd", "onVideoTrackDisabled");

                primaryVideoView.setVisibility(View.GONE);
//                Common.makeToast(VideoActivity.this, "Video Disable");
            }

            @Override
            public void onNetworkQualityLevelChanged(
                    @NonNull RemoteParticipant remoteParticipant,
                    @NonNull NetworkQualityLevel networkQualityLevel) {
                Log.e("abcd", "onNetworkQualityLevelChanged");

            }
        };
    }


    //                        PIYUSH
//    private DialogInterface.OnClickListener connectClickListener(final EditText roomEditText) {
    private DialogInterface.OnClickListener connectClickListener(final String roomEditText) {
        return (dialog, which) -> {
            /*
             * Connect to room
             */
//            PIYUSH
//            connectToRoom(roomEditText.getText().toString());
            connectToRoom(roomEditText.toString());
        };
    }

    private View.OnClickListener disconnectClickListener() {
        return v -> {
            /*
             * Disconnect from room
             */
            if (room != null) {
                room.disconnect();
                SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_EMERGENCY_CALL, false)) {
                    LeaveEmergencyRoom();
                } else {
                   // ExitRoom();
                }

//                        PIYUSH
                setTitle(getResources().getString(R.string.app_name));
            }
            intializeUI();
            audioSwitch.deactivate();
            if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("1")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, ManageBookingsActivity.class);
                startActivity(in);
                finish();
            } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("2")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            } else if (SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_VIDEO_BACK_CLASS, null).equals("3")) {
                SharedPreferencesUtils.getInstance(this).setValue(Constants.KEY_NAV_CLASS, false);
                Intent in = new Intent(this, ManageBookingsActivity.class);
                startActivity(in);
                finish();
            }
        };
    }

    private View.OnClickListener connectActionClickListener() {
//                        PIYUSH
//        return v -> showConnectDialog();
        return v -> connectToRoom(roomName);
//        return v -> connectToRoom("coolroom");
    }

    private DialogInterface.OnClickListener cancelConnectDialogClickListener() {
        return (dialog, which) -> {
            intializeUI();
            connectDialog.dismiss();
        };
    }

    private View.OnClickListener switchCameraClickListener() {
        return v -> {
            if (cameraCapturerCompat != null) {
                CameraCapturerCompat.Source cameraSource = cameraCapturerCompat.getCameraSource();
                cameraCapturerCompat.switchCamera();
                if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                    thumbnailVideoView.setMirror(
                            cameraSource == CameraCapturerCompat.Source.BACK_CAMERA);
                } else {
                    primaryVideoView.setMirror(
                            cameraSource == CameraCapturerCompat.Source.BACK_CAMERA);
                }
            }
        };
    }

    private View.OnClickListener localVideoClickListener() {
        return v -> {
            /*
             * Enable/disable the local video track
             */
            if (localVideoTrack != null) {
                boolean enable = !localVideoTrack.isEnabled();
                localVideoTrack.enable(enable);

                int icon;
                if (enable) {
                    icon = R.drawable.sysquo_video_pause;
//                    switchCameraActionFab.show();
                } else {
                    icon = R.drawable.sysquo_video_play;
//                    switchCameraActionFab.hide();
                }
                /*localVideoActionFab.setImageDrawable(
                        ContextCompat.getDrawable(this, icon));*/
                Drawable top = getResources().getDrawable(icon);
                localVideoActionFab.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            }
        };
    }

    private View.OnClickListener muteClickListener() {
        return v -> {
            /*
             * Enable/disable the local audio track. The results of this operation are
             * signaled to other Participants in the same Room. When an audio track is
             * disabled, the audio is muted.
             */
            if (localAudioTrack != null) {
                boolean enable = !localAudioTrack.isEnabled();
                localAudioTrack.enable(enable);
                int icon = enable ? R.drawable.sysquo_mic_on : R.drawable.sysquo_mic_off;
                Drawable top = getResources().getDrawable(icon);
                muteActionFab.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
//                muteActionFab.setBackgroundDrawable(ContextCompat.getDrawable(this, icon));
            }
        };
    }

    private View.OnClickListener voiceHighLowClickListener() {
        return v -> {
            AudioDevice selectedDevice = audioSwitch.getSelectedAudioDevice();
            List<AudioDevice> availableAudioDevices = audioSwitch.getAvailableAudioDevices();

            if (selectedDevice != null) {
                int selectedDeviceIndex = availableAudioDevices.indexOf(selectedDevice);

                ArrayList<String> audioDeviceNames = new ArrayList<>();
                for (AudioDevice a : availableAudioDevices) {
                    audioDeviceNames.add(a.getName());
                }

                if (speakerOn == true) {
                    try {
                        int position = audioDeviceNames.indexOf("Earpiece");
                        AudioDevice selectedAudioDevice = availableAudioDevices.get(position);
                        updateAudioDeviceIcon(selectedAudioDevice);
                        audioSwitch.selectDevice(selectedAudioDevice);
                        speakerOn = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int position = audioDeviceNames.indexOf("Speakerphone");
                        AudioDevice selectedAudioDevice = availableAudioDevices.get(position);
                        updateAudioDeviceIcon(selectedAudioDevice);
                        audioSwitch.selectDevice(selectedAudioDevice);

                        speakerOn = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int icon = speakerOn ? R.drawable.sysquo_laudspeaker_on : R.drawable.sysquo_loudspeaker_off;
                Drawable top = getResources().getDrawable(icon);
                Button_VoiceLowHigh.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            }
        };
    }

    private void retrieveAccessTokenfromServer() {
        Ion.with(this)
                .load(
                        String.format(
                                "%s?identity=%s",
                                ACCESS_TOKEN_SERVER, UUID.randomUUID().toString()))
                .asString()
                .setCallback(
                        (e, token) -> {
                            if (e == null) {
                                this.accessToken = token;
                            } else {
                                Toast.makeText(
                                                VideoActivity.this,
                                                R.string.error_retrieving_access_token,
                                                Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void ExitRoom() {
        String lang;
        if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        } else {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        }
        audioSwitch.deactivate();
        ExitRoomViewModel viewModel = ViewModelProviders.of(VideoActivity.this).get(ExitRoomViewModel.class);
        viewModel.init();
        ExitRoomRequestModel exitRoomRequestModel = new ExitRoomRequestModel();
        exitRoomRequestModel.setPageNumber(0);
        exitRoomRequestModel.setPageSize(0);
        exitRoomRequestModel.setRoomId(roomID);
        String token = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, null);
        viewModel.ExitRoom(token, lang, exitRoomRequestModel);
        viewModel.getVolumesResponseLiveData().observe(VideoActivity.this, response -> {

            if (response != null) {
                if (response.getStatus()) {
                    if (room != null && room.getState() != Room.State.DISCONNECTED) {
                        room.disconnect();
                        SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                    }
                    ExitRoom2();
//                    finish();
                }
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void ExitRoom2() {
        String lang;
        if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        } else {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        }
        DisconnectRoomViewModel viewModel = ViewModelProviders.of(VideoActivity.this).get(DisconnectRoomViewModel.class);
        viewModel.init();
        DisconnectRoomRequestModel disconnectRoomRequestModel = new DisconnectRoomRequestModel();
        disconnectRoomRequestModel.setDoctorId(physician);
        disconnectRoomRequestModel.setRoomName(roomName);
        disconnectRoomRequestModel.setUserId(mrnNumber);
        String token = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, null);
        viewModel.DisconnectRoom(token, lang, disconnectRoomRequestModel);
        viewModel.getVolumesResponseLiveData().observe(VideoActivity.this, response -> {

            if (response != null) {
                if (response.getStatus()) {
//                    room.disconnect();
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
//                    ExitRoom2();
                    finish();
                }
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void LeaveEmergencyRoom() {
        String lang;
        if (SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_ACCESS_TOKEN, "").length() == 0) {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LOGIN_LANGUAGE, Constants.ENGLISH);
        } else {
            lang = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        }
        audioSwitch.deactivate();
        LeaveEmergencyCallViewModel viewModel = ViewModelProviders.of(VideoActivity.this).get(LeaveEmergencyCallViewModel.class);
        viewModel.init();
        LeaveEmergencyCallRequestModel leaveEmergencyCallRequestModel = new LeaveEmergencyCallRequestModel();
        leaveEmergencyCallRequestModel.setUserId(mrnNumber);
        String token = SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, null);
        viewModel.LeaveEmergencyCall(token, lang, leaveEmergencyCallRequestModel);
        viewModel.getVolumesResponseLiveData().observe(VideoActivity.this, response -> {

            if (response != null) {
                if (response.getStatus()) {
                    SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_EMERGENCY_CALL, false);
                    if (room != null && room.getState() != Room.State.DISCONNECTED) {
                        room.disconnect();
                        SharedPreferencesUtils.getInstance(VideoActivity.this).setValue(Constants.KEY_NAV_CLASS, false);
                    }
                }
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    protected void onStop() {
        super.onStop();
        onStopCalled = true;
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    public void showWaitingDialog() {
        try {
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.sysquo_dialog_waiting_video, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
            builder.setView(dialogView);
            alertDialog = builder.create();
            alertDialog.setCancelable(true);

            LinearLayout linearChatClick = dialogView.findViewById(R.id.linearChatClick);


            linearChatClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pictureInPictureMode();
                }
            });

//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            // get the top most window of the android
            // screen using getWindow() method
            // and set the the gravity of the window to
            // top that is alert dialog will be now at
            // the topmost position

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
//    SCREEN CAPTURE
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    private void requestScreenCapturePermission() {
        Log.d(TAG, "Requesting permission to capture screen");
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // This initiates a prompt dialog for the user to confirm screen projection.
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION);
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    private void startScreenCapture() {
        isScreenSharingStart = true;
        localParticipant = room.getLocalParticipant();
        try {
            if (localParticipant != null) {
                localParticipant.unpublishTrack(localVideoTrack);
            }


            screenVideoTrack = LocalVideoTrack.create(this, true, screenCapturer);
            BUtton_ScreenCapture.setBackground(ContextCompat.getDrawable(this, R.drawable.sysquo_ic_stop_screen_share_white_24dp));
//        screenCaptureMenuItem.setTitle(R.string.stop_screen_share);

            Sharing_video_view.setVisibility(View.VISIBLE);
            thumbnailVideoView.setVisibility(View.GONE);
            screenVideoTrack.addSink(localVideoView);
            if (localParticipant != null) {
                localParticipant.publishTrack(screenVideoTrack);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    private final ScreenCapturer.Listener screenCapturerListener =
            new ScreenCapturer.Listener() {
                @Override
                public void onScreenCaptureError(String errorDescription) {
                    Log.e(TAG, "Screen capturer error: " + errorDescription);
                    stopScreenCapture();
                    Toast.makeText(
                                    VideoActivity.this,
                                    R.string.screen_capture_error,
                                    Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onFirstFrameAvailable() {
                    Log.d(TAG, "First frame from screen capturer available");
                }
            };

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    private void stopScreenCapture() {
        try {
            if (localVideoTrack != null) {
//            localVideoTrack.removeSink(localVideoView);
//            localVideoTrack.release();
//            localVideoTrack = null;

                if (localVideoTrack != null) {
                    localVideoTrack.removeSink(thumbnailVideoView);
                    localVideoTrack.addSink(thumbnailVideoView);
                }
//            localVideoView = thumbnailVideoView;
//            primaryVideoView.setMirror(
//                    cameraCapturerCompat.getCameraSource()
//                            == CameraCapturerCompat.Source.FRONT_CAMERA);
                localParticipant = room.getLocalParticipant();
                if (localParticipant != null) {
                    localParticipant.unpublishTrack(screenVideoTrack);
                    localParticipant.publishTrack(localVideoTrack);
                }

                Sharing_video_view.setVisibility(View.VISIBLE);
                thumbnailVideoView.setVisibility(View.VISIBLE);

                BUtton_ScreenCapture.setBackground(ContextCompat.getDrawable(this, R.drawable.sysquo_ic_screen_share_white_24dp));
//            screenCaptureMenuItem.setTitle(R.string.share_screen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                Toast.makeText(
                                this,
                                R.string.screen_capture_permission_not_granted,
                                Toast.LENGTH_LONG)
                        .show();
                return;
            }
            screenCapturer = new ScreenCapturer(this, resultCode, data, screenCapturerListener);
            startScreenCapture();
        }
    }
//------------------------------------------------------------------------------------------------\\
//------------------------------------------------------------------------------------------------//
}
