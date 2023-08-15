package sa.med.imc.myimc.globle.interfaces;

import java.util.List;

import sa.med.imc.myimc.Managebookings.model.BookingAppoinment;

public interface AppointmentListner {
    void onSuccess(List<BookingAppoinment> bookingAppoinmentList);

}
