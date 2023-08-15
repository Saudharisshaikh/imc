package sa.med.imc.myimc.Managebookings.view;


import sa.med.imc.myimc.Managebookings.model.VisitDetailReportResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Visit views interface to handle UI in VisitDetail Activity.
 */
public interface VisitDetailViews extends Loading {

    void onGetReportsCount(VisitDetailReportResponse response);

}
