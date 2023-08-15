package sa.med.imc.myimc.Base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.Loading;
import timber.log.Timber;

public class BaseBottomSheet extends BottomSheetDialogFragment implements Loading {
    protected BottomSheetBehavior<FrameLayout> behavior;
    protected final Timber.Tree tree = new Timber.DebugTree(){
        @Nullable
        @Override
        protected String createStackElementTag(@NonNull StackTraceElement element) {
            Class<?> k = BaseBottomSheet.this.getClass();
            return k!=null ? k.getSimpleName() : super.createStackElementTag(element);

        }

        @Override
        protected void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t) {
            if (BuildConfig.DEBUG)
                super.log(priority, tag, message, t);
        }
    };

    private final BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @Override
    public void onPause() {
        super.onPause();
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @Override
    public void onStop() {
        super.onStop();
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @Override
    public void onStart() {
        super.onStart();
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @Override
    public void onResume() {
        super.onResume();
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setOnShowListener(d -> {
            behavior = dialog.getBehavior();
            dialog.getBehavior().addBottomSheetCallback(bottomSheetCallback);
        });
        return dialog;
    }

    protected void setState (int state){
        if (behavior!=null)
            behavior.setState(state);
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;

    }

    @Override
    public void showLoading() {
        Common.showDialog(requireContext());
    }

    @Override
    public void hideLoading() {
        Common.hideDialog();
    }

    @Override
    public void onFail(String msg) {
        Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoInternet() {
        Toast.makeText(requireContext(),getString(R.string.network_content),Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        tree.v(new Throwable().getStackTrace()[0].getMethodName());
    }
}
