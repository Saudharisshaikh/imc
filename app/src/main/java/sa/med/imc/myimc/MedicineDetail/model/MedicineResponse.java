package sa.med.imc.myimc.MedicineDetail.model;

import java.util.List;

public class MedicineResponse {

    String status;
    List<MedicineModel> medication_list;

    public MedicineResponse() {
    }

    public MedicineResponse(String status, List<MedicineModel> medication_list) {
        this.status = status;
        this.medication_list = medication_list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MedicineModel> getMedication_list() {
        return medication_list;
    }

    public void setMedication_list(List<MedicineModel> medication_list) {
        this.medication_list = medication_list;
    }
}
