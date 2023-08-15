package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.SickLeave;

public interface SickLeaveListner {
    void onSuccess(List<SickLeave> sickLeaveList);
    void onFaild();
}
