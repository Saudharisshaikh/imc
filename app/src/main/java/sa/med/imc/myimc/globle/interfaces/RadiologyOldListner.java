package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.RadiologyOldModel;

public interface RadiologyOldListner {
    void onSuccess(List<RadiologyOldModel> radiologyOldModelList,String total_items);
    void onFaild();
}
