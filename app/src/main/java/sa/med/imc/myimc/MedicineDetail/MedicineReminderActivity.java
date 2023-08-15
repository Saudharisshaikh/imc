package sa.med.imc.myimc.MedicineDetail;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.SimpleRecyclerViewAdapter;
import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.MedicineDetail.model.MedicineReminderModel;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

public class MedicineReminderActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.et_medicine_name)
    EditText etMedicineName;
    @BindView(R.id.switchOngoingMed)
    Switch switchOngoingMed;
    @BindView(R.id.et_med_state_date)
    TextView etMedStateDate;
    @BindView(R.id.et_time_1)
    TextView etTime1;
    @BindView(R.id.lay_time_one)
    LinearLayout layTimeOne;
    @BindView(R.id.et_time_2)
    TextView etTime2;
    @BindView(R.id.lay_time_two)
    LinearLayout layTimeTwo;
    @BindView(R.id.et_time_3)
    TextView etTime3;
    @BindView(R.id.lay_time_three)
    LinearLayout layTimeThree;
    @BindView(R.id.et_provider_name)
    EditText etProviderName;
    @BindView(R.id.switchReminder)
    Switch switchReminder;
    @BindView(R.id.et_reminder_end_date)
    TextView etReminderEndDate;
    @BindView(R.id.lay_btn_save)
    LinearLayout layBtnSave;

    PendingIntent pendingIntent;
    MedicationRespone.MedicationModel data;
    ImcDatabase db;
//    WorkManager mWorkManager;
//    PeriodicWorkRequest saveRequest;
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    @BindView(R.id.select_schedule)
    TextView selectSchedule;
    @BindView(R.id.lay_times)
    LinearLayout layTimes;
    @BindView(R.id.check_mon)
    CheckBox checkMon;
    @BindView(R.id.check_tue)
    CheckBox checkTue;
    @BindView(R.id.check_wed)
    CheckBox checkWed;
    @BindView(R.id.check_thu)
    CheckBox checkThu;
    @BindView(R.id.check_fri)
    CheckBox checkFri;
    @BindView(R.id.check_sat)
    CheckBox checkSat;
    @BindView(R.id.check_sun)
    CheckBox checkSun;
    @BindView(R.id.lay_week_days)
    LinearLayout layWeekDays;
    @BindView(R.id.et_no_hours)
    EditText etNoHours;
    @BindView(R.id.lay_hours)
    LinearLayout layHours;
    @BindView(R.id.et_time_4)
    TextView etTime4;
    @BindView(R.id.lay_time_four)
    LinearLayout layTimeFour;
    @BindView(R.id.et_descp)
    TextView etDescp;


    @Override
    protected Context getActivityContext() {
        return this;
    }

    public static void startActivity(Activity activity, MedicationRespone.MedicationModel data) {
        Intent intent = new Intent(activity, MedicineReminderActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MedicineReminderActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (SharedPreferencesUtils.getInstance(MedicineReminderActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        ButterKnife.bind(this);
        db = Room.databaseBuilder(this, ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
//        Test();
        //medication data through intent
        data = (MedicationRespone.MedicationModel) getIntent().getSerializableExtra("data");
        MedicineReminderModel medicineReminderModel = db.medicationReminderDataDao().getSelectMedReminder(String.valueOf(data.getDispendingId()));

        setData(medicineReminderModel);
//
//        mWorkManager = WorkManager.getInstance();
//        saveRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES, 15, TimeUnit.MINUTES)
//                .addTag("first")
//                .build();
//
//        mWorkManager.getWorkInfoByIdLiveData(saveRequest.getId()).
//                observe(this, workInfo -> {
//                    if (workInfo != null) {
//                        WorkInfo.State state = workInfo.getState();
//                        Log.e("main saveRequest  ", state.toString() + "\n");
//                    }
//                });
    }

    @OnClick({R.id.select_schedule, R.id.iv_back, R.id.lay_btn_save, R.id.et_time_1, R.id.et_time_2, R.id.et_time_3, R.id.et_time_4, R.id.et_med_state_date, R.id.et_reminder_end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.select_schedule:
                showListPopUp(selectSchedule, selectSchedule.getText().toString());
                break;

            case R.id.iv_back:
                //Stop alarm manager
//                stopAlarmManager();
                onBackPressed();
                break;

            case R.id.lay_btn_save:
                if (isValid())
                    saveDataInDBTriggerScheduler();
                break;

            case R.id.et_time_1:
                Common.showHourPicker(etTime1, "", this);
                break;

            case R.id.et_time_2:
                Common.showHourPicker(etTime2, etTime1.getText().toString(), this);
                break;

            case R.id.et_time_3:
                Common.showHourPicker(etTime3, etTime2.getText().toString(), this);
                break;

            case R.id.et_time_4:
                Common.showHourPicker(etTime4, etTime3.getText().toString(), this);
                break;

            case R.id.et_med_state_date:
//                Common.getDateStartPickerCU(etMedStateDate, this);
                Common.getDateStartPickerCU(etMedStateDate, this, etReminderEndDate, data.getNoOfDays());

                break;

            case R.id.et_reminder_end_date:
                Common.getDateToPickerCU(etReminderEndDate, this, etMedStateDate.getText().toString());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        MedicineReminderActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    boolean isValid() {

        boolean valid = true;
        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.select))) {
            makeToast(getString(R.string.select_time_schedule));
        }

        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.hourly))) {
            if (etNoHours.getText().toString().length() == 0) {
                etNoHours.setError(getString(R.string.required));
                valid = false;
            }

            if (etNoHours.getText().toString().length() > 0) {
                if (Integer.parseInt(etNoHours.getText().toString()) <= 0) {
                    etNoHours.setError(getString(R.string.invalid));
                    valid = false;
                }
            }
        }

        if (etReminderEndDate.getText().toString().length() == 0) {
            makeToast(getString(R.string.select_end_date));
            valid = false;
        }

        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.daily))) {
            if (etTime1.getText().toString().length() == 0 &&
                    etTime2.getText().toString().length() == 0 &&
                    etTime3.getText().toString().length() == 0 &&
                    etTime4.getText().toString().length() == 0) {
                makeToast(getString(R.string.select_at_least_one_time));
                valid = false;
            }
        }

        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.weekly))) {
            if (getCount(checkSun.isChecked(), checkMon.isChecked(), checkTue.isChecked(),
                    checkWed.isChecked(), checkThu.isChecked(), checkFri.isChecked(),
                    checkSat.isChecked()) == 0) {
                makeToast(getString(R.string.select_at_least_one_day));
                valid = false;
            }
        }

        return valid;
    }

    void alarmSetup() {
        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MedicineReminderActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MedicineReminderActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
    }

    //Trigger alarm manager with entered time interval
    public void triggerAlarmManager() {

        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();

        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.MINUTE, 1);
        cal.add(Calendar.HOUR_OF_DAY, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime(),
                60 * 1000,
                pendingIntent);
    }

    // Pop up list to select schedule
    public void showListPopUp(TextView view, String lastValue) {
        List<String> list = new ArrayList<>();

        list.add(getString(R.string.once));
        list.add(getString(R.string.hourly));
        list.add(getString(R.string.daily));
        list.add(getString(R.string.weekly));

        view.setEnabled(false);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.dialog_custom_list, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                view.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        RecyclerView recycler_list = popupView.findViewById(R.id.recycler_list);

        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(this, list, lastValue);
        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_list.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            view.setEnabled(true);
            view.setText(list.get(position));

            if (list.get(position).equalsIgnoreCase(getString(R.string.once))) {
                layTimes.setVisibility(View.VISIBLE);

                layTimeTwo.setVisibility(View.GONE);
                layTimeThree.setVisibility(View.GONE);
                layTimeFour.setVisibility(View.GONE);

                layWeekDays.setVisibility(View.GONE);
                layHours.setVisibility(View.GONE);
            }

            if (list.get(position).equalsIgnoreCase(getString(R.string.hourly))) {
                layTimes.setVisibility(View.GONE);
                layWeekDays.setVisibility(View.GONE);
                layHours.setVisibility(View.VISIBLE);
            }

            if (list.get(position).equalsIgnoreCase(getString(R.string.daily))) {
                layTimes.setVisibility(View.VISIBLE);
                layWeekDays.setVisibility(View.GONE);
                layHours.setVisibility(View.GONE);

                layTimeOne.setVisibility(View.VISIBLE);
                layTimeTwo.setVisibility(View.VISIBLE);
                layTimeThree.setVisibility(View.VISIBLE);
                layTimeFour.setVisibility(View.VISIBLE);
            }

            if (list.get(position).equalsIgnoreCase(getString(R.string.weekly))) {
                layTimes.setVisibility(View.GONE);
                layWeekDays.setVisibility(View.VISIBLE);
                layHours.setVisibility(View.GONE);
            }

            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> view.setEnabled(true));
        popupWindow.showAsDropDown(view, 2, 4);//5

    }

    //set data on opening page done DB if exist else use medication data
    void setData(MedicineReminderModel medicineReminderModel) {
        String freq = "";
        String weekValues = "";

        if (medicineReminderModel != null) {
            etMedicineName.setText(medicineReminderModel.getMedication_name());
            switchOngoingMed.setChecked(medicineReminderModel.isIs_on_going());
            switchReminder.setChecked(medicineReminderModel.isIs_reminder());

            etMedStateDate.setText(medicineReminderModel.getStart_date());
            etReminderEndDate.setText(medicineReminderModel.getEnd_date());

            etTime1.setText(medicineReminderModel.getDay_time_one());
            etTime2.setText(medicineReminderModel.getDay_time_two());
            etTime3.setText(medicineReminderModel.getDay_time_three());
            etTime4.setText(medicineReminderModel.getDay_time_four());

            if (medicineReminderModel.getFreq_desp() != null)
                etDescp.setText(medicineReminderModel.getFreq_desp());

            if (medicineReminderModel.getFreq() != null) {
                freq = medicineReminderModel.getFreq();
                if (freq.contains("WK")) {
                    weekValues = medicineReminderModel.getWeek_days();
                    getWeekDescription(freq);

                }
            }

            if (freq != null) {
                if (freq.length() > 0) {
                    // selectSchedule.setEnabled(false);
                } else {
                    selectSchedule.setText(getString(R.string.select));
                    //selectSchedule.setEnabled(true);
                }

            } else {
                selectSchedule.setText(getString(R.string.select));
                // selectSchedule.setEnabled(true);
            }


            if (weekValues.length() > 0) {
                if (weekValues.contains("mon"))
                    checkMon.setChecked(true);

                if (weekValues.contains("tue"))
                    checkTue.setChecked(true);

                if (weekValues.contains("wed"))
                    checkWed.setChecked(true);

                if (weekValues.contains("thu"))
                    checkThu.setChecked(true);

                if (weekValues.contains("fri"))
                    checkFri.setChecked(true);

                if (weekValues.contains("sat"))
                    checkSat.setChecked(true);

                if (weekValues.contains("sun"))
                    checkSun.setChecked(true);
            }
        } else {

            if (data.getFreqDescription() != null)
                etDescp.setText(data.getFreqDescription());


            if (data.getDescp() != null)
                etMedicineName.setText(data.getDescp());

            if (data.getInActiveMed().equalsIgnoreCase("1")) {
                switchOngoingMed.setChecked(false);
            } else {
                switchOngoingMed.setChecked(true);
            }

            if (data.getDeliveryDate() != null) {
                etMedStateDate.setText(Common.getConvertDate(data.getDeliveryDate()));
                if (!BuildConfig.DEBUG)
                    if (data.getNoOfDays() != null)
                        etReminderEndDate.setText(Common.getEndDate(etMedStateDate.getText().toString(), Integer.parseInt(data.getNoOfDays())));
            }

            if (data.getFreq() != null) {
                if (data.getFreq().length() > 0) {
                    freq = data.getFreq();
                    //selectSchedule.setEnabled(false);
                } else {
                    selectSchedule.setText(getString(R.string.select));
                    //selectSchedule.setEnabled(true);
                }

            } else {
                selectSchedule.setText(getString(R.string.select));
                //selectSchedule.setEnabled(true);
            }
            if (freq.contains("WK")) {
                layTimes.setVisibility(View.GONE);
                layWeekDays.setVisibility(View.VISIBLE);
                layHours.setVisibility(View.GONE);

                selectSchedule.setText(getString(R.string.weekly));
                setWeekDays(freq, weekValues);
            }

        }

        if (freq.contains("WK")) {
            layTimes.setVisibility(View.GONE);
            layWeekDays.setVisibility(View.VISIBLE);
            layHours.setVisibility(View.GONE);

            selectSchedule.setText(getString(R.string.weekly));
        }

        if (freq.contains("OD") || freq.contains("TID") || freq.contains("BID") || freq.contains("QID")) {
            layTimes.setVisibility(View.VISIBLE);
            layWeekDays.setVisibility(View.GONE);
            layHours.setVisibility(View.GONE);
            selectSchedule.setText(getString(R.string.daily));


            if (freq.contains("OD")) {
                layTimeTwo.setVisibility(View.GONE);
                layTimeThree.setVisibility(View.GONE);
                layTimeFour.setVisibility(View.GONE);
                etDescp.setText("Once Daily");
                selectSchedule.setText(getString(R.string.daily));
            }

            if (freq.contains("BID")) {
                layTimeThree.setVisibility(View.GONE);
                layTimeFour.setVisibility(View.GONE);
                etDescp.setText("Twice Daily");
                selectSchedule.setText(getString(R.string.daily));
            }

            if (freq.contains("TID")) {
                layTimeFour.setVisibility(View.GONE);
                etDescp.setText("Three Times Daily");
                selectSchedule.setText(getString(R.string.daily));

            }

            if (freq.contains("QID")) {
                etDescp.setText("Four Times Daily");
                selectSchedule.setText(getString(R.string.daily));

            }
        }

        if (freq.contains("Q") && freq.contains("H")) {
            layTimes.setVisibility(View.GONE);
            layWeekDays.setVisibility(View.GONE);
            layHours.setVisibility(View.VISIBLE);
            selectSchedule.setText(getString(R.string.hourly));

            String hour = freq.replace("Q", "");
            hour = hour.replace("H", "");
            etNoHours.setText(hour);
            if (hour.equalsIgnoreCase("1"))
                etDescp.setText("After " + hour + " Hour");
            else
                etDescp.setText("After " + hour + " Hours");

            selectSchedule.setText(getString(R.string.hourly));

        }

        if (freq.contains("ONCE")) {
            selectSchedule.setText(getString(R.string.once));
            etDescp.setText("Only Once");
            layTimes.setVisibility(View.VISIBLE);
            layTimeTwo.setVisibility(View.GONE);
            layTimeThree.setVisibility(View.GONE);
            layTimeFour.setVisibility(View.GONE);
            layWeekDays.setVisibility(View.GONE);
            layHours.setVisibility(View.GONE);
        }

    }

    // set weekdays
    void setWeekDays(String value, String days) {
        switch (value) {
            case "1/WK":
                checkMon.setChecked(true);
                etDescp.setText("Once Weekly");
                break;

            case "2/WK":
                checkMon.setChecked(true);
                checkWed.setChecked(true);
                etDescp.setText("Twice Weekly");

                break;

            case "3/WK":
                checkMon.setChecked(true);
                checkWed.setChecked(true);
                checkSat.setChecked(true);
                etDescp.setText("Three Times Weekly");

                break;

            case "4/WK":
                checkMon.setChecked(true);
                checkWed.setChecked(true);
                checkFri.setChecked(true);
                checkSun.setChecked(true);
                etDescp.setText("Four Times Weekly");

                break;

            case "5/WK":
                checkMon.setChecked(true);
                checkWed.setChecked(true);
                checkFri.setChecked(true);
                checkSat.setChecked(true);
                checkSun.setChecked(true);
                etDescp.setText("Five Times Weekly");

                break;

            case "6/WK":
                checkMon.setChecked(true);
                checkWed.setChecked(true);
                checkThu.setChecked(true);
                checkFri.setChecked(true);
                checkSat.setChecked(true);
                checkSun.setChecked(true);
                etDescp.setText("Six Times Weekly");

                break;

            case "7/WK":
                checkMon.setChecked(true);
                checkTue.setChecked(true);
                checkWed.setChecked(true);
                checkThu.setChecked(true);
                checkFri.setChecked(true);
                checkSat.setChecked(true);
                checkSun.setChecked(true);
                etDescp.setText("Seven Times Weekly");

                break;
        }
    }


    void getWeekDescription(String value) {
        switch (value) {
            case "1/WK":
                etDescp.setText("Once Weekly");
                break;

            case "2/WK":
                etDescp.setText("Twice Weekly");

                break;

            case "3/WK":
                etDescp.setText("Three Times Weekly");

                break;

            case "4/WK":
                etDescp.setText("Four Times Weekly");

                break;

            case "5/WK":
                etDescp.setText("Five Times Weekly");

                break;

            case "6/WK":
                etDescp.setText("Six Times Weekly");

                break;

            case "7/WK":
                etDescp.setText("Seven Times Weekly");

                break;
        }
    }

    // create frequency if data not received in API.
    String getFreq() {
        String freq = null;
        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.once))) {
            freq = "ONCE";
        }

        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.hourly))) {
            freq = "Q" + etNoHours.getText().toString() + "H";
        }

        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.daily))) {

            if (etTime1.getText().toString().length() > 0 &&
                    etTime2.getText().toString().length() > 0 &&
                    etTime3.getText().toString().length() > 0 &&
                    etTime4.getText().toString().length() > 0) {
                freq = "QID";
            } else if (etTime1.getText().toString().length() > 0 &&
                    etTime2.getText().toString().length() > 0 &&
                    etTime3.getText().toString().length() > 0) {
                freq = "TID";
            } else if (etTime1.getText().toString().length() > 0 &&
                    etTime2.getText().toString().length() > 0) {
                freq = "BID";
            } else if (etTime1.getText().toString().length() > 0) {
                freq = "OD";
            }
        }

        if (selectSchedule.getText().toString().equalsIgnoreCase(getString(R.string.weekly))) {

            int times = getCount(checkSun.isChecked(),
                    checkMon.isChecked(), checkTue.isChecked(),
                    checkWed.isChecked(), checkThu.isChecked(),
                    checkFri.isChecked(), checkSat.isChecked());
            freq = times + "/WK";

        }
        Log.e("freq----", "" + freq);
        return freq;

    }

    // get week days count to send reminder
    int getCount(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat) {
        int count = 0;
        if (sun)
            count = count + 1;

        if (mon)
            count = count + 1;

        if (tue)
            count = count + 1;

        if (wed)
            count = count + 1;

        if (thu)
            count = count + 1;

        if (fri)
            count = count + 1;

        if (sat)
            count = count + 1;

        return count;

    }

    // save data in database and trigger work manager and alarm on basis of device version
    void saveDataInDBTriggerScheduler() {
        MedicineReminderModel medicineReminderModel = new MedicineReminderModel();
        medicineReminderModel.setMed_dispense_id(String.valueOf(data.getDispendingId()));
        medicineReminderModel.setMedication_name(data.getDescp());

        medicineReminderModel.setStart_date(etMedStateDate.getText().toString());
        medicineReminderModel.setEnd_date(etReminderEndDate.getText().toString());

        medicineReminderModel.setIs_on_going(switchOngoingMed.isChecked());
        medicineReminderModel.setIs_reminder(switchReminder.isChecked());

        medicineReminderModel.setDay_time_one(etTime1.getText().toString());
        medicineReminderModel.setDay_time_two(etTime2.getText().toString());
        medicineReminderModel.setDay_time_three(etTime3.getText().toString());
        medicineReminderModel.setDay_time_four(etTime4.getText().toString());
        medicineReminderModel.setLast_sent_date(Common.getCurrentDateWithLastUpdate());
        medicineReminderModel.setLast_sent_time("00:00");
        medicineReminderModel.setFreq_desp(etDescp.getText().toString());

        if (getFreq() != null) {
            if (getFreq().length() > 0) {
                medicineReminderModel.setFreq(getFreq());
                // selectSchedule.setEnabled(false);
            } else {
                medicineReminderModel.setFreq(getFreq());
                selectSchedule.setText(getString(R.string.select));
                // selectSchedule.setEnabled(true);
            }

        } else {
            medicineReminderModel.setFreq(getFreq());
            selectSchedule.setText(getString(R.string.select));
            //selectSchedule.setEnabled(true);
        }


        medicineReminderModel.setSchedule_time(selectSchedule.getText().toString());
        if (etNoHours.getText().toString().length() > 0) {
            medicineReminderModel.setHours(Integer.parseInt(etNoHours.getText().toString()));
        }

        String week_days = "";

        if (checkMon.isChecked())
            week_days = "mon,";

        if (checkTue.isChecked())
            week_days = week_days + "tue,";

        if (checkWed.isChecked())
            week_days = week_days + "wed,";

        if (checkThu.isChecked())
            week_days = week_days + "thu,";

        if (checkFri.isChecked())
            week_days = week_days + "fri,";

        if (checkSat.isChecked())
            week_days = week_days + "sat,";

        if (checkSun.isChecked())
            week_days = week_days + "sun,";

        medicineReminderModel.setWeek_days(week_days);
        medicineReminderModel.setPatient_name(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_USER).getFirstName());

        db.medicationReminderDataDao().saveMedReminderData(medicineReminderModel);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //trigger worker for latest android versions
//            mWorkManager.enqueueUniquePeriodicWork("first", ExistingPeriodicWorkPolicy.KEEP, saveRequest);
//        } else {
        //trigger alarm manager
        alarmSetup();
        triggerAlarmManager();
//        }
        makeToast(getString(R.string.reminder_saved));
    }
}
