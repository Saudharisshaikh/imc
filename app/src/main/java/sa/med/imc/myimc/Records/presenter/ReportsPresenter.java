package sa.med.imc.myimc.Records.presenter;


/*
 * Reports Presenter interface to call API in ReportsImpl.
 */
public interface ReportsPresenter {

    void callGetAllLabReports(String page,String bookingStatus);

    void callGetAllMedicalReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp);

    void callGetAllSickLeaveReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp);

    void callGenerateMedicalPdf(String token, String mr_number, String order_no, String order_no_line, int hosp);

    void callGenerateSickLeavePdf(String token, String mr_number, String leave_id, String with_diagnosis, int hosp);

    void callGeneratePaymentPdf(String token,String mr_number,int hosp,int sessionId,String refId);

    void callGetOperativeReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp);

    void callGetDischargeReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp);

    void callGenerateOperativePdf(String token, String mr_number, String reportId, String reportType, int hosp);

    //TODO: add hop
    void callGenerateSmartLabPdf(String token,String mr_number, String order_no, String language);
    void callGenerateLabPdf(String token, String mr_number, String report_type, String specimen_num, int hosp);

    void callGenerateDischargePdf(String token, String mr_number, String reportId, String reportType, int hosp);

    void callGetCardioReports(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp);

    //TODO: add hop
    void callGenerateCardioPdf(String token, String mr_number, String attendanceId, String report_type,int hosp);


    //TODO: add hop
    //added by Pm
    void callGetAllLabReportsMedicus(String token, String mrn_number, String searc, String episodeId, String item_set, String page, String from, String to, int hosp);
    void callGetPin(String token, String mr_number);


}


