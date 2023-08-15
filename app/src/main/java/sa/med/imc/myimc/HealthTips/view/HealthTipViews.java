package sa.med.imc.myimc.HealthTips.view;


import sa.med.imc.myimc.HealthTips.model.HealthTipsResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * HealthTip views interface to handle UI in Health Tips.
 */
public interface HealthTipViews extends Loading {

    void onSuccess(HealthTipsResponse response);

}
