package sa.med.imc.myimc.Settings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReportTypeResponse {
    public ArrayList<ReportTypeList> report_type_list;
    public String status;


    public class ReportTypeList{
        public String report_code;
        public String report_description;

        public ReportTypeList() {
        }

        public ReportTypeList(String report_code, String report_description) {
            this.report_code = report_code;
            this.report_description = report_description;
        }

        public String getReport_code() {
            return report_code;
        }

        public void setReport_code(String report_code) {
            this.report_code = report_code;
        }

        public String getReport_description() {
            return report_description;
        }

        public void setReport_description(String report_description) {
            this.report_description = report_description;
        }
    }

    public ReportTypeResponse() {
    }

    public ReportTypeResponse(ArrayList<ReportTypeList> report_type_list, String status) {
        this.report_type_list = report_type_list;
        this.status = status;
    }

    public ArrayList<ReportTypeList> getReport_type_list() {
        return report_type_list;
    }

    public void setReport_type_list(ArrayList<ReportTypeList> report_type_list) {
        this.report_type_list = report_type_list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
