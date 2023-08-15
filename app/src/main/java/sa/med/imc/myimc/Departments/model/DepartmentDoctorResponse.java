package sa.med.imc.myimc.Departments.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import sa.med.imc.myimc.Physicians.model.PhysicianModel;

public class DepartmentDoctorResponse {

    @SerializedName("data")
    @Expose
    private List<PhysicianModel> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<PhysicianModel> getData() {
        return data;
    }

    public void setData(List<PhysicianModel> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
