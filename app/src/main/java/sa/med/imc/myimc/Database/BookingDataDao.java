package sa.med.imc.myimc.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import sa.med.imc.myimc.Managebookings.model.Booking;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Queries to add, fetch bookings
 */
@Dao
public interface BookingDataDao {

    @Insert(onConflict = REPLACE)
    void saveBookingData(Booking profileData);

    @Query("SELECT * FROM Booking WHERE bookingStatus = :booking_status AND MRN=:mrn")
    List<Booking> loadBookingData(String booking_status, String mrn);

    @Query("UPDATE  Booking SET bookingStatus=:booking_status WHERE id = :id AND MRN=:mrn")
    void sentStatus(String booking_status, String id, String mrn);

    @Query("DELETE  FROM Booking")
    void deleteData();


}
