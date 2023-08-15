package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.LaboratoryHistoryModel;

public interface LabreportOldDataListner {
    void onSuccess(List<LaboratoryHistoryModel> laboratoryHistoryModelList,String total);
    void onFailed();
}
