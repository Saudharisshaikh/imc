package sa.med.imc.myimc.HealthSummary.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Managebookings.model.BookingResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;


/**
 *
 */

public class BottomFilterReadingDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.booking_id_date_spinner)
    Spinner booking_id_date_spinner;
    @BindView(R.id.select_from_date)
    TextView selectFromDate;
    @BindView(R.id.select_to_date)
    TextView selectToDate;
    @BindView(R.id.progress_circular)
    ProgressBar progressCircular;

    private BottomDialogListener mDialogListener;
    String from = "", to = "", booking_id = "";
    int type = -1;


    ArrayList<String> list = new ArrayList<>();

    public static BookingResponse response1;

    @NonNull
    public static BottomFilterReadingDialog newInstance() {
        BottomFilterReadingDialog astro = new BottomFilterReadingDialog();
        return astro;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // reusing...
        View view = inflater.inflate(R.layout.bottom_sheet_reading_filters, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);
        type = -1;

        if (getArguments() != null)
            type = getArguments().getInt("i");

        selectFromDate.setText(from);
        selectToDate.setText(to);

        if (response1 != null)
            setData();
        else
            callGetCompletedBookings(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

        booking_id_date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    from = "";
                    to = "";
                    selectFromDate.setText("");
                    selectToDate.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mDialogListener = (BottomDialogListener) parent;
        } else {
            mDialogListener = (BottomDialogListener) context;
        }
    }

    @Override
    public void onDetach() {
        mDialogListener = null;
        super.onDetach();
    }


    @OnClick({R.id.iv_close, R.id.iv_clear, R.id.select_to_date, R.id.select_from_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                // apply selected filter values
                if (list.size() > 0)
                    if (booking_id_date_spinner.getSelectedItemPosition() > 0) {
                        booking_id = String.valueOf(response1.getBookings().get(booking_id_date_spinner.getSelectedItemPosition() - 1).getId());
                    } else {
                        booking_id = "";
                    }
                else
                    booking_id = "";


                from = selectFromDate.getText().toString();
                to = selectToDate.getText().toString();
                mDialogListener.onDone(booking_id, from, to);
                dismiss();

                break;

            case R.id.iv_clear:
                // clear all data used to filter contents
                booking_id_date_spinner.setSelection(0);

                from = "";
                to = "";
                booking_id = "";

                mDialogListener.onClear("clear");

                dismiss();

                break;

            case R.id.select_from_date:
                Common.getDateFromPicker(selectFromDate, getActivity());

                break;

            case R.id.select_to_date:
                if (selectFromDate.getText().toString().trim().length() > 0) {
                    Common.getDateToPicker(selectToDate, getActivity(), selectFromDate.getText().toString());
                    booking_id_date_spinner.setSelection(0);
                } else
                    Common.makeToast(getActivity(), getString(R.string.select_from));

                break;
        }
    }

    public interface BottomDialogListener {
        void onDone(String booking_id, String from, String to);

        void onClear(String text);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Get All list doctors know
    public void callGetCompletedBookings(String token, String mrNumber) {
        progressCircular.setVisibility(View.VISIBLE);
        // Common.showDialog(getContext());

        JSONObject object = new JSONObject();
        try {
            object.put("mrNumber", mrNumber);
            object.put("fromDate", "");
            object.put("toDate", "");
            object.put("clinicCode", "");
            object.put("physicianCode", "");
            object.put("bookingStatus", Constants.BookingStatus.COMPLETED);
            object.put("itemCount", "999");
            object.put("page", "0");
            object.put("hosp", SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.SELECTED_HOSPITAL, 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<BookingResponse> xxx = webService.getAllBookings(body);

        xxx.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                Common.hideDialog();
                progressCircular.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    response1 = response.body();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                Common.hideDialog();
                progressCircular.setVisibility(View.GONE);

            }
        });

    }

    void setData() {
        try {

            if (response1 != null) {
                if (response1.getStatus().equalsIgnoreCase("true")) {
                    list = new ArrayList<>();

                    if (response1.getBookings() != null)
                        for (int i = 0; i < response1.getBookings().size(); i++) {
                            list.add(response1.getBookings().get(i).getId() + " - " + Common.getConvertToDate(response1.getBookings().get(i).getApptDateString()));
                        }

                    if (getActivity() != null) {

                        list.add(0, getString(R.string.select));

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        booking_id_date_spinner.setAdapter(spinnerArrayAdapter);
                    }
                }
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
