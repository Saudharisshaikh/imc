package sa.med.imc.myimc.Records.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabReportsParentMedicus {
    private String givenName;
    private String orderNum;
    private String aptTime;
    private List<LabReportsMedicus> lstOfChildMedicus;

    public String getAptTime() {
        return aptTime;
    }

    public void setAptTime(String aptTime) {
        this.aptTime = aptTime;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public List<LabReportsMedicus> getLstOfChildMedicus() {
        return lstOfChildMedicus;
    }

    public void setLstOfChildMedicus(List<LabReportsMedicus> lstOfChildMedicus) {
        this.lstOfChildMedicus = lstOfChildMedicus;
    }
}
