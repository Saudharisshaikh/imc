package sa.med.imc.myimc.Notifications.presenter;


/*
 * Login Presenter interface to handle UI in Login Activity.
 */
public interface NotificationPresenter {

    void callGetNotificationsCount(String token, String mrNumber, int hosp);

    void callGetNotifications(String token, String mrNumber, String itemCount, String page, int hosp);

    void callDeleteNotification(String token, String notification_id, int hosp);

    void callClearAllNotification(String token, String MRN, int hosp);

    void callReadNotification(String token, String MRN, String not_id, int hosp);

}
