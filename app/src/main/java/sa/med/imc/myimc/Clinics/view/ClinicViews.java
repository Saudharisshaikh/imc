package sa.med.imc.myimc.Clinics.view;


import sa.med.imc.myimc.Clinics.model.ClinicResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Clinics views interface to handle UI in Clinics Fragment.
 */
public interface ClinicViews extends Loading {

    void onGetClinicsList(ClinicResponse response);
    void onGetSearchClinicsList(ClinicResponse response);

}
