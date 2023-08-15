package sa.med.imc.myimc.Profile.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Adapter.DependentMrnsAdapter;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;


/**
 * Bottom sheet dialog to view dependents .
 */

public class BottomDependentListDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rv_mrn)
    RecyclerView rvMrn;
    DependentMrnsAdapter adapter;
    String from = "", to = "";
    ProfileResponse response;
    private BottomDailogListener mDialogListener;


    @NonNull
    public static BottomDependentListDialog newInstance(ProfileResponse response) {
        BottomDependentListDialog astro = new BottomDependentListDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", response);
        astro.setArguments(bundle);
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
        View view = inflater.inflate(R.layout.bottom_sheet_mrn_list, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);
        toolbarTitle.setText(getString(R.string.dependent_list));
        response = (ProfileResponse) getArguments().getSerializable("data");

        if (response != null) {
            setUp(response);
        }
        return view;
    }

    void setUp(ProfileResponse response) {
        adapter = new DependentMrnsAdapter(getActivity(), response.getDepandant());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvMrn.setLayoutManager(mLayoutManager);
        rvMrn.setAdapter(adapter);

        rvMrn.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialogListener.onDone(response.getDepandant().get(position).getMrn(), position);
                dismiss();
            }
        }));
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


    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_close) {
//            mDialogListener.onDone(response.getUserDetails().getMrnumber(), -1);
            dismiss();
        }
    }

    public interface BottomDailogListener {
        void onDone(String MRN, int pos);

        void onClear(String text);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
