package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.Records.model.DischargeOldModel;

public interface DischageOdListner {

    void onSuccess(DischargeOldModel dischargeOldModel);
    void onFailed();
}
