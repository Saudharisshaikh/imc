package sa.med.imc.myimc.AddGuardian.view;


import sa.med.imc.myimc.AddGuardian.model.GuardianResponse;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Guardian views interface to handle UI in Guardian Activity.
 */
public interface GuardianViews extends Loading {

    void onGetGuardian(GuardianResponse response);
    void onAddGuardian(SimpleResponse response);
    void onRemove(SimpleResponse response);
    void onLinkUnlink(SimpleResponse response,int active);
    void onUpdateGuardian(GuardianResponse response);

}
