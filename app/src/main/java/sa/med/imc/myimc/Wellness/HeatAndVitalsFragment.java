package sa.med.imc.myimc.Wellness;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.data.HealthFields;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlinx.coroutines.channels.BroadcastChannel;
import sa.med.imc.myimc.Adapter.DatesAdapter;
import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.FitnessAndWellness.value.AddHealthValueSheet;
import sa.med.imc.myimc.FitnessAndWellness.value.model.HealthValueModel;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Listeners.DateSelectionListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.databinding.FragmentHeatAndVitalsBinding;
import sa.med.imc.myimc.models.Date;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.fitness.data.HealthDataTypes.TYPE_BLOOD_PRESSURE;


public class HeatAndVitalsFragment extends BaseFragment implements DateSelectionListener {
    private final String TAG = this.getClass().getName();
    private final BroadcastReceiver receiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          if (intent.getAction().equals(Constants.REFRESH)){
              getHeartRateHistory(startTime, endTime);
              getBloodPressureHistory(startTime, endTime);
              getBloodGlucoseHistory(startTime, endTime);
              getBloodOxygenHistory(startTime, endTime);
              getBodyTemperatureHistory(startTime, endTime);
          }
        }
    };
    private long startTime;
    private long endTime;
    private FragmentHeatAndVitalsBinding binding;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_heart_rate)
    TextView tvHeartRate;
    @BindView(R.id.tv_bp_systolic)
    TextView tvBpSystolic;
    @BindView(R.id.tv_bp_diastolic)
    TextView tvBpDiastolic;
    @BindView(R.id.tv_blood_gulocose)
    TextView tvBloodGulocose;
    @BindView(R.id.tv_blood_oxygen)
    TextView tvBloodOxygen;
    @BindView(R.id.tv_body_temp)
    TextView tvBodyTemperature;
    @BindView(R.id.rv_dates)
    RecyclerView rvDates;
    @BindView(R.id.today)
    TextView tvToday;
//    @BindView(R.id.cardAssessment)
//    CardView cardAssessment;

    java.util.Date currentDate;
    int currentDateNum = 0;
    int datesRecyclerAdapterCount = 0;
    List<Date> dates = new ArrayList();

    FitnessOptions fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.TYPE_DISTANCE_DELTA)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED)
            .addDataType(DataType.TYPE_MOVE_MINUTES)
            .addDataType(DataType.TYPE_HEIGHT)
            .addDataType(DataType.TYPE_WEIGHT)
            .addDataType(DataType.TYPE_HEART_RATE_BPM)
            .addDataType(TYPE_BLOOD_PRESSURE)
            .addDataType(HealthDataTypes.TYPE_BLOOD_GLUCOSE)
            .addDataType(HealthDataTypes.TYPE_OXYGEN_SATURATION)
            .addDataType(HealthDataTypes.TYPE_BODY_TEMPERATURE)
            .addDataType(DataType.TYPE_SLEEP_SEGMENT).build();

    private Boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    FragmentListener fragmentAdd;
    private OnDataPointListener onDataPointListener;


    public HeatAndVitalsFragment() {
        // Required empty public constructor
    }

    public static HeatAndVitalsFragment newInstance() {
        HeatAndVitalsFragment fragment = new HeatAndVitalsFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHeatAndVitalsBinding.inflate(inflater, container, false);
        ButterKnife.bind(this, binding.getRoot());

        receiveArguments();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver,new IntentFilter(Constants.REFRESH));
        binding.addHeartRate.setOnClickListener(v->{
            ArrayList<HealthValueModel> list = new ArrayList<>();
            HealthValueModel model =new HealthValueModel("HeartRate",Field.FIELD_BPM,DataType.TYPE_HEART_RATE_BPM,"Bpm","");
            list.add(model);
            AddHealthValueSheet.newInstance(list,startTime,endTime)
                    .showNow(getChildFragmentManager(),"");
        });
        binding.addBloodPressure.setOnClickListener(v->{
            ArrayList<HealthValueModel> list = new ArrayList<>();
            HealthValueModel sys =new HealthValueModel("Systolic",HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC,HealthDataTypes.TYPE_BLOOD_PRESSURE,"Sys","");
            HealthValueModel dia =new HealthValueModel("Diastolic",HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC,HealthDataTypes.TYPE_BLOOD_PRESSURE,"Dia","");
            list.add(sys);
            list.add(dia);
            AddHealthValueSheet.newInstance(list,startTime,endTime)
                    .showNow(getChildFragmentManager(),"");
        });
        binding.addGlucose.setOnClickListener(v->{
            ArrayList<HealthValueModel> list = new ArrayList<>();
            HealthValueModel model =new HealthValueModel("Glucose",HealthFields.FIELD_BLOOD_GLUCOSE_LEVEL,HealthDataTypes.TYPE_BLOOD_GLUCOSE,"mmol/L","");
            list.add(model);
            AddHealthValueSheet.newInstance(list,startTime,endTime)
                    .showNow(getChildFragmentManager(),"");
        });
        binding.addOxygen.setOnClickListener(v->{
            ArrayList<HealthValueModel> list = new ArrayList<>();
            HealthValueModel oxygen =new HealthValueModel("Oxygen",HealthFields.FIELD_OXYGEN_SATURATION,HealthDataTypes.TYPE_OXYGEN_SATURATION,"%","");
            HealthValueModel saturationFLow =new HealthValueModel("Oxygen",HealthFields.FIELD_SUPPLEMENTAL_OXYGEN_FLOW_RATE,HealthDataTypes.TYPE_OXYGEN_SATURATION,"%","80");
            list.add(oxygen);
            list.add(saturationFLow);
            AddHealthValueSheet.newInstance(list,startTime,endTime)
                    .showNow(getChildFragmentManager(),"");
        });
        binding.addBodyTemp.setOnClickListener(v->{
            ArrayList<HealthValueModel> list = new ArrayList<>();
            HealthValueModel model =new HealthValueModel("Body Temperature",HealthFields.FIELD_BODY_TEMPERATURE,HealthDataTypes.TYPE_BODY_TEMPERATURE,"F","");
            list.add(model);
            AddHealthValueSheet.newInstance(list,startTime,endTime)
                    .showNow(getChildFragmentManager(),"");
        });
        init();
    }

    private void receiveArguments() {

    }

    @Override
    public void onResume() {
        super.onResume();

        readData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            GoogleFitActions postSignInAction = GoogleFitActions.values()[requestCode];
            if (postSignInAction != null) {
                performActionForRequestCode(postSignInAction);
            }
        } else {
            Common.showAlert(requireActivity(), "There is an error signing into Google Fit. Please try again later." );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            if (fragmentAdd != null)
                fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
            break;

        }
    }

    private void init() {
        setupCalendar();
        rvDates.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(new DatesAdapter(requireContext(), dates, currentDateNum, this::onSelected));
        rvDates.post(() -> rvDates.smoothScrollToPosition(currentDateNum + 2));
    }

    private void setupCalendar() {
        try {
            currentDateNum = 0;
            datesRecyclerAdapterCount = 0;
            dates.clear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat dayFormat = new SimpleDateFormat("E");
            SimpleDateFormat dateFormat = new SimpleDateFormat("d");

            currentDate = calendar.getTime();

            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int day = 0; day < daysInPreviousMonth; day++) {
                calendar.set(Calendar.DAY_OF_MONTH, day + 1);
                java.util.Date currentDay = calendar.getTime();

                if (currentDay.before(currentDate) || currentDay.equals(currentDate)) {
                    Date tempDate;
                    if (currentDay.equals(currentDate)) {
                        tempDate = new Date(
                                dayFormat.format(calendar.getTime()),
                                Integer.parseInt(dateFormat.format(calendar.getTime())),
                                calendar.get(Calendar.MONTH),
                                true,
                                false
                        );
                        currentDateNum = datesRecyclerAdapterCount;
                    } else {
                        tempDate = new Date(
                                dayFormat.format(calendar.getTime()),
                                Integer.parseInt(dateFormat.format(calendar.getTime())),
                                calendar.get(Calendar.MONTH),
                                false,
                                false
                        );
                    }

                    dates.add(tempDate);
                } else {
                    Date tempDate = new Date(
                            dayFormat.format(calendar.getTime()),
                            Integer.parseInt(dateFormat.format(calendar.getTime())),
                            calendar.get(Calendar.MONTH),
                            false,
                            true
                    );

                    dates.add(tempDate);
                }
                datesRecyclerAdapterCount++;
            }

            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int day = 0; day < daysInMonth; day++) {
                calendar.set(Calendar.DAY_OF_MONTH, day + 1);
                java.util.Date currentDay = calendar.getTime();
                if (currentDay.before(currentDate) || currentDay.equals(currentDate)) {
                    Date tempDate;

                    if (currentDay.equals(currentDate)) {
                        tempDate = new Date(
                                dayFormat.format(calendar.getTime()),
                                Integer.parseInt(dateFormat.format(calendar.getTime())),
                                calendar.get(Calendar.MONTH),
                                true,
                                false
                        );
                        currentDateNum = datesRecyclerAdapterCount;
                    } else {
                        tempDate = new Date(
                                dayFormat.format(calendar.getTime()),
                                Integer.parseInt(dateFormat.format(calendar.getTime())),
                                calendar.get(Calendar.MONTH),
                                false,
                                false
                        );

                    }

                    dates.add(tempDate);
                } else {
                    Date tempDate = new Date(
                            dayFormat.format(calendar.getTime()),
                            Integer.parseInt(dateFormat.format(calendar.getTime())),
                            calendar.get(Calendar.MONTH),
                            false,
                            true
                    );

                    dates.add(tempDate);
                }
                datesRecyclerAdapterCount++;
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void checkPermissionsAndRun(GoogleFitActions googleFitActions) {
        if (permissionApproved()) {
            fitSignIn(googleFitActions);
        } else {
            requestRuntimePermissions(googleFitActions);
        }
    }

    private void fitSignIn(GoogleFitActions googleFitActions) {
        if (oAuthPermissionsApproved()) {
            performActionForRequestCode(googleFitActions);
        } else {
            if (googleFitActions != null) {
                GoogleSignIn.requestPermissions(
                        this,
                        googleFitActions.ordinal(),
                        getGoogleAccount(), fitnessOptions);
            }
        }
    }

    private Boolean oAuthPermissionsApproved() {
        return GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions);
    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions);
    }

    private void performActionForRequestCode(GoogleFitActions googleFitActions) {
        switch (googleFitActions) {

        }
    }

    private Boolean permissionApproved() {
        Boolean activityRecognition;
        Boolean location;

        if (runningQOrLater) {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACTIVITY_RECOGNITION)) {
                activityRecognition = true;
            } else {
                activityRecognition = false;
            }

            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                location = true;
            } else {
                location = false;
            }
        } else {
            activityRecognition = true;
            location = true;
        }

        return activityRecognition && location;
    }

    private void requestRuntimePermissions(GoogleFitActions googleFitActions) {
        String[] requiredPermissions = {Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION};

        Boolean shouldProvideRationaleOfActivityRecognition =
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION);

        Boolean shouldProvideRationaleOfLocation =
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (googleFitActions != null) {
            if (shouldProvideRationaleOfActivityRecognition) {
                Common.showAlert(requireActivity(), "We Need Activity Recognition Permission For Recording Your Health Data.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissions(requiredPermissions, googleFitActions.ordinal());
                    }
                });

            } else if (shouldProvideRationaleOfLocation) {
                Common.showAlert(requireActivity(), "We Need Fine Location Permission For Recording Your Health Data.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissions(requiredPermissions, googleFitActions.ordinal());
                    }
                });
            } else {
                requestPermissions(requiredPermissions, googleFitActions.ordinal());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (grantResults.length <= 0) {
            Log.i(TAG, "User interaction was cancelled.");
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            GoogleFitActions googleFitActions = GoogleFitActions.values()[requestCode];
            if (googleFitActions != null) {
                fitSignIn(googleFitActions);
            }
        } else {
            Common.showAlert(requireActivity(), requireActivity().getString(R.string.permission_denied_explanation), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    private void readData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        endTime = calendar.getTimeInMillis();

        getHeartRateHistory(startTime, endTime);
        getBloodPressureHistory(startTime, endTime);
        getBloodGlucoseHistory(startTime, endTime);
        getBloodOxygenHistory(startTime, endTime);
        getBodyTemperatureHistory(startTime, endTime);
    }

    @Override
    public void onSelected(@NotNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
        calendar.set(Calendar.MONTH, date.getMonth());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startTime = calendar.getTimeInMillis();

        java.util.Date currentDay = calendar.getTime();

        if (currentDay == currentDate) {
            tvToday.setText("Today");
        } else {
            SimpleDateFormat todayFormat = new SimpleDateFormat("EEE, MMM d");
            String todayDate = todayFormat.format(calendar.getTime());
            tvToday.setText(todayDate);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        endTime = calendar.getTimeInMillis();

        getHeartRateHistory(startTime, endTime);
        getBloodPressureHistory(startTime, endTime);
        getBloodGlucoseHistory(startTime, endTime);
        getBloodOxygenHistory(startTime, endTime);
        getBodyTemperatureHistory(startTime, endTime);
    }

    private void getHeartRateHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_HEART_RATE_BPM)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpHeartRateDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void getBloodPressureHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(TYPE_BLOOD_PRESSURE)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpBloodPressureDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void getBloodGlucoseHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(HealthDataTypes.TYPE_BLOOD_GLUCOSE)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpBloodGlucoseDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void getBloodOxygenHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(HealthDataTypes.TYPE_OXYGEN_SATURATION)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpBloodOxygenDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void getBodyTemperatureHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(HealthDataTypes.TYPE_BODY_TEMPERATURE)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpBodyTemperatureDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void dumpHeartRateDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double bpm = dp.getValue(field).asFloat();
                    tvHeartRate.setText(String.format("%.0f", bpm));
                }
            }
        } else {
            tvHeartRate.setText("0");
        }
    }

    private void dumpBloodPressureDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double systolic = dp.getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC_MAX).asFloat();
                    double diastolic = dp.getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC_MAX).asFloat();

                    tvBpSystolic.setText(String.format("%.1f", systolic));
                    tvBpDiastolic.setText(String.format("%.1f", diastolic));
                }
            }
        } else {
            tvBpSystolic.setText("0.0");
            tvBpDiastolic.setText("0.0");
        }
    }

    private void dumpBloodGlucoseDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double bloodGlucose = dp.getValue(field).asFloat();
                    tvBloodGulocose.setText(String.format("%.0f", bloodGlucose));
                    break;
                }
            }
        } else {
            tvBloodGulocose.setText("0");
        }
    }

    private void dumpBloodOxygenDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double bloodOxygen = dp.getValue(field).asFloat();
                    tvBloodOxygen.setText(String.format("%.0f", bloodOxygen));
                    break;
                }
            }
        } else {
            tvBloodOxygen.setText("0");
        }
    }

    private void dumpBodyTemperatureDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double bodyTemperature = dp.getValue(field).asFloat();
                    tvBodyTemperature.setText(String.format("%.1f", bodyTemperature));
                    break;
                }
            }
        } else {
            tvBodyTemperature.setText("0.0");
        }
    }

    @Override
    protected Runnable onBackPressedAction() {
        return null;
    }
}
