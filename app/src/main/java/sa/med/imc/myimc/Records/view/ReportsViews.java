package sa.med.imc.myimc.Records.view;


import java.util.List;

import sa.med.imc.myimc.Records.model.CardioReportResponse;
import sa.med.imc.myimc.Records.model.LabReportResponse;
import sa.med.imc.myimc.Records.model.LabReportsParentMedicus;
import sa.med.imc.myimc.Records.model.MedicalResponse;
import sa.med.imc.myimc.Records.model.OperativeResponse;
import sa.med.imc.myimc.Records.model.PinResponse;
import sa.med.imc.myimc.Records.model.SickLeaveResponse;
import sa.med.imc.myimc.Records.model.SmartLabReport;
import sa.med.imc.myimc.Records.model.SmartLabReportResponse;
import sa.med.imc.myimc.Utils.Loading;

import okhttp3.Headers;
import okhttp3.ResponseBody;

/*
 * Reports views interface to handle UI in Records fragment.
 */
public interface ReportsViews extends Loading {

    void onGetLabReports(LabReportResponse response);
    void onGetMedicalReports(MedicalResponse response);
    void onGetSickLeaveReports(SickLeaveResponse response);
    void onGetOperativeReports(OperativeResponse response);
    void onGetCardioReports(CardioReportResponse reportResponse);

    void onGetSearchLabReports(LabReportResponse response);
    void onGetSearchMedicalReports(MedicalResponse response);

    void onGenerateMedicalPdf(ResponseBody response, Headers headers);
    void onGenerateLabPdf(ResponseBody response, Headers headers);
    void onGeneratePaymentPdf(ResponseBody response, Headers headers);

    // added by @Pm
    void onGetLabReportsMedicus(List<LabReportsParentMedicus> response, int totalItems);
    void onGetPin(PinResponse response);

    /*
    * author: rohit
    * Purpose of the code: Below function is used for smart report purpose
    * */
    void onGenerateSmartReport(ResponseBody response, Headers headers);
}
