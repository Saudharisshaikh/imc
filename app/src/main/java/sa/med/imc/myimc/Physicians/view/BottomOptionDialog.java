package sa.med.imc.myimc.Physicians.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Physicians.model.LanguageResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class BottomOptionDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.clinic_spinner)
    Spinner clinicSpinner;
    @BindView(R.id.depart_spinner)
    Spinner depart_spinner;
    @BindView(R.id.rd_one)
    CheckBox rdOne;
    private BottomDailogListener mDialogListener;
    public static ClinicResponse departResponseDoc;

    ArrayList<String> languages = new ArrayList<>();
    ArrayList<String> departments = new ArrayList<>();

    public static LanguageResponse response1;

    @NonNull
    public static BottomOptionDialog newInstance() {
        BottomOptionDialog astro = new BottomOptionDialog();
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
        View view = inflater.inflate(R.layout.bottom_sheet_filters, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);

        if (response1 != null)
            setData();
        else
            callGetAllLanguages(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""));

        if (departResponseDoc != null)
            setDataDepartList(departResponseDoc);
        else
            callGetAllDepartments(SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_ACCESS_TOKEN, ""),
                    SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0));


//        if (getActivity() != null) {
//            departments = new ArrayList<>();
//            departments.add(0, getString(R.string.select));
//
//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, departments); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            depart_spinner.setAdapter(spinnerArrayAdapter);
//        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mDialogListener = (BottomDailogListener) parent;
        } else {
            mDialogListener = (BottomDailogListener) context;
        }
    }

    @Override
    public void onDetach() {
        mDialogListener = null;
        super.onDetach();
    }


    @OnClick({R.id.iv_close, R.id.iv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                String rat = "", id = "";

//                if (rdOne.isChecked())
//                    rat = "5";
//                else
//                    rat = "";

                if (languages.size() > 0)
                    if (clinicSpinner.getSelectedItemPosition() > 0)
                        id = response1.getLanguages().get(clinicSpinner.getSelectedItemPosition() - 1).getDescEn().toLowerCase();


                if (departments.size() > 0)
                    if (depart_spinner.getSelectedItemPosition() > 0)
                        rat = departResponseDoc.getSpecialityList().get(depart_spinner.getSelectedItemPosition() - 1).getSpecialityCode();

                mDialogListener.onDone(rat, id);
                dismiss();

                break;

            case R.id.iv_clear:
                rdOne.setChecked(false);
                clinicSpinner.setSelection(0);
                depart_spinner.setSelection(0);

                mDialogListener.onClear("clear");
                dismiss();

                break;
        }
    }


    public interface BottomDailogListener {
        void onDone(String rating, String id);

        void onClear(String text);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Get All languages doctors know
    public void callGetAllLanguages(String token) {
//        Common.showDialog(getContext());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
        Call<LanguageResponse> xxx = webService.getLanguages();

        xxx.enqueue(new Callback<LanguageResponse>() {
            @Override
            public void onResponse(Call<LanguageResponse> call, Response<LanguageResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    response1 = response.body();
                    setData();
                }


            }

            @Override
            public void onFailure(Call<LanguageResponse> call, Throwable t) {
                Common.hideDialog();
            }
        });

    }

    void setData() {
        if (response1 != null) {
            if (response1.getStatus().equalsIgnoreCase("true")) {
                languages = new ArrayList<>();

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {

                    for (int i = 0; i < response1.getLanguages().size(); i++) {
                        if (response1.getLanguages().get(i).getDescAr() != null && response1.getLanguages().get(i).getShowOnPortal().equalsIgnoreCase("1"))
                            languages.add(response1.getLanguages().get(i).getDescAr());
                    }
                } else
                    for (int i = 0; i < response1.getLanguages().size(); i++) {
                        if (response1.getLanguages().get(i).getDescEn() != null && response1.getLanguages().get(i).getShowOnPortal().equalsIgnoreCase("1"))
                            languages.add(response1.getLanguages().get(i).getDescEn());
                    }


                if (getActivity() != null) {

                    languages.add(0, getString(R.string.select));

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clinicSpinner.setAdapter(spinnerArrayAdapter);
                }
            }
        }
    }

    // Get All languages_list doctors know
    public void callGetAllDepartments(String token, int hosp) {
//        if (depart_id.length() != 0)
//            Common.showDialog(getContext());

//        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        Call<LanguageResponse> xxx = webService.getDeptAll();
//
//        xxx.enqueue(new Callback<LanguageResponse>() {
//            @Override
//            public void onResponse(Call<LanguageResponse> call, Response<LanguageResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    departResponseDoc = response.body();
//                    setDataDepartList(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LanguageResponse> call, Throwable t) {
//                Common.hideDialog();
//            }
//        });

        /////////////
        JSONObject object = new JSONObject();
        try {
            object.put("physicianId", "");
            object.put("isActive", "Y");
            object.put("search_txt", "");
            object.put("item_count", "999");
            object.put("page", "0");
            object.put("hosp", hosp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object).toString());
        WebService webService = ServiceGenerator.createService(WebService.class, "Bearer " + token);
//        PIYUSH TRACK CARE
        Call<ClinicResponse> xxx = webService.getAllWebClinics(SharedPreferencesUtils.getInstance(getContext()).getValue(Constants.HOSPITAL_CODE, "IMC"));

        xxx.enqueue(new Callback<ClinicResponse>() {
            @Override
            public void onResponse(Call<ClinicResponse> call, Response<ClinicResponse> response) {
                //  views.hideLoading();
                if (response.isSuccessful()) {
                    ClinicResponse response1 = response.body();
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase("true")) {
                            departResponseDoc = response.body();
                            setDataDepartList(response.body());
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ClinicResponse> call, Throwable t) {
                String message = "";

                if (t instanceof SocketTimeoutException) {//ClassNotFoundException
                    message = "timeout";
                } else if (t instanceof ConnectException || t instanceof UnknownHostException) {
                    //  views.onNoInternet();
                } else {
                    message = "Unknown";
                }
            }
        });

        //////////////////////

    }


    void setDataDepartList(ClinicResponse departResponse) {
        if (departResponse != null) {
            if (departResponse.getStatus().equalsIgnoreCase("true")) {
                departments = new ArrayList<>();


                /*if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {

                    for (int i = 0; i < departResponse.getSpecialityList().size(); i++) {
                        if (departResponse.getClinicModels().get(i).getDescAr() != null)
                            departments.add(departResponse.getClinicModels().get(i).getDescAr());
                    }

                } else
                    for (int i = 0; i < departResponse.getClinicModels().size(); i++) {
                        if (departResponse.getClinicModels().get(i).getDescEn() != null)
                            departments.add(departResponse.getClinicModels().get(i).getDescEn());
                    }*/
                for (int i = 0; i < departResponse.getSpecialityList().size(); i++) {
                    if (departResponse.getSpecialityList().get(i).getSpecialityDescription() != null)
                        departments.add(departResponse.getSpecialityList().get(i).getSpecialityDescription());
                }

                if (getActivity() != null) {

                    //Collections.sort(departments);
                    departments.add(0, getActivity().getString(R.string.select));

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, departments); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    depart_spinner.setAdapter(spinnerArrayAdapter);

                }
            }
        }
    }
}
