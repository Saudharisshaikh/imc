package sa.med.imc.myimc.Managebookings.view;

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

import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class BottomFilterBookingDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.clinic_spinner)
    Spinner clinicSpinner;
    @BindView(R.id.doctor_spinner)
    Spinner doctorSpinner;
    @BindView(R.id.select_from_date)
    TextView selectFromDate;
    @BindView(R.id.select_to_date)
    TextView selectToDate;
    @BindView(R.id.progress_circular)
    ProgressBar progress_circular;

    private BottomDialogListener mDialogListener;
    String from = "", to = "", doc_id = "", clinic_id = "";
    int type = -1;


    ArrayList<String> clinics_list = new ArrayList<>();
    ArrayList<String> doc_list = new ArrayList<>();

    public static ClinicResponse response1;
    public static PhysicianResponse physicianResponse;

    @NonNull
    public static BottomFilterBookingDialog newInstance() {
        BottomFilterBookingDialog astro = new BottomFilterBookingDialog();
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
        View view = inflater.inflate(R.layout.bottom_sheet_booking_filters, container, false);

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
            callGetAllLanguages(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""), "",
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));

        if (physicianResponse != null)
            setDataPhysician(physicianResponse);
        else
            callGetAllDoctors(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    "0", SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));


        clinicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    callGetAllDoctors(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                            response1.getSpecialityList().get(position - 1).getSpecialityCode(),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""),
                            SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));
//                } else if (position == 0 && response1 != null) {
//                    callGetAllLanguages(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),"");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
//                    callGetAllLanguages(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""), physicianResponse.getPhysicians().get(position - 1).getDeptCode());
//                }
//                else if (position == 0 && physicianResponse != null) {
//                    callGetAllDoctors(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""), "", SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_MRN, ""));

//                }
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
                if (clinics_list.size() > 0)
                    if (clinicSpinner.getSelectedItemPosition() > 0) {
                        clinic_id = response1.getSpecialityList().get(clinicSpinner.getSelectedItemPosition() - 1).getSpecialityCode();
                    } else {
                        clinic_id = "";
                    }
                else
                    clinic_id = "";

                if (doc_list.size() > 0)
                    if (doctorSpinner.getSelectedItemPosition() > 0) {
                        doc_id = physicianResponse.getPhysicians().get(doctorSpinner.getSelectedItemPosition() - 1).getDocCode();
                    } else {
                        doc_id = "";
                    }
                else
                    doc_id = "";

                from = selectFromDate.getText().toString();
                to = selectToDate.getText().toString();
                mDialogListener.onDone(doc_id, clinic_id, from, to);
                dismiss();

                break;

            case R.id.iv_clear:
                // clear all data used to filter contents
                clinicSpinner.setSelection(0);
                doctorSpinner.setSelection(0);
                physicianResponse = null;

                from = "";
                to = "";
                doc_id = "";
                clinic_id = "";
                mDialogListener.onClear("clear");

                dismiss();

                break;

            case R.id.select_from_date:
                if (type > -1)
                    Common.getDateFromPickerCU(selectFromDate, getActivity());
                else
                    Common.getDateFromPicker(selectFromDate, getActivity());

                break;

            case R.id.select_to_date:
                if (selectFromDate.getText().toString().trim().length() > 0) {
                    if (type > -1)
                        Common.getDateToPickerCU(selectToDate, getActivity(), selectFromDate.getText().toString());
                    else
                        Common.getDateToPicker(selectToDate, getActivity(), selectFromDate.getText().toString());

                } else
                    Common.makeToast(getActivity(), getString(R.string.select_from));

                break;
        }
    }

    public interface BottomDialogListener {
        void onDone(String doc_id, String clinic_id, String from, String to);

        void onClear(String text);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Get All clinics_list doctors know
    public void callGetAllLanguages(String token, String physicianId, int hosp) {
        // Common.showDialog(getContext());

        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", physicianId);
            object.put("isActive", "Y");
            object.put("search_txt", "");
            object.put("item_count", "0");
            object.put("page", "0");
            object.put("hosp", hosp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<ClinicResponse> xxx = webService.getAllClinicsFilter(body);

        xxx.enqueue(new Callback<ClinicResponse>() {
            @Override
            public void onResponse(Call<ClinicResponse> call, Response<ClinicResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    response1 = response.body();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<ClinicResponse> call, Throwable t) {
                Common.hideDialog();
            }
        });

    }


    // Get All clinics_list doctors know
    public void callGetAllDoctors(String token, String clinic_id, String mrn, int hosp) {
        if (!clinic_id.equalsIgnoreCase("0"))
            progress_circular.setVisibility(View.VISIBLE);

        JSONObject object = new JSONObject();
        try {
            object.put("mrnumber", mrn);

            object.put("clinic_code", clinic_id);
            object.put("search_txt", "");
            object.put("rating", "");
            object.put("serviceid", "");
            object.put("lang", "");
            object.put("deptCode", "");
            object.put("getall", "Y");
            object.put("item_count", "12");
            object.put("page", "0");
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());

        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<PhysicianResponse> xxx = webService.getAllDoctorsFilters(body);

        xxx.enqueue(new Callback<PhysicianResponse>() {
            @Override
            public void onResponse(Call<PhysicianResponse> call, Response<PhysicianResponse> response) {
                progress_circular.setVisibility(View.GONE);
                if (response.isSuccessful()) {
//                    if (clinic_id.length() == 0)
                    physicianResponse = response.body();
                    setDataPhysician(response.body());
                }
            }

            @Override
            public void onFailure(Call<PhysicianResponse> call, Throwable t) {
                progress_circular.setVisibility(View.GONE);
            }
        });

    }

    void setData() {
        try {

            if (response1 != null) {
                if (response1.getStatus().equalsIgnoreCase("true")) {
                    clinics_list = new ArrayList<>();

                    /*if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH))
                        for (int i = 0; i < response1.getSpecialityList().size(); i++) {
                            if (response1.getSpecialityList().get(i).getDescEn() != null)
                                clinics_list.add(response1.getStatus().get(i).getDescEn());
                        }
                    else if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))

                        for (int i = 0; i < response1.getClinicModels().size(); i++) {
                            if (response1.getClinicModels().get(i).getDescAr() != null)
                                clinics_list.add(response1.getClinicModels().get(i).getDescAr());
                        }*/

                    for (int i = 0; i < response1.getSpecialityList().size(); i++) {
                        clinics_list.add(response1.getSpecialityList().get(i).getSpecialityDescription());
                    }
                    if (getActivity() != null) {

                        clinics_list.add(0, getString(R.string.select));

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clinics_list); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        clinicSpinner.setAdapter(spinnerArrayAdapter);
                    }
                }
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void setDataPhysician(PhysicianResponse physicianResponse) {
        if (physicianResponse != null) {
            if (physicianResponse.getStatus().equalsIgnoreCase("true")) {
                doc_list = new ArrayList<>();

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH))
                    for (int i = 0; i < physicianResponse.getPhysicians().size(); i++) {
                        if (physicianResponse.getPhysicians().get(i).getGivenName() != null)
                            doc_list.add(physicianResponse.getPhysicians().get(i).getGivenName() + " " + physicianResponse.getPhysicians().get(i).getFamilyName());
                    }
                else if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))

                    for (int i = 0; i < physicianResponse.getPhysicians().size(); i++) {
                        if (physicianResponse.getPhysicians().get(i).getGivenNameAr() != null)
                            doc_list.add(physicianResponse.getPhysicians().get(i).getGivenNameAr() + " " + physicianResponse.getPhysicians().get(i).getFamilyNameAr());
                    }

                if (getActivity() != null) {
                    doc_list.add(0, getActivity().getString(R.string.select));

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, doc_list); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    doctorSpinner.setAdapter(spinnerArrayAdapter);
                }
            }
        }
    }
}
