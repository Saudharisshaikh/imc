package sa.med.imc.myimc.Physicians;

import static sa.med.imc.myimc.Base.BaseActivity.hideKeyboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Clinics.presenter.ClinicPresenter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.CustomTypingEditText;

public class AllDoctorBeforeLoginActivity extends AppCompatActivity {

    RecyclerView rv_physicians;
    ImageView iv_back;

    LinearLayout filter_layout;
    boolean edit = false;
    ImageView ivSearch;
    CustomTypingEditText edSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctor_before_login);
        initView();

    }

    private void initView() {
        rv_physicians = findViewById(R.id.rv_physicians);
        iv_back = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);
        edSearch = findViewById(R.id.ed_search);
        filter_layout = findViewById(R.id.filter_layout);

        iv_back.setOnClickListener(view -> finish());


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
                hideKeyboard(AllDoctorBeforeLoginActivity.this);
//                page = 0;
//                callAPI(page);
                Intent search = new Intent(Constants.physician_search);
                search.putExtra("key", edSearch.getText().toString().trim());
                LocalBroadcastManager.getInstance(this).sendBroadcast(search);
                return true;
            }
            return false;
        });
        FragmentManager manager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
        Log.e("abcd","befor login");
        transaction.replace(R.id.mainframe, new AllDoctorBeforeLoginFragment(AllDoctorBeforeLoginActivity.this,true)).commit();
    }
}