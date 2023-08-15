package sa.med.imc.myimc.Managebookings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormIdNameModel implements Serializable {
    String formId;
    String formName;
    String assessmentId;
    String hosp;

    public FormIdNameModel() {
    }

    public FormIdNameModel(String formId, String formName, String assessmentId, String hosp) {
        this.formId = formId;
        this.formName = formName;
        this.assessmentId = assessmentId;
        this.hosp = hosp;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }
}
