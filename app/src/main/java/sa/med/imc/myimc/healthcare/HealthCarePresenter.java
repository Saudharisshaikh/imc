package sa.med.imc.myimc.healthcare;

import java.util.List;

import sa.med.imc.myimc.Utils.Loading;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;
import sa.med.imc.myimc.healthcare.model.HealthCareListRequest;
import sa.med.imc.myimc.healthcare.model.HealthCareSubmission;

public interface HealthCarePresenter  {

    void onHealthCareRequest();

    void onHealthCareSubmit(String name,String email,String number,List<HealthCareItem> list);
}
