package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.Records.model.OperativeReportOldModel;

public interface OperativeOldListner {
    void onSuccess(OperativeReportOldModel operativeReportOldModel);
    void onFaild();
}
