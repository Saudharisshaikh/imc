package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.Records.model.LabReportResponse;

public interface LabreportListner {

    void onSuccess(LabReportResponse labReportResponse);
    void onFailed();
}
