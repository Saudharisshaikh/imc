package sa.med.imc.myimc.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import sa.med.imc.myimc.BuildConfig;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.MedicineDetail.model.MedicineReminderModel;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.SickLeave;
import sa.med.imc.myimc.Records.model.SmartLabReport;
import sa.med.imc.myimc.Utils.Converters;


/**
 * Imc local database load when no internet.
 */

@Database(entities = {Booking.class, MedicineReminderModel.class},
        version = BuildConfig.DatabaseVersion, exportSchema = false)
// live app have 3 version
// debug build have 4 version and 2-3, 3-4 migration
@TypeConverters({Converters.class})
public abstract class ImcDatabase extends RoomDatabase {

    public abstract BookingDataDao bookingDataDao();

//    public abstract LabReportDataDao labReportDataDao();

/*  public abstract SmartLabReportDataDao smartLabReportDataDao();*/

//    public abstract MedicalReportDataDao medicalReportDataDao();

//    public abstract SickLeaveDataDao sickLeaveDataDao();

    public abstract MedicationReminderDataDao medicationReminderDataDao();

}
