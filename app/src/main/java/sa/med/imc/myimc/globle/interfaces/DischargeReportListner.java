package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.DischargeModel;

public interface DischargeReportListner {
    void onSuccess(List<DischargeModel> dischargeModelList);
    void onFailed();

}
