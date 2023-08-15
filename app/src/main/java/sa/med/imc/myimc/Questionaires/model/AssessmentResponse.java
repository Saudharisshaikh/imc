package sa.med.imc.myimc.Questionaires.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AssessmentResponse {


    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("assessmentId")
    String assessmentId;

    @SerializedName("assessment")
    AssessmentModel assessment;


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessment(AssessmentModel assessment) {
        this.assessment = assessment;
    }

    public AssessmentModel getAssessment() {
        return assessment;
    }


}
