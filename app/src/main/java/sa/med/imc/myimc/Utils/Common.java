package sa.med.imc.myimc.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import kotlin.jvm.internal.Intrinsics;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Home.HomeFragment;
import sa.med.imc.myimc.MainActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Telr.TelrActivity;
import sa.med.imc.myimc.custom_dailog.CustomDailogBuilder;
import sa.med.imc.myimc.globle.interfaces.SelectDate;


public class Common {
    public static String CONTAINER_FRAGMENT = "BaseFragment";

    public static int INTERVAL = 0;
    private static int screenHeight = 0;
    private static final long ONE_MINUTE_IN_MILLIS = 60000;
    public static final int TIME_OUT_DURATION = 60000;
    public static final String INTENT_REPORT_URI = "intent_report_uri";

    public static void openFile(Activity activity, Uri uri) {
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfOpenintent.setDataAndType(uri, "application/pdf");
        try {
            activity.startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static enum ReportTypes {
        MEDICAL,
        LAB,
        SICK,
        SMART,
        PRESCRIPTION,
        SLIP
    }

    @SuppressLint("StaticFieldLeak")
    public static CustomDailogBuilder progressDialog;

    public static void showDialog(Context context) {
        progressDialog = new CustomDailogBuilder(context);
        progressDialog.show();
    }

    public static void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void messageDailog(Activity activity, String content) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    public static void messageDailog(Activity activity, String content, View.OnClickListener onClickListener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        buttonOk.setOnClickListener(v -> {
            onClickListener.onClick(dialogView);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }


    public static void messageDailogForCompleteActivity(Activity activity, String content) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.simple_alert, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        TextView contentTextView = dialogView.findViewById(R.id.content);
        TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
        contentTextView.setText(content);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            activity.finish();

        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static String getFromDate(int addMonth, int minusYear) throws ParseException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = 01;

        int newMonth = (month + addMonth);
        if (newMonth > 12) {
            int newYear = newMonth / 12;
            year = (year + newYear);
            month = (newMonth - (newYear * 12));
        } else {
            month = newMonth;
        }


        DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date = originalFormat.parse(day + "-" + month + "-" + (year - minusYear));
        String formattedDate = targetFormat.format(date);

        return formattedDate;
    }


    public static void setBDDate(final TextView txt, Activity activity) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

//        String start_date_o = (mYear - 1) + "-" + (12) + "-" + (31);
//        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date newDate2 = new Date();

        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.MyAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                txt.setText(parseDob(date));
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(newDate2.getTime());
        datePickerDialog.show();
    }

    public static void makeToast(Activity activity, String msg) {

        if (msg != null && activity != null)
            if (msg.toLowerCase().contains("Session expired") || msg.toLowerCase().contains("لقد تم إنهاء الجلسة أو تم تسجيل الدخول من جهاز مختلف.")) {
                //msg = activity.getString(R.string.session_expired);
                sessionExpired(activity);
            } else {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
    }

    //getting device screen width.
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //  int height = size.y;
        return (int) size.x;
    }

    //getting device screen height.
    public static int getScreenWidt(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            screenHeight = size.x - 100;
        }
        return screenHeight;
    }

    //getting device screen height.
    public static int getScreenFullHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            screenHeight = size.y - 100;
        }
        return screenHeight;
    }

    public static String getHourAheadTime(String fist, int hours) {
        DateFormat dfMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat hour1 = new SimpleDateFormat("HH", Locale.ENGLISH);
        DateFormat minu = new SimpleDateFormat("mm", Locale.ENGLISH);
        String time = "";
        Date newDateMin = null;
        try {
            newDateMin = dfMin.parse(fist);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (newDateMin != null) {
            int hourOfDay = Integer.parseInt(hour1.format(newDateMin)) + hours;
            int minute = Integer.parseInt(minu.format(newDateMin));
            String format;

            if (hourOfDay == 0) {
                format = "AM";
            } else if (hourOfDay == 12) {
                format = "PM";
            } else if (hourOfDay > 12) {
                hourOfDay = hourOfDay - 12;
                format = "PM";
            } else {
                format = "AM";
            }

            if (hourOfDay < 10) {
                if (minute < 10) {
                    time = "0" + hourOfDay + ":0" + minute + " " + format;
                } else
                    time = "0" + hourOfDay + ":" + minute + " " + format;
            } else {
                if (minute < 10) {
                    time = hourOfDay + ":0" + minute + " " + format;

                } else
                    time = hourOfDay + ":" + minute + " " + format;
            }
        }
        return time;
    }

    public static String getOneMinuteAheadTimeH(String fist) {
        DateFormat dfMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat hour1 = new SimpleDateFormat("HH", Locale.ENGLISH);
        DateFormat minu = new SimpleDateFormat("mm", Locale.ENGLISH);
        String time = "";
        Date newDateMin = null;
        try {
            newDateMin = dfMin.parse(fist);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (newDateMin != null) {
            int hourOfDay = Integer.parseInt(hour1.format(newDateMin));
            int minute = Integer.parseInt(minu.format(newDateMin)) + 1;
            String format;

            if (hourOfDay == 0) {
                format = "AM";
            } else if (hourOfDay == 12) {
                format = "PM";
            } else if (hourOfDay > 12) {
                format = "PM";
                hourOfDay = hourOfDay - 12;
            } else {
                format = "AM";
            }

            if (hourOfDay < 10) {
                if (minute < 10) {
                    time = "0" + hourOfDay + ":0" + minute + " " + format;
                } else
                    time = "0" + hourOfDay + ":" + minute + " " + format;
            } else {
                if (minute < 10) {
                    time = hourOfDay + ":0" + minute + " " + format;

                } else
                    time = hourOfDay + ":" + minute + " " + format;
            }
        }
        return time;
    }

    public static String getOneMinuteAheadTime(String fist) {
        DateFormat dfMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat hour1 = new SimpleDateFormat("HH", Locale.ENGLISH);
        DateFormat minu = new SimpleDateFormat("mm", Locale.ENGLISH);
        String time = "";
        Date newDateMin = null;
        try {
            newDateMin = dfMin.parse(fist);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (newDateMin != null) {
            int hourOfDay = Integer.parseInt(hour1.format(newDateMin));
            int minute = Integer.parseInt(minu.format(newDateMin)) + 1;

            if (hourOfDay < 10) {
                if (minute < 10) {
                    time = "0" + hourOfDay + ":0" + minute;
                } else
                    time = "0" + hourOfDay + ":" + minute;
            } else {
                if (minute < 10) {
                    time = hourOfDay + ":0" + minute;

                } else
                    time = hourOfDay + ":" + minute;
            }
        }
        return time;
    }

    public static void showHourPicker(TextView tview, String fist, Context context) {
        final Calendar myCalender = Calendar.getInstance();

        DateFormat dfMin = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        DateFormat hour1 = new SimpleDateFormat("hh", Locale.ENGLISH);
        DateFormat minu = new SimpleDateFormat("mm", Locale.ENGLISH);

        Date newDateMin = null;
        try {
            newDateMin = dfMin.parse(fist);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (newDateMin != null) {
            myCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour1.format(newDateMin)));
            myCalender.set(Calendar.MINUTE, Integer.parseInt(minu.format(newDateMin)) + 15);
        }

        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    String format;

                    if (hourOfDay == 0) {
                        hourOfDay += 12;
                        format = "AM";
                    } else if (hourOfDay == 12) {
                        format = "PM";
                    } else if (hourOfDay > 12) {
                        hourOfDay -= 12;
                        format = "PM";
                    } else {
                        format = "AM";
                    }

                    if (hourOfDay < 10) {
                        if (minute < 10) {
                            tview.setText("0" + hourOfDay + ":0" + minute + " " + format);
                        } else
                            tview.setText("0" + hourOfDay + ":" + minute + " " + format);
                    } else {
                        if (minute < 10) {
                            tview.setText(hourOfDay + ":0" + minute + " " + format);

                        } else
                            tview.setText(hourOfDay + ":" + minute + " " + format);
                    }
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose Time:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }


    public static String toAMPM(String s) {
        String formattedTime = s;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime time = LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"));
            String temp = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
            formattedTime = temp;
        }
        return formattedTime;
    }

    //    Checking for all possible internet providers
    public static boolean isNetworkAvail(Context context) {
        boolean flag = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = null;
            if (connectivityManager != null) {
                info = connectivityManager.getActiveNetworkInfo();
            }

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  System.out.println(info.getTypeName());
                flag = true;
            }
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //    System.out.println(info.getTypeName());
                flag = true;
            }
        } catch (Exception exception) {
//           Toast.makeText(context,"Error is "+exception,Toast.LENGTH_LONG).show();
//                 System.out.println("Exception at network connection....."
//                         + exception);
        }

        return flag;
    }

    //    Hide keyboard if opened.
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View view = activity.getCurrentFocus();
            IBinder iBinder = null;
            if (view != null) {
                iBinder = view.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
            }

        }
    }

    public static String convertDateForReport(String s) {
        String date;
        String time;
        try {
            date = s.substring(0, 10);
            time = s.substring(11, 16);

            Log.e("abcd", date + " " + time);

        } catch (Exception e) {
            return s;
        }

        return date + " " + toAMPM(time);

    }


    public static void showKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    //    Conversion dp to px.
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    // get From date picker with max date validation
    public static void getDateFromPicker(final TextView view, Context activity) {
        final String[] date = new String[1];

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String maxDate = mYear + "-" + (mMonth + 1) + "-" + (mDay - 1);
        DateFormat dfMax = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date newDateMax = null;
        try {
            newDateMax = dfMax.parse(maxDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (view1, year, monthOfYear, dayOfMonth) -> {
            monthOfYear = monthOfYear + 1;

            if (dayOfMonth < 10) {
                if (monthOfYear < 10) {
                    date[0] = "0" + dayOfMonth + "-0" + monthOfYear + "-" + year;
                } else {
                    date[0] = "0" + dayOfMonth + "-" + monthOfYear + "-" + year;
                }
            } else {
                if (monthOfYear < 10) {
                    date[0] = dayOfMonth + "-0" + monthOfYear + "-" + year;
                } else {
                    date[0] = dayOfMonth + "-" + monthOfYear + "-" + year;
                }
            }

            view.setText(getMMMDate(date[0]));
        }, mYear, mMonth, mDay);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMaxDate(newDateMax != null ? newDateMax.getTime() : 0);
        datePickerDialog.show();

    }

    // get From date picker with max date validation
    public static void getDateFromPickerCU(final TextView view, Activity activity) {
        final String[] date = new String[1];
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;

                if (dayOfMonth < 10) {
                    if (monthOfYear < 10) {
                        date[0] = "0" + dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = "0" + dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                } else {
                    if (monthOfYear < 10) {
                        date[0] = dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                }

                view.setText(getMMMDate(date[0]));
            }

        }, mYear, mMonth, mDay);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
        datePickerDialog.show();
    }

    public static void getDateFromPickerCU(Activity activity, FragmentManager manager, SelectDate selectDate) {

        //        datePicker.show(getSupportFragmentManager(), datePicker.getTag());

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startYear = currentYear - 100;
        currentYear = currentYear -18;

        DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance((DatePickerFragmentDialog.OnDateSetListener)(new DatePickerFragmentDialog.OnDateSetListener() {
            public final void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Intrinsics.checkNotNullExpressionValue(calendar, "calendar");
                selectDate.onSelect(simpleDateFormat.format(calendar.getTime()));

            }
        }), currentYear, currentMonth, currentDay);

        try {
            dialog.setMinDate(System.currentTimeMillis());
            dialog.setAccentColor(activity.getResources().getColor(R.color.app_green));
            dialog.show(manager,"");
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    // get From date picker with max date validation
    public static void getDateStartPickerCU(final TextView view, Activity activity, final TextView end_view, String no_days) {
        final String[] date = new String[1];

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;

                if (dayOfMonth < 10) {
                    if (monthOfYear < 10) {
                        date[0] = "0" + dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = "0" + dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                } else {
                    if (monthOfYear < 10) {
                        date[0] = dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                }

                view.setText(getMMMDate(date[0]));
                if (no_days != null)
                    end_view.setText(Common.getEndDate(view.getText().toString(), Integer.parseInt(no_days)));

            }

        }, mYear, mMonth, mDay);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
        datePickerDialog.show();
    }

    public static String parseDob(String date1) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(date1);
            String formattedDate = toDf.format(date);
            return formattedDate.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String changeFormatDob(String date1) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(date1);
            String formattedDate = toDf.format(date);
            return formattedDate.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // get to date picker min and max date validation.
    public static void getDateToPicker(final TextView view, Activity activity, String startDate) {
        final String[] date = new String[1];

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String maxDate = mYear + "-" + (mMonth + 1) + "-" + (mDay);

        DateFormat dfMin = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        DateFormat dfMax = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date newDateMin = null, newDateMax = null;
        try {
            newDateMin = dfMin.parse(startDate);
            newDateMax = dfMax.parse(maxDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;

                if (dayOfMonth < 10) {
                    if (monthOfYear < 10) {
                        date[0] = "0" + dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = "0" + dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                } else {
                    if (monthOfYear < 10) {
                        date[0] = dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                }

                view.setText(getMMMDate(date[0]));
            }

        }, mYear, mMonth, mDay);

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, 0);

        if (newDateMin != null) {
            datePickerDialog.getDatePicker().setMinDate(newDateMin.getTime());
        }
        if (newDateMax != null) {
            datePickerDialog.getDatePicker().setMaxDate(newDateMax.getTime());
        }
        datePickerDialog.show();
    }

    // get to date picker min and max date validation.
    public static void getDateToPickerCU(final TextView view, Activity activity, String startDate) {
        final String[] date = new String[1];

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String maxDate = mYear + "-" + (mMonth + 1) + "-" + (mDay);

        DateFormat dfMin = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        Date newDateMin = null;
        try {
            newDateMin = dfMin.parse(startDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;

                if (dayOfMonth < 10) {
                    if (monthOfYear < 10) {
                        date[0] = "0" + dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = "0" + dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                } else {
                    if (monthOfYear < 10) {
                        date[0] = dayOfMonth + "-0" + monthOfYear + "-" + year;
                    } else {
                        date[0] = dayOfMonth + "-" + monthOfYear + "-" + year;
                    }
                }

                view.setText(getMMMDate(date[0]));
            }

        }, mYear, mMonth, mDay);

        if (newDateMin != null) {
            datePickerDialog.getDatePicker().setMinDate(newDateMin.getTime());
        }

        datePickerDialog.show();
    }

    public static void noInternet(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.no_network_title))
                .setMessage(activity.getString(R.string.network_content))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void permissionDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.permission_title))
                .setMessage(activity.getString(R.string.title_camera_and_storage_access))
                .setCancelable(true)
                .setPositiveButton(activity.getString(R.string.open_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);

                    }
                })

                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                    ((TextView) alertDialog.findViewById(android.R.id.message)).setTypeface(typeface);

                    if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static String getMMMDate(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate2(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = df.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDateDay(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("EE", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            if (formattedDate.equalsIgnoreCase("mon")) {
                return 2;

            } else if (formattedDate.equalsIgnoreCase("tue")) {
                return 3;

            } else if (formattedDate.equalsIgnoreCase("wed")) {
                return 4;

            } else if (formattedDate.equalsIgnoreCase("thu")) {
                return 5;

            } else if (formattedDate.equalsIgnoreCase("fri")) {
                return 6;

            } else if (formattedDate.equalsIgnoreCase("sat")) {
                return 7;

            } else if (formattedDate.equalsIgnoreCase("sun")) {
                return 1;

            } else

                return 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDateDayI(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("EE", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            if (formattedDate.equalsIgnoreCase("mon")) {
                return 2;

            } else if (formattedDate.equalsIgnoreCase("tue")) {
                return 3;

            } else if (formattedDate.equalsIgnoreCase("wed")) {
                return 4;

            } else if (formattedDate.equalsIgnoreCase("thu")) {
                return 5;

            } else if (formattedDate.equalsIgnoreCase("fri")) {
                return 6;

            } else if (formattedDate.equalsIgnoreCase("sat")) {
                return 7;

            } else if (formattedDate.equalsIgnoreCase("sun")) {
                return 1;

            } else

                return 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getConvertTimeSlot(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = toDf.format(d);

        return formatted;
    }

    public static String getConvertTimeSlotNew(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = toDf.format(d);

        return formatted;
    }

    public static String getConvertTimeSlotDisplay(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = toDf.format(d);

        return formatted;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getConvertTimeSlotDisplayNew(String dateStr) {
        LocalTime time = LocalTime.parse(dateStr, DateTimeFormatter.ofPattern("HH:mm"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm a"));
        Log.e("abcd", formattedTime);
        //        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
//
//        Date d = null;
//        try {
//            d = df.parse(dateStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String formatted = toDf.format(d);

        return formattedTime;
    }

    public static String longToDate(String dateStr) {
        long val = Long.parseLong(dateStr);
        Date date = new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);

        return dateText;
    }

    public static String getConvertNotificationTime(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = toDf.format(d);
        return formatted;
    }

    public static String getCurrentTime(String dateStr, int booking_time) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat toDfH = new SimpleDateFormat("HH", Locale.ENGLISH);
        SimpleDateFormat toDfM = new SimpleDateFormat("mm", Locale.ENGLISH);
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date d = null;
        try {
            d = df.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = "";

        int hours = Integer.valueOf(toDfH.format(d));
        int minutes = Integer.valueOf(toDfM.format(d));

        int hr = (int) booking_time / 60;
        int min = (int) booking_time % 60;

        hours = hours + hr;
        minutes = minutes + min;

        if (minutes > 60) {
            int hr1 = (int) minutes / 60;
            int min1 = (int) minutes % 60;
            hours = hours + hr1;

            minutes = min1;
        }

        String h = "" + hours, m = "" + minutes;
        if (hours < 10)
            h = "0" + hours;

        if (minutes < 10)
            m = "0" + minutes;

        formatted = h + ":" + m;

        return formatted;
    }

    public static String getCurrentTime24(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        Date d = null;
        try {
            d = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = toDf.format(d);
        return formatted;
    }

    public static String getCurrentTime12(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        Date d = null;
        try {
            d = df.parse(dateStr);

            String formatted = toDf.format(d);
            return formatted;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getConvertDateTime(String dateStr) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertServerDateTimeA(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getConvertReportDateTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEndDate(String dt, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getCurrentDatePayOnly() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getCurrentDateWithLastUpdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getCurrentTimeWithLastUpdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getCurrentTimeWithLastUpdateAM() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getCurrentWeekDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getConvertTimeToHour(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("HH", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertTimeToMintue(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("mm", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertTimeTo24Hour(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertDatePrescription(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MM-yy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToTimeHome(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertTtime(String dateStr) {
        try {
            dateStr = dateStr.replace("T", " ");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat toDf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

            Date date = null;
            String[] arr = dateStr.split("\\.");
            if (arr.length > 0) {
                date = df.parse(arr[0]);
                String formattedDate = toDf.format(date);
                return formattedDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getConvertToTimeHour(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("HH", Locale.ENGLISH);
        Date date = null;

        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToTimeMinutes(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("mm", Locale.ENGLISH);

        Date date = null;

        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertDate15Buffer(String dateStr, int buffer) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long t = cal.getTimeInMillis();

            Date afterAddingMins = new Date(t + (buffer * ONE_MINUTE_IN_MILLIS));

            String formattedDate = toDf.format(afterAddingMins);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getConvertToPriceDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getConvertToCheckInTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getConvertToAMHome(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("a", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToDayHome(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd", Locale.getDefault());

        Date date;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        Date date;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToDateRequest(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToMonthHome(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String parseShortDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String parseShortTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    get Time Before 10 minuts
    public static String getTimeBefore10minutes(String currentDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(convertStringToDate(format.format(currentDate)));

            //Set calendar before 10 minutes
            calendar.add(Calendar.MINUTE, -10);
            //Formatter
            Date currentDatePlusOne = calendar.getTime();
            format.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));
            return format.format(currentDatePlusOne);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    //    Conver String_Date format into Date Formate
    public static Date convertStringToDate(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        Date date = null;
        try {
            date = format.parse(dateInString);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //    Difference between two Date
    public static long findDifference(String start_date, String end_date) {
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        Date endDate = convertStringToDate(end_date);
        Date startDate = convertStringToDate(start_date);
        // Try Class
        try {

            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            /*System.out.println("startDate : " + startDate);
            System.out.println("endDate : "+ endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/

            long diffTime = different / 60000;
            return diffTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String completedAssessmentDateTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getConvertToCheckIn(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getConvertToMonthHomeInt(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000+0000", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("MM", Locale.ENGLISH);

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return Integer.parseInt(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getConvertToDayInt(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000+0000", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd", Locale.ENGLISH);

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            if (Integer.parseInt(formattedDate) < 10) {
                formattedDate = "0" + formattedDate;
            }
            return Integer.parseInt(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getConvertToYearInt(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000+0000", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        toDf.setTimeZone(TimeZone.getTimeZone("Asia/Bahrain"));

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            if (Integer.parseInt(formattedDate) < 10) {
                formattedDate = "0" + formattedDate;
            }
            return Integer.parseInt(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void doctorNotAvailableDialog(Activity activity) {
        String phone = "";
        SharedPreferences preferences = activity.getSharedPreferences(Constants.PREFERENCE_CONFIG, Context.MODE_PRIVATE);
        if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            phone = preferences.getString("ph", "") + "+";
        } else
            phone = "+" + preferences.getString("ph", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.alert))
                .setMessage(activity.getString(R.string.doctor_not_available, phone))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                try {
                    if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                    } else {
                        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        alertDialog.show();
    }

    // Session Expired dialog.
    public static void sessionExpired(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setMessage(activity.getResources().getString(R.string.session_expired));
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPreferencesUtils.getInstance(activity).clearAll(activity);
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    // dialog.
    public static void showAlert(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    public static void simpleDailog(Context activity, String title, String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, activity.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void showAlert(Activity activity, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Need Attention");
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), listener);

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
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

    public static int getConvertToMonthReminderInt(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("MM", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            return Integer.parseInt(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getConvertToDayReminderInt(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("dd", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            if (Integer.parseInt(formattedDate) < 10) {
                formattedDate = "0" + formattedDate;
            }
            return Integer.parseInt(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getConvertToYeaReminderrInt(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat toDf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = df.parse(dateStr);
            String formattedDate = toDf.format(date);
            if (Integer.parseInt(formattedDate) < 10) {
                formattedDate = "0" + formattedDate;
            }
            return Integer.parseInt(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void showChatBot(Activity activity) {
        try {
            Dialog sucDialog = new Dialog(activity, R.style.MyAlertDialogThemeFullChat); // Context, this, etc.
//            sucDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sucDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


            sucDialog.setContentView(R.layout.dialog_chatbot);

            ImageView iv_back = sucDialog.findViewById(R.id.iv_back);
            WebView web_view = sucDialog.findViewById(R.id.web_view);
            ProgressBar pd_load = sucDialog.findViewById(R.id.pd_load);
            loadWeb(SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_CHATBOT_LINK, ""), web_view, pd_load);
            sucDialog.setCancelable(false);

            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sucDialog.cancel();
                }
            });

            sucDialog.show();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void loadWeb(String url, WebView webView, ProgressBar progressBar) {
        webView.invalidate();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

        });
    }

    //31-Oct-20        --> 2020-10-27
    public static String getConvert(String dateStr) {
        DateFormat originalFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date;
        String formattedDate = null;
        try {
            date = originalFormat.parse(dateStr);
            formattedDate = targetFormat.format(date);  // 20120821
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    /**
     * called for invoice open and open home fragment
     * this function handles activity results from multiple activity stacks
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data) {
        Log.v(activity.getClass().getSimpleName(), "onActivityResult ");
        if (requestCode == TelrActivity.PAYMENT_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (activity instanceof MainActivity) {
                    if (data.hasExtra(TelrActivity.SUCCESS)) {
                        showHomeFunctionality((MainActivity) activity);
                        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        pdfOpenintent.setDataAndType(data.getData(), "application/pdf");
                        try {
                            activity.startActivity(pdfOpenintent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (data.hasExtra(TelrActivity.FAIL)) {
                        showHomeFunctionality((MainActivity) activity);
                    }

                } else {
                    activity.setResult(Activity.RESULT_OK, data);
                    activity.finish();
                }
            }
        }

    }

    private static void showHomeFunctionality(MainActivity activity) {
        try {
            Intent refresh = new Intent(Constants.Filter.REFRESH_HOME);
            refresh.putExtra("cancel", "cancel");
            LocalBroadcastManager.getInstance(activity).sendBroadcast(refresh);
            activity.showToolbar();
            activity.selectHomeTab();
            activity.getSupportFragmentManager().popBackStack(HomeFragment.class.getName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createFile(Activity activity, String fileName, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void createFile(Fragment fragment, String fileName, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static AlertDialog customDialog(AppCompatActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));

            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(R.id.alertTitle)).setTypeface(typeface);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return alert;

    }

    public static void showConsentPopUpAgree(Activity activity, View.OnClickListener onClickListener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_dotcor_consent_pop_up_agree, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        TextView tv_i_agree = dialogView.findViewById(R.id.tv_i_agree);
        TextView tv_i_disagree = dialogView.findViewById(R.id.tv_i_disagree);
        WebView contentWeb = dialogView.findViewById(R.id.tv_content);
        ProgressBar progress_bar = dialogView.findViewById(R.id.progress_bar);
        LinearLayout layButton = dialogView.findViewById(R.id.layButton);

        contentWeb.getSettings().setJavaScriptEnabled(true);
        contentWeb.getSettings().setLoadWithOverviewMode(true);
        contentWeb.getSettings().setUseWideViewPort(true);
        contentWeb.getSettings().setAllowContentAccess(true);
        contentWeb.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        contentWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        contentWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        contentWeb.getSettings().setSupportZoom(true);

        if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))
            contentWeb.loadUrl(WebUrl.CONSENT_TELEMED_AR);
        else
            contentWeb.loadUrl(WebUrl.CONSENT_TELEMED);

        contentWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                contentWeb.setVisibility(View.VISIBLE);
                layButton.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            }


        });
        tv_i_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        tv_i_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onClickListener.onClick(v);
            }
        });


        alertDialog.show();

    }


}


