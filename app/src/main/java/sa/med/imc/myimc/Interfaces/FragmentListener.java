package sa.med.imc.myimc.Interfaces;

import java.io.Serializable;

import sa.med.imc.myimc.Base.BaseFragment;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.webView.model.WebViewRequest;

public interface FragmentListener {

    void openProfileMedicineFragment(String value);

    void openPhysicianFragment(String value, String clinic_id);

    void openClinicFragment(String value);

    void openPaymentInfoFragment(Serializable value,String key);

    void openTelrFragment(Serializable book,Serializable res, String value);

    void openMedicineFragment(String type,String value);

    void backPressed(String type);

    void openLMSRecordFragment(String value,String type,String episodeId);

    void startTask(int time);

    void openHealthSummary(String value);

    void openFitness(String value);

    void openWellness(String value);

    void openBodyMeasurement(String value);

    void openActivity(String value);

    void openSleepCycle(String value);

    void openHeatAndVitals(String value);


    void openAllergies(String value);

    void openVitalSigns(String value);

    void checkLocation();

    default void openHome(){

    }

    default void openPharmacyFragment(String rxNo,String episodeNo){

    }

    default void onWebView(WebViewRequest request){

    }

    default void showCenterIcon(){

    }

    default void hideCenterIcon(){

    }

    default void showNavigation(){

    }

    default void hideNavigation(){

    }


    default void showToolbar(){

    }

    default void hideToolbar(){

    }

    default void openFragment(BaseFragment fragment) {

    }
}
