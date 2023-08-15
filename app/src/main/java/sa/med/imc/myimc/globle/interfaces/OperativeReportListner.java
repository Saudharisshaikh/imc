package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.OperativeModel;

public interface OperativeReportListner {
    void onSuccess(List<OperativeModel> operativeModelList);
    void onFailed();
}
