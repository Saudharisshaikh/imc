package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.MedicineDetail.model.MedicineResponse;

public interface MedicineListner {
    void onSuccess(MedicineResponse medicineResponse);
    void onFail();
}
