package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Records.model.CardiologyModel;

public interface CardiologyListner {
    void onSuccess(List<CardiologyModel> cardiologyModelList);
    void onFailed();
}
