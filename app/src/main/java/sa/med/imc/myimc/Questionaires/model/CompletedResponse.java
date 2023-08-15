package sa.med.imc.myimc.Questionaires.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletedResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName(value = "assessments",alternate = "promisFormAssessmentList")
    @Expose
    private List<Assessment> assessments = null;
    @SerializedName("total_items")
    @Expose
    private String totalItems;
    @SerializedName("total_pages")
    @Expose
    private String totalPages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public class Assessment {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("formId")
        @Expose
        private String formId;
        @SerializedName("patId")
        @Expose
        private String patId;
        @SerializedName("episode")
        @Expose
        private String episode;
        @SerializedName("assessmentId")
        @Expose
        private String assessmentId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("result")
        @Expose
        private String result;
        @SerializedName("thetaValue")
        @Expose
        private String thetaValue;
        @SerializedName("stdError")
        @Expose
        private String stdError;
        @SerializedName("formName")
        @Expose
        private String formName;
        @SerializedName("updateDateTime")
        @Expose
        private String updateDateTime;

        public String getUpdateDateTime() {
            return updateDateTime;
        }

        public void setUpdateDateTime(String updateDateTime) {
            this.updateDateTime = updateDateTime;
        }

        public String getFormName() {
            return formName;
        }

        public void setFormName(String formName) {
            this.formName = formName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getEpisode() {
            return episode;
        }

        public void setEpisode(String episode) {
            this.episode = episode;
        }

        public String getAssessmentId() {
            return assessmentId;
        }

        public void setAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getThetaValue() {
            return thetaValue;
        }

        public void setThetaValue(String thetaValue) {
            this.thetaValue = thetaValue;
        }

        public String getStdError() {
            return stdError;
        }

        public void setStdError(String stdError) {
            this.stdError = stdError;
        }

    }

    ;

}
