package sa.med.imc.myimc.Managebookings.view;

import android.widget.Button;

import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;

public interface AppoinmentOnclick {
    void onPay(BookingAppoinment booking);
    void onCancel(BookingAppoinment booking);
    void videoCall(int position, String DrName);
}
