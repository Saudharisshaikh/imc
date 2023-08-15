package sa.med.imc.myimc.Questionaires.presenter;


import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;

/*
 * Assessment Presenter interface to handle UI in Assessment Activity.
 */
public interface AssessmentPresenter {

    //TODO: Validate hosp changes
    void callGetNextAssessment(String token, String id, String itemOID, String response,int hosp);

    void callGetListCompletedAssessment(String token, String patId, String item_count, String page, int hosp);

    void callGetFirstQuestion(String token,String mrn, BookingAppoinment.PromisForm promisForm1, BookingAppoinment booking1);


}
