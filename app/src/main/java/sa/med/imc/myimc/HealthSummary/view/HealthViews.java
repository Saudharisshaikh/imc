package sa.med.imc.myimc.HealthSummary.view;


import sa.med.imc.myimc.HealthSummary.model.AllergyResponse;
import sa.med.imc.myimc.HealthSummary.model.ReadingResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Health views interface to handle UI in Health Fragment.
 */
public interface HealthViews extends Loading {

    void onSuccess(AllergyResponse response);
    void onSuccessReadings(ReadingResponse response);

}
