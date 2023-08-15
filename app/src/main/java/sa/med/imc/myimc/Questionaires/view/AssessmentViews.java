package sa.med.imc.myimc.Questionaires.view;


import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.Questionaires.model.AssessmentModel;
import sa.med.imc.myimc.Questionaires.model.AssessmentResponse;
import sa.med.imc.myimc.Questionaires.model.CompletedResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Assessment views interface to handle UI in Assessment Activity.
 */
public interface AssessmentViews extends Loading {

    void onGetAssessmentFirst(AssessmentResponse response);

    void onGetAssessmentNext(AssessmentResponse response);

    void onGetListAssessments(CompletedResponse response);

}
