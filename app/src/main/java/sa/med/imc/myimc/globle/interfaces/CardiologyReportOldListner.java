package sa.med.imc.myimc.globle.interfaces;

import sa.med.imc.myimc.Records.model.CadiologyReportOldModel;

public interface CardiologyReportOldListner {

    void onSuccess(CadiologyReportOldModel cadiologyReportOldModel);
    void onFailed();
}
