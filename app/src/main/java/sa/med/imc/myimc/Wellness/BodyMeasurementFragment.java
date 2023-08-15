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
import sa.med.imc.myimc.Adapter.DatesAdapter;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.FitnessAndWellness.value.AddHealthValueSheet;
import sa.med.imc.myimc.FitnessAndWellness.value.model.HealthValueModel;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Listeners.DateSelectionListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.models.Date;

import static android.app.Activity.RESULT_OK;


public class BodyMeasurementFragment extends Fragment implements DateSelectionListener {

    private final String TAG = this.getClass().getName();
    private final BroadcastReceiver receiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.REFRESH)){
                getHeightHistory(startTime,endTime);
                getWeightHistory(startTime,endTime);
            }
        }
    };
    private long startTime;
    private long endTime;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_height_value)
    TextView tvHeight;
    @BindView(R.id.tv_weight_value)
    TextView tvWeight;
    @BindView(R.id.tv_bmi_value)
    TextView tvBMI;
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
            .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE)
            .addDataType(HealthDataTypes.TYPE_BLOOD_GLUCOSE)
            .addDataType(HealthDataTypes.TYPE_OXYGEN_SATURATION)
            .addDataType(HealthDataTypes.TYPE_BODY_TEMPERATURE)
            .addDataType(DataType.TYPE_SLEEP_SEGMENT).build();

    private Boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    FragmentListener fragmentAdd;
    private OnDataPointListener onDataPointListener;

    public BodyMeasurementFragment() {
        // Required empty public constructor
    }

    public static BodyMeasurementFragment newInstance() {
        BodyMeasurementFragment fragment = new BodyMeasurementFragment();
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
        View view = inflater.inflate(R.layout.body_measurement, container, false);
        ButterKnife.bind(this, view);

        receiveArguments();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver,new IntentFilter(Constants.REFRESH));
        if (getView()!=null)
        getView().findViewById(R.id.addHeightWeight).setOnClickListener(v->{
            ArrayList<HealthValueModel> list = new ArrayList<>();
            HealthValueModel height =new HealthValueModel("Height",Field.FIELD_HEIGHT,DataType.TYPE_HEIGHT,"meter","",5,"1.90");
            HealthValueModel weight =new HealthValueModel("Weight",Field.FIELD_WEIGHT,DataType.TYPE_WEIGHT,"Kg","");
            list.add(height);
            list.add(weight);
            AddHealthValueSheet.newInstance(list,startTime,endTime).showNow(getChildFragmentManager(),"");
        });
        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        readData();
    }

    private void receiveArguments() {

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

        getHeightHistory(startTime,endTime);
        getWeightHistory(startTime, endTime);
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

        getWeightHistory(startTime, endTime);
        getHeightHistory(startTime,endTime);
    }

    private void getHeightHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_HEIGHT)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpHeightDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void getWeightHistory(long startTime, long endTime) {
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_WEIGHT)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(requireContext(), GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions))
                .readData(readRequest)
                .addOnSuccessListener (response -> {
                    for (Bucket bucket : response.getBuckets()) {
                        for (DataSet dataSet : bucket.getDataSets()) {
                            dumpWeightDataSet(dataSet);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was an error reading data from Google Fit", e));
    }

    private void dumpHeightDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double heightInMeters = dp.getValue(Field.FIELD_MAX).asFloat();
                    tvHeight.setText(String.format("%.1f", heightInMeters * 100));
                    updateBMI();
                }
            }
        }
        else {
            tvHeight.setText("0");
            updateBMI();
        }
    }

    private void dumpWeightDataSet(DataSet dataSet) {
        if (dataSet.getDataPoints().size() > 0) {
            for (DataPoint dp : dataSet.getDataPoints()) {
                for (Field field : dp.getDataType().getFields()) {
                    double weight = dp.getValue(Field.FIELD_MAX).asFloat();
                    tvWeight.setText(String.format("%.1f", weight));
                    updateBMI();
                }
            }
        } else {
            tvWeight.setText("0");
            updateBMI();
        }
    }

    private void updateBMI() {
        try {
            float height = Float.parseFloat(tvHeight.getText().toString());
            float weight = Float.parseFloat(tvWeight.getText().toString());

            if (height > 0 && weight > 0) {
                // Converting height from centimeters to meters.
                height = height / 100;
                double bmi = weight / (height * height);

                if (bmi > 0) {
                    tvBMI.setText(String.format("%.1f", bmi));
                } else {
                    tvBMI.setText("0.0");
                }
            } else {
                tvBMI.setText("0.0");
            }
        } catch (Exception e) {

        }
    }
}
