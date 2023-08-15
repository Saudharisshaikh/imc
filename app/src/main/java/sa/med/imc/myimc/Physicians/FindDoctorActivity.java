package sa.med.imc.myimc.Physicians;

import static sa.med.imc.myimc.Base.BaseActivity.hideKeyboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Clinics.presenter.ClinicImpl;
import sa.med.imc.myimc.Clinics.presenter.ClinicPresenter;
import sa.med.imc.myimc.Clinics.view.ClinicViews;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.view.PhysicianFullDetailActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansActivity;
import sa.med.imc.myimc.Physicians.view.PhysiciansFragment;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.cardiology.CardiologyReportNewFragment;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

public class FindDoctorActivity extends AppCompatActivity implements ClinicViews {

    Spinner clinic_spinner;
    RecyclerView rv_physicians;
    ImageView iv_back;
    ClinicPresenter presenter;
    List<ClinicResponse.SpecialityList> list = new ArrayList<>();

    LinearLayout filter_layout;
    boolean edit = false;
    ImageView ivSearch;
    CustomTypingEditText edSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor2);
        presenter = new ClinicImpl(this, this);

        initView();
    }

    private void initView() {
        clinic_spinner = findViewById(R.id.clinic_spinner);
        rv_physicians = findViewById(R.id.rv_physicians);
        iv_back = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);
        edSearch = findViewById(R.id.ed_search);
        filter_layout = findViewById(R.id.filter_layout);

        loadClinic();

        iv_back.setOnClickListener(view -> finish());

        clinic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    filter_layout.setVisibility(View.VISIBLE);
                } else {
                    filter_layout.setVisibility(View.INVISIBLE);
                }
                ivSearch.setImageResource(R.drawable.ic_search);
                edSearch.setVisibility(View.GONE);
                edit = false;
                edSearch.clearFocus();
                edSearch.clear();
                loadDoctor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ivSearch.setOnClickListener(view -> {
            if (edit) {
                ivSearch.setImageResource(R.drawable.ic_search);
                edSearch.setVisibility(View.GONE);
                edit = false;
                edSearch.clearFocus();

            } else {
                ivSearch.setImageResource(R.drawable.ic_close);
                edSearch.setVisibility(View.VISIBLE);
                edit = true;
                edSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        edSearch.setOnTypingModified((view, isTyping) -> {
            if (!isTyping) {
                if (edSearch.getText().toString().trim().length() > 0) {
//                    Log.e("abcd",edSearch.getText().toString().trim());
                }
            }
        });
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(FindDoctorActivity.this);
//                page = 0;
//                callAPI(page);
                Intent search = new Intent(Constants.physician_search);
                search.putExtra("key", edSearch.getText().toString().trim());
                LocalBroadcastManager.getInstance(this).sendBroadcast(search);
                return true;
            }
            return false;
        });
    }


    private void loadDoctor() {

        if (clinic_spinner.getSelectedItemPosition() == 0) {

            FragmentManager manager = getSupportFragmentManager();
            androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframe, new AllDoctorFragment(FindDoctorActivity.this,false)).commit();
            return;
        } else {
            String specialityCode = list.get(clinic_spinner.getSelectedItemPosition() - 1).getSpecialityCode();
            String specialityDescription = list.get(clinic_spinner.getSelectedItemPosition() - 1).getSpecialityDescription();
            String arabicSpecialityDescription = list.get(clinic_spinner.getSelectedItemPosition() - 1).getArabicSpecialityDescription();

            Log.e("Activ", specialityCode + " " + specialityDescription);

            FragmentManager manager = getSupportFragmentManager();
            androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainframe, new FindDoctorFragment(specialityCode, specialityDescription,arabicSpecialityDescription)).commit();

        }

    }


    private void loadClinic() {
        presenter.callGetAllClinics(SharedPreferencesUtils.getInstance(this).getValue(Constants.KEY_ACCESS_TOKEN, ""), "", "", "", Constants.RECORD_SET
                , String.valueOf(1), SharedPreferencesUtils.getInstance(this).getValue(Constants.SELECTED_HOSPITAL, 0));
    }

    @Override
    public void onGetClinicsList(ClinicResponse response) {
        String lang = SharedPreferencesUtils.getInstance(FindDoctorActivity.this).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        list.addAll(response.getSpecialityList());

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(getString(R.string.all));
        for (int i = 0; i < list.size(); i++) {
            if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
                arrayList.add(list.get(i).getSpecialityDescription());
            } else {
                arrayList.add(list.get(i).getArabicSpecialityDescription());

            }

        }

        ArrayAdapter<String> a = new ArrayAdapter<String>(FindDoctorActivity.this, R.layout.spinner_item, arrayList);

        clinic_spinner.setAdapter(a);

        loadDoctor();
    }


    @Override
    public void onGetSearchClinicsList(ClinicResponse response) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onFail(String msg) {

    }

    @Override
    public void onNoInternet() {

    }

}