package sa.med.imc.myimc.Physicians.interfaces;

import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;

public interface GetTimeSlot {
    void onGetTimeSlot(DrTimeSlot drTimeSlot);
    void onFail(String msg);

}
