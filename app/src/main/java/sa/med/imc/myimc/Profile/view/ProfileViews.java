package sa.med.imc.myimc.Profile.view;


import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Profile.model.LastBookingResponse;
import sa.med.imc.myimc.Profile.model.MedicationRespone;
import sa.med.imc.myimc.Profile.model.ProfileResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Profile views interface to handle UI in Login Activity.
 */
public interface ProfileViews extends Loading {

    void onGetProfile(ProfileResponse response);

    void onGetMedication(MedicationRespone response);

    void onUpdateProfile(SimpleResponse response);

}
