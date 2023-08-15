package sa.med.imc.myimc.Base;

import androidx.fragment.app.Fragment;

import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.Loading;

public abstract class BaseFragment  extends Fragment implements Loading {


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

    }

    @Override
    public void onNoInternet() {

    }

    protected FragmentListener getCallBack(){
        return ((FragmentListener)requireActivity());
    }

    protected abstract Runnable onBackPressedAction();
}
