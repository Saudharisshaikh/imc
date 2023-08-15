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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.Physicians.model.DepartResponse;
import sa.med.imc.myimc.Physicians.model.LanguageCMSResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */

public class BottomFilterDoctorDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.departments_spinner)
    Spinner departmentsSpinner;
    @BindView(R.id.languages_spinner)
    Spinner languagesSpinner;
    @BindView(R.id.sort_spinner)
    Spinner sortSpinner;
    @BindView(R.id.rd_both)
    RadioButton rdBoth;
    @BindView(R.id.rd_male)
    RadioButton rdMale;
    @BindView(R.id.rd_female)
    RadioButton rdFemale;
    private BottomDialogListener mDialogListener;
    String lang_id = "", depart_id = "";
    int type = -1;


    ArrayList<String> languages_list = new ArrayList<>();
    ArrayList<String> depart_list = new ArrayList<>();
    ArrayList<String> sort_list = new ArrayList<>();

    public static LanguageCMSResponse response1;
    public static DepartResponse departResponse;

    @NonNull
    public static BottomFilterDoctorDialog newInstance() {
        BottomFilterDoctorDialog astro = new BottomFilterDoctorDialog();
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
        View view = inflater.inflate(R.layout.bottom_sheet_find_doctors, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);
        type = -1;

        if (getArguments() != null)
            type = getArguments().getInt("i");

        setUpSpinners();

        if (response1 != null)
            setData();
        else
            callGetAllLanguages();

//        if (departResponse != null)
//            setDataDepartList(departResponse);
//        else
//            callGetAllDepartments();


        departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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


    @OnClick({R.id.iv_close, R.id.iv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                // apply selected filter values
//                if (languages_list.size() > 0)
//                    if (departmentsSpinner.getSelectedItemPosition() > 0)
//                        depart_id = response1.getClinicModels().get(departmentsSpinner.getSelectedItemPosition() - 1).getId();
//
//
//                if (depart_list.size() > 0)
//                    if (languagesSpinner.getSelectedItemPosition() > 0)
//                        lang_id = physicianResponse.getPhysicians().get(languagesSpinner.getSelectedItemPosition() - 1).getDocCode();

                mDialogListener.onDone(lang_id, depart_id);
                dismiss();

                break;

            case R.id.iv_clear:
                // clear all data used to filter contents
                departmentsSpinner.setSelection(0);
                languagesSpinner.setSelection(0);

                lang_id = "";
                depart_id = "";
                mDialogListener.onClear("clear");

                dismiss();

                break;


        }
    }

    public interface BottomDialogListener {
        void onDone(String doc_id, String clinic_id);

        void onClear(String text);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Get All languages_list doctors know
    public void callGetAllLanguages() {

        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");
        Call<LanguageCMSResponse> xxx = webService.getAllLanguagesCMS();

        xxx.enqueue(new Callback<LanguageCMSResponse>() {
            @Override
            public void onResponse(Call<LanguageCMSResponse> call, Response<LanguageCMSResponse> response) {
                Common.hideDialog();
                if (response.isSuccessful()) {
                    response1 = response.body();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<LanguageCMSResponse> call, Throwable t) {
                Common.hideDialog();
            }
        });

    }


    // Get All languages_list doctors know
//    public void callGetAllDepartments() {
////        if (depart_id.length() != 0)
////            Common.showDialog(getContext());
//
//        WebService webService = ServiceGenerator.createService(WebService.class, WebUrl.BASE_URL_LINKS, "");
//        Call<DepartResponse> xxx = webService.getDeptAll();
//
//        xxx.enqueue(new Callback<DepartResponse>() {
//            @Override
//            public void onResponse(Call<DepartResponse> call, Response<DepartResponse> response) {
//                Common.hideDialog();
//                if (response.isSuccessful()) {
//                    departResponse = response.body();
//                    setDataDepartList(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DepartResponse> call, Throwable t) {
//                Common.hideDialog();
//            }
//        });
//
//    }

    void setData() {
        if (response1 != null) {
            if (response1.getStatus()) {
                languages_list = new ArrayList<>();

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH))
                    for (int i = 0; i < response1.getData().size(); i++) {
                        if (response1.getData().get(i).getFullEn() != null)
                            languages_list.add(response1.getData().get(i).getFullEn());
                    }
                else if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))

                    for (int i = 0; i < response1.getData().size(); i++) {
                        if (response1.getData().get(i).getFullAr() != null)
                            languages_list.add(response1.getData().get(i).getFullAr());
                    }

                languages_list.add(0, getString(R.string.select));

                ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages_list); //selected item will look like a spinner set from XML
                spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                languagesSpinner.setAdapter(spinnerArrayAdapter1);

            }
        }
    }

    void setUpSpinners() {
        depart_list = new ArrayList<>();
        depart_list.add(0, getActivity().getString(R.string.select));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, depart_list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentsSpinner.setAdapter(spinnerArrayAdapter);

        languages_list = new ArrayList<>();
        languages_list.add(0, getString(R.string.select));

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages_list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(spinnerArrayAdapter1);

        sort_list = new ArrayList<>();
        sort_list.add(getActivity().getString(R.string.first_a_z));
        sort_list.add(getActivity().getString(R.string.first_z_a));
        sort_list.add(getActivity().getString(R.string.last_a_z));
        sort_list.add(getActivity().getString(R.string.last_z_a));

        ArrayAdapter<String> spinnerArrayAdapte2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sort_list); //selected item will look like a spinner set from XML
        spinnerArrayAdapte2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerArrayAdapte2);
    }


    void setDataDepartList(DepartResponse departResponse) {
        if (departResponse != null) {
            if (departResponse.getStatus()) {
                depart_list = new ArrayList<>();

                if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ENGLISH))
                    for (int i = 0; i < departResponse.getData().size(); i++) {
                        if (departResponse.getData().get(i).getTitleEn() != null)
                            depart_list.add(departResponse.getData().get(i).getTitleEn());
                    }
                else if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC))

                    for (int i = 0; i < departResponse.getData().size(); i++) {
                        if (departResponse.getData().get(i).getTitleAr() != null)
                            depart_list.add(departResponse.getData().get(i).getTitleAr());
                    }

                if (getActivity() != null) {

                    Collections.sort(depart_list);

                    depart_list.add(0, getActivity().getString(R.string.select));

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, depart_list); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    departmentsSpinner.setAdapter(spinnerArrayAdapter);

                }
            }
        }
    }
}
