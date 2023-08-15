package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.HealthSummary.model.NewVitalModel;

public interface NewVitalListner {
    void onSuccess(NewVitalModel newVitalModel);
    void onFailed();

}
