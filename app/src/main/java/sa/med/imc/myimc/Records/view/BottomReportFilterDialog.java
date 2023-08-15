package sa.med.imc.myimc.Records.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Bottom sheet dialog to filter reports by date range.
 */

public class BottomReportFilterDialog extends BottomSheetDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.select_from_date)
    TextView selectFromDate;
    @BindView(R.id.select_to_date)
    TextView selectToDate;
    @BindView(R.id.title_cardio)
    TextView title_cardio;
    String from = "", to = "";

    private BottomDailogListener mDialogListener;


    @NonNull
    public static BottomReportFilterDialog newInstance() {
        BottomReportFilterDialog astro = new BottomReportFilterDialog();
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
        View view = inflater.inflate(R.layout.bottom_sheet_reports_filters, container, false);

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        ButterKnife.bind(this, view);

        selectFromDate.setText(from);
        selectToDate.setText(to);

        if (getArguments() != null) {
            if (getArguments().containsKey("card"))
                title_cardio.setVisibility(View.VISIBLE);
        }

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


    @OnClick({R.id.iv_close, R.id.iv_clear, R.id.select_to_date, R.id.select_from_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:

                from = selectFromDate.getText().toString();
                to = selectToDate.getText().toString();
                mDialogListener.onDone(from, to);
                dismiss();

                break;

            case R.id.iv_clear:
                from = "";
                to = "";
                mDialogListener.onClear("clear");
                dismiss();

                break;

            case R.id.select_from_date:
                Common.getDateFromPicker(selectFromDate, getContext());
                break;

            case R.id.select_to_date:
                if (selectFromDate.getText().toString().trim().length() > 0)
                    Common.getDateToPicker(selectToDate, getActivity(), selectFromDate.getText().toString());
                else
                    Common.makeToast(getActivity(), getString(R.string.select_from));

                break;
        }
    }

    public interface BottomDailogListener {
        void onDone(String from, String to);

        void onClear(String text);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
