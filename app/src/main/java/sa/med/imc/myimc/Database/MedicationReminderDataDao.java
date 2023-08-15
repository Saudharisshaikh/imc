package sa.med.imc.myimc.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sa.med.imc.myimc.MedicineDetail.model.MedicineReminderModel;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Medication Reminder data
 */
@Dao
public interface MedicationReminderDataDao {


    @Insert(onConflict = REPLACE)
    void saveMedReminderData(MedicineReminderModel data);

    @Query("SELECT * FROM MedicineReminderModel WHERE is_on_going AND is_reminder")
    List<MedicineReminderModel> loadMedReminderData();

    @Query("SELECT * FROM MedicineReminderModel WHERE med_dispense_id=:med_dispense_id")
    MedicineReminderModel getSelectMedReminder(String med_dispense_id);

    @Query("SELECT * FROM MedicineReminderModel WHERE med_dispense_id=:med_dispense_id AND is_on_going AND is_reminder")
    MedicineReminderModel isReminder(String med_dispense_id);

    @Query("UPDATE  MedicineReminderModel SET is_reminder=:is_reminder,is_on_going=:is_on_going WHERE med_dispense_id=:med_dispense_id")
    void updateStatusOnly(boolean is_reminder, boolean is_on_going, String med_dispense_id);

    @Query("UPDATE  MedicineReminderModel SET last_sent_date=:last_sent_date,last_sent_time=:last_sent_time WHERE med_dispense_id=:med_dispense_id")
    void updateLastSentDate(String last_sent_date, String last_sent_time, String med_dispense_id);

    @Query("DELETE  FROM MedicineReminderModel WHERE med_dispense_id=:med_dispense_id")
    void deleteData(String med_dispense_id);
}
