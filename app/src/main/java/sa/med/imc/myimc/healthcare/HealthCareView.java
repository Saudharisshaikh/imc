package sa.med.imc.myimc.healthcare;

import java.util.List;

import sa.med.imc.myimc.Utils.Loading;
import sa.med.imc.myimc.healthcare.model.HealthCareItem;

public interface HealthCareView extends Loading {
    void onHealthCareListResponse(List<HealthCareItem> list);

    void onHealthCareSubmission();
}
