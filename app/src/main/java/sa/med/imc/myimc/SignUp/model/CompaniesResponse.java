package sa.med.imc.myimc.SignUp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import sa.med.imc.myimc.SignUp.activity.SelfRegistrationActivity;

public class CompaniesResponse {
    public String status;
    public List<InsuranceList> insurance_list;

    public CompaniesResponse() {
    }

    public CompaniesResponse(String status, List<InsuranceList> insurance_list) {
        this.status = status;
        this.insurance_list = insurance_list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<InsuranceList> getInsurance_list() {
        return insurance_list;
    }

    public void setInsurance_list(List<InsuranceList> insurance_list) {
        this.insurance_list = insurance_list;
    }

    public class InsuranceList {
        public String insurance_code;
        public String insurance_description;
        public String date_from;

        public InsuranceList(String insurance_code, String insurance_description, String date_from) {
            this.insurance_code = insurance_code;
            this.insurance_description = insurance_description;
            this.date_from = date_from;
        }

        public InsuranceList() {
        }

        public String getInsurance_code() {
            return insurance_code;
        }

        public void setInsurance_code(String insurance_code) {
            this.insurance_code = insurance_code;
        }

        public String getInsurance_description() {
            return insurance_description;
        }

        public void setInsurance_description(String insurance_description) {
            this.insurance_description = insurance_description;
        }

        public String getDate_from() {
            return date_from;
        }

        public void setDate_from(String date_from) {
            this.date_from = date_from;
        }
    }


}
