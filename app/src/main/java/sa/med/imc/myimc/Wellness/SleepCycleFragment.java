package sa.med.imc.myimc.Wellness;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.tasks.OnFailureListener;

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
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Listeners.DateSelectionListener;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.models.Date;


public class SleepCycleFragment extends Fragment implements DateSelectionListener {

    private final String TAG = this.getClass().getName();

    @BindView(R.id.iv_back)
    ImageView ivBack;
    FragmentListener fragmentAdd;
//    @BindView(R.id.cardAssessment)
//    CardView cardAssessment;
    @BindView(R.id.rv_dates)
    RecyclerView rvDates;
    @BindView(R.id.sleep_chart)
    LineChart sleepChart;
    @BindView(R.id.today)
    TextView tvToday;

    List<String> SLEEP_STAGE_NAMES = new ArrayList<>();
    List<String> hours = new ArrayList<>();
    ArrayList<Entry> sleepEntries = new ArrayList<>();
    LineData sleepData;
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

    public SleepCycleFragment() {
        // Required empty public constructor
    }

    public static SleepCycleFragment newInstance() {
        SleepCycleFragment fragment = new SleepCycleFragment();
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
        View view = inflater.inflate(R.layout.fragment_sleep_cycle, container, false);
        ButterKnife.bind(this, view);

        receiveArguments();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setupChart();
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPermissionsAndRun(GoogleFitActions.READ_SLEEP_SEGMENT);
    }

    private void init() {
        setupCalendar();
        rvDates.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(new DatesAdapter(requireContext(), dates, currentDateNum, this::onSelected));
        rvDates.post(() -> rvDates.smoothScrollToPosition(currentDateNum + 2));

        SLEEP_STAGE_NAMES.add(0, "Unused");
        SLEEP_STAGE_NAMES.add(1, "Awake (during sleep)");
        SLEEP_STAGE_NAMES.add(2, "Sleep");
        SLEEP_STAGE_NAMES.add(3, "Out-of-bed");
        SLEEP_STAGE_NAMES.add(4, "Light sleep");
        SLEEP_STAGE_NAMES.add(5, "Deep sleep");
        SLEEP_STAGE_NAMES.add(6, "REM sleep");
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
            case READ_SLEEP_SEGMENT:
                readSleepData();
                break;
        }
    }

    private void receiveArguments() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    private void readSleepData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int numOfHours = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);

        hours.clear();
        sleepEntries.clear();
        for (int hour = 1; hour <= numOfHours; hour++) {
            long startTime = calendar.getTimeInMillis();
            calendar.add(Calendar.HOUR_OF_DAY, +1);
            SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
            hours.add(hourFormat.format(calendar.getTime()));
            long endTime = calendar.getTimeInMillis();
            getSleepData(startTime, endTime, hour);
        }

        setupChart();
    }

    private void getSleepData(long startTime, long endTime, int hour) {
        SessionReadRequest request = new SessionReadRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .read(DataType.TYPE_SLEEP_SEGMENT)
                .readSessionsFromAllApps()
                .includeSleepSessions()
                .enableServerQueries()
                .build();

        Fitness.getSessionsClient(requireContext(), getGoogleAccount()).readSession(request)
                .addOnSuccessListener(sessionReadResponse -> {
                    if (sessionReadResponse.getSessions().isEmpty()) {
                        sleepEntries.add(new Entry(hour, 0));
                        sleepData.notifyDataChanged();
                        sleepChart.notifyDataSetChanged();
                        sleepChart.invalidate();
                    } else {
                        for (Session session : sessionReadResponse.getSessions()) {
                            List<DataSet> dataSets = sessionReadResponse.getDataSet(session);
                            for (DataSet dataSet : dataSets) {
                                if (dataSet.getDataPoints().isEmpty()) {
//                                    sleepEntries.add(new Entry(hour, 0));
//                                    sleepData.notifyDataChanged();
//                                    sleepChart.notifyDataSetChanged();
//                                    sleepChart.invalidate();
                                } else {
                                    for (DataPoint dataPoint : dataSet.getDataPoints()) {
                                        int sleepStageVal = dataPoint.getValue(Field.FIELD_SLEEP_SEGMENT_TYPE).asInt();
                                        if (sleepStageVal > 0) {
                                            try {
                                                Log.d("SARIM", dataPoint.getStartTime(TimeUnit.MILLISECONDS) + " | Start");
                                                Log.d("SARIM", dataPoint.getEndTime(TimeUnit.MILLISECONDS) + " | End");

                                                String sleepStage = SLEEP_STAGE_NAMES.get(sleepStageVal);
                                                Log.d("SARIM", sleepStage);
                                                sleepEntries.add(new Entry(hour, sleepStageVal - 1));
                                                sleepData.notifyDataChanged();
                                                sleepChart.notifyDataSetChanged();
                                                sleepChart.invalidate();
                                                break;
                                            } catch (Exception e) {

                                            }
                                        } else {
//                                            sleepEntries.add(new Entry(hour, 0));
//                                            sleepData.notifyDataChanged();
//                                            sleepChart.notifyDataSetChanged();
//                                            sleepChart.invalidate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
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

        java.util.Date currentDay = calendar.getTime();

        if (currentDay == currentDate) {
            tvToday.setText("Today");
        } else {
            SimpleDateFormat todayFormat = new SimpleDateFormat("EEE, MMM d");
            String todayDate = todayFormat.format(calendar.getTime());
            tvToday.setText(todayDate);
        }

        int numOfHours = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);

        hours.clear();
        sleepEntries.clear();
        for (int hour = 1; hour <= numOfHours; hour++) {
            long startTime = calendar.getTimeInMillis();
            calendar.add(Calendar.HOUR_OF_DAY, +1);
            SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
            hours.add(hourFormat.format(calendar.getTime()));
            long endTime = calendar.getTimeInMillis();
            getSleepData(startTime, endTime, hour);
        }
    }

    private void setupChart() {
        for (int i = 1; i <= 24; i++) {
            sleepEntries.add(new Entry(i, 0));
        }

        LineDataSet sleepDataSet = new LineDataSet(sleepEntries, "");
        sleepDataSet.setDrawFilled(true);
        sleepDataSet.setFillDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_sleep_chart));
        sleepDataSet.setLineWidth(3f);
        sleepDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.color_ed_green));
        sleepDataSet.setValueTextSize(0f);
        sleepDataSet.setDrawCircles(false);

        XAxis xAxis = sleepChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        xAxis.setAxisMaximum(24);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(hours));

        YAxis yAxisRight = sleepChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = sleepChart.getAxisLeft();
        yAxisLeft.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        yAxisLeft.setTextSize(14f);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMaximum(6);

        ArrayList<String> sleepStages = new ArrayList<>();
        sleepStages.add(getResources().getString(R.string.awake));
        sleepStages.add(getResources().getString(R.string.sleep));
        sleepStages.add(getResources().getString(R.string.out_of_bed));
        sleepStages.add(getResources().getString(R.string.light));
        sleepStages.add(getResources().getString(R.string.deep));
        sleepStages.add(getResources().getString(R.string.rem));

        yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(sleepStages));

        sleepChart.setVisibleXRangeMaximum(12);
        sleepChart.getDescription().setEnabled(false);
        sleepChart.getLegend().setEnabled(false);
        sleepChart.setScaleEnabled(false);

        sleepData = new LineData(sleepDataSet);
        sleepChart.setData(sleepData);

        sleepChart.invalidate();

        sleepEntries.clear();
    }
}
