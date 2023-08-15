package sa.med.imc.myimc.Managebookings.presenter;

import android.app.Activity;

import androidx.room.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Appointmnet.model.PriceResponse;
import sa.med.imc.myimc.Database.ImcDatabase;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Managebookings.model.BookingResponseNew;
import sa.med.imc.myimc.Managebookings.model.VisitDetailReportResponse;
import sa.med.imc.myimc.Managebookings.view.BookingViews;
import sa.med.imc.myimc.Managebookings.view.VisitDetailViews;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.SYSQUO.util.Constant;
import sa.med.imc.myimc.Utils.Common;

/**
 * Booking API implementation class.
 * Get Bookings data
 * Reschedule booking
 * Cancel booking
 */

public class BookingImpl implements BookingPresenter {

    private Activity activity;
    private BookingViews views;
    private VisitDetailViews visitDetailViews;

    ImcDatabase db;

    public BookingImpl(BookingViews views, Activity activity) {
        this.views = views;
        this.activity = activity;
        db = Room.databaseBuilder(activity, ImcDatabase.class, "imc_database").
                fallbackToDestructiveMigration().allowMainThreadQueries().build();

    }

    public BookingImpl(VisitDetailViews views, Activity activity) {
        this.visitDetailViews = views;
        this.activity = activity;
        db = Room.databaseBuilder(activity, ImcDatabase.class, "imc_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    @Override
    public void callGetAllBookingsHomeApi(String token, String mrNumber, String itemCount, String page,
                                          String bookingStatus,int hosp) {
        // views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("fromDate", "");
            object.put("toDate", "");
            object.put("clinicCode", "");
            object.put("physicianCode", "");
            object.put("bookingStatus", bookingStatus);
            object.put("itemCount", itemCount);
            object.put("page", page);
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookingResponse> xxx = webService.getAllBookings(body);

        xxx.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    BookingResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {

                            if (response1.getBookings() != null)
                                for (int i = 0; i < response1.getBookings().size(); i++) {
                                    Booking data1 = response1.getBookings().get(i);
                                    data1.setMRN(mrNumber);
                                    db.bookingDataDao().saveBookingData(data1);
                                }

                            views.onGetBookings(response1);

                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {

//                    Executors.newSingleThreadExecutor().execute(new Runnable() {
//                        @Override
//                        public void run() {


                    /*List<Booking> dataList = db.bookingDataDao().loadBookingData(bookingStatus, mrNumber);

                    if (dataList != null && dataList.size() > 0) {
                        message = activity.getString(R.string.network_content);
                        views.onFail(message);

                        BookingResponse response = new BookingResponse();
                        response.setBookings(dataList);
                        response.setStatus("true");
                        response.setMessage("ddfs");
                        response.setTotalBookings(String.valueOf(dataList.size()));
                        views.onGetBookings(response);
                    } else {
                        views.onNoInternet();

                    }*/
//                        }
//                    });

                } else {
                    message = t.getMessage();
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetAllBookingsApi(int hosp, String token, String mrNumber, String itemCount, String page, String bookingStatus) {
        // views.showLoading();

        JSONObject object = new JSONObject();
        try {
            /*object.put("mrNumber", mrNumber);
            object.put("fromDate", "");
            object.put("toDate", "");
            object.put("clinicCode", "");
            object.put("physicianCode", "");
            object.put("bookingStatus", bookingStatus);
            object.put("itemCount", itemCount);
            object.put("page", page);
            object.put("hosp", hosp);*/

            object.put("hospitalCode", "IMC");
            object.put("patientId", SharedPreferencesUtils.getInstance().getValue(Constants.KEY_PATIENT_ID, ""));
            object.put("mrNumber", mrNumber);
            object.put("fromDate", "07-JUN-2000");
            object.put("toDate", "05-JUL-2050");
            object.put("bookingStatus", bookingStatus);
            object.put("physicianCode", "");
            object.put("clinicCode", "");
            object.put("page", "0");
            object.put("itemCount", "10000");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookingResponseNew> xxx = webService.getAllBookingsNew(body);

        xxx.enqueue(new Callback<BookingResponseNew>() {
            @Override
            public void onResponse(Call<BookingResponseNew> call, Response<BookingResponseNew> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    BookingResponseNew response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {

                            /*if (response1.getBookings() != null)
                                for (int i = 0; i < response1.getBookings().size(); i++) {
                                    BookingResponseNew.Bookings data1 = response1.getBookings().get(i);
//                                    data1.setMRN(mrNumber);
                                    db.bookingDataDao().saveBookingData(data1);
                                }*/

                            views.onGetBookingsNew(response1);

                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<BookingResponseNew> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {

//                    Executors.newSingleThreadExecutor().execute(new Runnable() {
//                        @Override
//                        public void run() {


                    /*List<Booking> dataList = db.bookingDataDao().loadBookingData(bookingStatus, mrNumber);

                    if (dataList != null && dataList.size() > 0) {
                        message = activity.getString(R.string.network_content);
                        views.onFail(message);

                        BookingResponse response = new BookingResponse();
                        response.setBookings(dataList);
                        response.setStatus("true");
                        response.setMessage("ddfs");
                        response.setTotalBookings(String.valueOf(dataList.size()));
                        views.onGetBookings(response);
                    } else {
                        views.onNoInternet();

                    }*/
//                        }
//                    });

                } else {
                    message = t.getMessage();
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetAllBookingsApi(int hosp, String token, String mrNumber, String itemCount, String page, String bookingStatus, String clinicCode, String doc_code, String from, String to) {
        if (page.equalsIgnoreCase("0"))
            views.showLoading();

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("fromDate", from);
            object.put("toDate", to);
            object.put("clinicCode", clinicCode);
            object.put("physicianCode", doc_code);
            object.put("bookingStatus", bookingStatus);
            object.put("itemCount", itemCount);
            object.put("page", page);
            object.put("hosp", hosp);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookingResponse> xxx = webService.getAllBookings(body);

        xxx.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    BookingResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onGetBookings(response1);

                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {


//                    Executors.newSingleThreadExecutor().execute(new Runnable() {
//                        @Override
//                        public void run() {



                   /* List<Booking> dataList = db.bookingDataDao().loadBookingData(bookingStatus, mrNumber);

                    if (dataList != null && dataList.size() > 0) {
                        message = activity.getString(R.string.network_content);
                        views.onFail(message);

                        BookingResponse response = new BookingResponse();
                        response.setBookings(dataList);
                        response.setStatus("true");
                        response.setMessage("ddfs");
                        response.setTotalBookings(String.valueOf(dataList.size()));
                        views.onGetBookings(response);
                    } else {
                        views.onNoInternet();

                    }*/
//                        }
//                    });

                } else {
                    message = t.getMessage();
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callCancelBooking(String booking_id, String token, String mrn, int hosp) {
        views.showLoading();

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<SimpleResponse> xxx = webService.cancelBooking(booking_id, mrn, hosp);

        xxx.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    SimpleResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            views.onCancel(response1);
                            db.bookingDataDao().sentStatus("3", booking_id, mrn);

                        } else
                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                views.hideLoading();
                String message = t.getMessage();

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(activity.getString(R.string.time_out_messgae));
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
//                    message = activity.getString(R.string.network_content);
//                    views.onFail(message);
                    Common.noInternet(activity);

//                    views.onNoInternet();
                } else {
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetReportsCount(String token, String mrn, String espisode_id, int hosp) {
        JSONObject object = new JSONObject();
        try {
            object.put("mrnNumber", mrn);
            object.put("epsoide", espisode_id);
            object.put("hosp", hosp);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<VisitDetailReportResponse> xxx = webService.getReportsCount(body);

        xxx.enqueue(new Callback<VisitDetailReportResponse>() {
            @Override
            public void onResponse(Call<VisitDetailReportResponse> call, Response<VisitDetailReportResponse> response) {
                visitDetailViews.hideLoading();
                if (response.isSuccessful()) {
                    VisitDetailReportResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            visitDetailViews.onGetReportsCount(response1);
                        } else
                            visitDetailViews.onFail(response1.getMessage());
                    } else {
                        visitDetailViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    visitDetailViews.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<VisitDetailReportResponse> call, Throwable t) {
                visitDetailViews.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    visitDetailViews.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    visitDetailViews.onNoInternet();
                } else {
                    message = t.getMessage();
                    visitDetailViews.onFail(message);
                }
            }
        });
    }

    @Override
    public void callPrintSlipApi(String token, String mrn, Integer espisode_id, int hosp) {
        views.showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("mrn", mrn);
            object.put("bookingId", espisode_id);
            object.put("hosp", hosp);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());


        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ResponseBody> xxx = webService.downloadConfirmBooking(body);

        xxx.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                views.hideLoading();
                Headers headers = response.headers();
                if (response.isSuccessful()) {
                    ResponseBody response1 = response.body();
                    if (response1 != null) {
                        views.onDownloadConfirmation(response1, headers);
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = activity.getString(R.string.time_out_messgae);
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = t.getMessage();
                    views.onFail(message);
                }
            }
        });
    }

    @Override
    public void callGetAppointmentPrice(String token, String mrNumber, String sessionId, String date, int hosp) {
        views.showLoading();
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PriceResponse> xxx = webService.getCheckInPrice(sessionId, date);

        xxx.enqueue(new Callback<PriceResponse>() {
            @Override
            public void onResponse(Call<PriceResponse> call, Response<PriceResponse> response) {
                views.hideLoading();
                if (response.isSuccessful()) {
                    PriceResponse response1 = response.body();
                    if (response1 != null) {
//                        if (response1.getStatus().equalsIgnoreCase("true")) {
                        views.onGetPrice(response1);
//                        } else
//                            views.onFail(response1.getMessage());
                    } else {
                        views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                    }
                } else {
                    views.onFail(activity.getResources().getString(R.string.plesae_try_again));
                }
            }

            @Override
            public void onFailure(Call<PriceResponse> call, Throwable t) {
                views.hideLoading();
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                    views.onFail(message);
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    views.onNoInternet();
                } else {
                    message = "Unknown";
                    views.onFail(message);
                }
            }
        });
    }

}
