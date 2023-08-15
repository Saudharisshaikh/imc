package sa.med.imc.myimc.Questionaires.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import sa.med.imc.myimc.Managebookings.model.FormIdNameModel;

public class FormsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("formLists")
    @Expose
    private List<FormIdNameModel> formIdNameModels = null;

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


    public List<FormIdNameModel> getFormIdNameModels() {
        return formIdNameModels;
    }

    public void setFormIdNameModels(List<FormIdNameModel> formIdNameModels) {
        this.formIdNameModels = formIdNameModels;
    }
}
