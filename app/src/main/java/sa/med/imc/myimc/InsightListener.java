package sa.med.imc.myimc;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ai.medicus.insights.ArchiveInsightEvent;
import ai.medicus.insights.InsightButtonActionType;
import ai.medicus.insights.InsightCardActionType;
import ai.medicus.insights.InsightDetailsActivity;
import ai.medicus.insights.InsightsCallback;
import ai.medicus.insights.ScheduleTestPopup;
import ai.medicus.insights.TooltipDialog;
import ai.medicus.medicuscore.Answer;
import ai.medicus.medicuscore.DoPatientProgramItem;
import ai.medicus.medicuscore.DoctorNote;
import ai.medicus.medicuscore.EngineInsightType;
import ai.medicus.medicuscore.PatientCoachingStep;
import ai.medicus.medicuscore.PatientInsight;
import ai.medicus.medicuscore.ProfileQuestion;
import ai.medicus.medicuscore.WBListInsight;
import ai.medicus.smartreport.biomarker_details.popups.BiomarkerInfoPopup;

public class InsightListener implements InsightsCallback {


    @Override
    public void onInsightClicked(Activity activity, PatientInsight patientInsight,
                                 WBListInsight wbListInsight, String  doctorNote,
                                 InsightCardActionType actionType, String symbol, Integer position) {
        switch (actionType) {
            case INSIGHT_DETAILS:
                InsightDetailsActivity.open(activity, patientInsight, null, null, null, symbol,
                        false,
                        patientInsight.getInsightId() == null ? -1 : patientInsight.getInsightId(), position);
                break;
            case BIOMARKER_INFO:
                BiomarkerInfoPopup.showPopup(symbol, (FragmentActivity) activity);
                break;
            case NOTE:
                if (doctorNote != null) {
                    InsightDetailsActivity.open(activity, patientInsight, wbListInsight,
                            null, doctorNote, symbol,
                            false, 0, position);
                }
                break;
        }
    }




    @Override
    public void onInsightButtonClicked(Activity activity, PatientInsight patientInsight, InsightButtonActionType actionType) {

        EventBus.getDefault().post(new ArchiveInsightEvent());
        switch (actionType) {
            case NO_ACTION:
                break;
            case SCHEDULE:
                boolean isDoctor = patientInsight.getInsightType() == EngineInsightType.DOCTOR_VISIT || patientInsight.getAllVitalsRelatedBiomarkers();
                ScheduleTestPopup scheduleTestPopup = new ScheduleTestPopup(isDoctor, activity, patientInsight);
                scheduleTestPopup.show(((FragmentActivity) activity).getSupportFragmentManager(), "");
                break;
        }
    }

    @Override
    public void onInsightSwiped(Activity activity, PatientInsight patientInsight) {
    }

    @Override
    public void onIconClicked(Activity activity, PatientInsight patientInsight) {
        TooltipDialog.show((FragmentActivity) activity, patientInsight);
    }

    @Override
    public void onCancelDoctorAppointment(Activity activity, PatientInsight patientInsight, Integer position) {
    }

    @Override
    public void onListQuestionAnswered(Activity activity, boolean yesOrNo, String symbol, Integer answerId) {
    }

    @Override
    public void onFamilyHistoryQuestionAnswered(Activity activity, boolean yesOrNo, Answer answer) {
    }

    @Override
    public void onNormalQuestionAnswered(Activity activity, String type, String symbol) {
    }

    @Override
    public void onUnderneathBiomarkerClicked(Activity activity, String biomarkerJson, String symbol, int reportId) {
    }

    @Override
    public void onStepsNeeded(ViewGroup parent) {
    }

    @Override
    public void configureSteps(ArrayList<PatientCoachingStep> steps) {
    }

    @Override
    public void onCoachingProgramsViewHolderNeeded(ViewGroup parent, Activity activity, String category) {
    }

    @Override
    public void configureCoachingPrograms(ArrayList<DoPatientProgramItem> items) {
    }

    @Override
    public void onProgramsHeaderNeeded(ViewGroup parent) {
    }

    @Override
    public void configureProgramsHeader() {
    }

    @Override
    public void onBiomarkerInfoClicked(String symbol, Activity activity) {
        BiomarkerInfoPopup.showPopup(symbol, (FragmentActivity) activity);
    }

    @Override
    public void onAddBiomarkerClicked(String symbol, Activity activity) {
    }

    @Override
    public void onGoToProfileClicked(ProfileQuestion profileQuestion, Activity activity) {
    }

    @Override
    public void onRescheduleAllNotifications() {

    }
}
