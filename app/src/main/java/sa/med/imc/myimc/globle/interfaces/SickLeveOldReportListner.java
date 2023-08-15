package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.Records.model.SickReportOldModel;

public interface SickLeveOldReportListner {
    void onSuccess(SickReportOldModel sickReportOldModel);
    void onFaild();
}
