package sa.med.imc.myimc.Physicians.view;


import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.NextTimeResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Physician views interface to handle UI in Physician Fragment.
 */
public interface PhysicianViews extends Loading {

    void onGetPhysicianList(PhysicianResponse response);

    void onGetServiceList(DrServiceResponse response);

    void onGetPhysicianProfile(PhysicianDetailResponse response);

    void onGetCMSPhysician(PhysicianCompleteDetailCMSResponse response);

    void onGetNextAvailableTIme(NextTimeResponse response,int pos);


}
