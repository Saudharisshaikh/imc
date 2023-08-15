package sa.med.imc.myimc.Notifications.view;


import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Notifications.model.NotificationResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Notifications views interface to handle UI in Notifications Activity.
 */
public interface NotificationViews extends Loading {

    void onGetCount(int count);

    void onSuccess(NotificationResponse response);

    void onDelete(SimpleResponse response);

    void onClearAll(SimpleResponse response);

    void onRead(SimpleResponse response);

}
