package sa.med.imc.myimc.HealthSummary.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllergyResponse {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("allergy_list")
    @Expose
    private List<AllergyModel> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllergyModel> getData() {
        return data;
    }

    public void setData(List<AllergyModel> data) {
        this.data = data;
    }


    public class AllergyModel {

        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("allergy_code")
        @Expose
        String allergy_code;

        @SerializedName("allergy_description")
        @Expose
        String allergy_description;

        @SerializedName("allergy_date")
        @Expose
        String allergy_date;

        @SerializedName("severity")
        @Expose
        String severity;

        @SerializedName("nature_of_reaction")
        @Expose
        String nature_of_reaction;

        public AllergyModel() {
        }

        public AllergyModel(String id, String allergy_code, String allergy_description, String allergy_date, String severity, String nature_of_reaction) {
            this.id = id;
            this.allergy_code = allergy_code;
            this.allergy_description = allergy_description;
            this.allergy_date = allergy_date;
            this.severity = severity;
            this.nature_of_reaction = nature_of_reaction;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAllergy_code() {
            return allergy_code;
        }

        public void setAllergy_code(String allergy_code) {
            this.allergy_code = allergy_code;
        }

        public String getAllergy_description() {
            return allergy_description;
        }

        public void setAllergy_description(String allergy_description) {
            this.allergy_description = allergy_description;
        }

        public String getAllergy_date() {
            return allergy_date;
        }

        public void setAllergy_date(String allergy_date) {
            this.allergy_date = allergy_date;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getNature_of_reaction() {
            return nature_of_reaction;
        }

        public void setNature_of_reaction(String nature_of_reaction) {
            this.nature_of_reaction = nature_of_reaction;
        }
    }

}
