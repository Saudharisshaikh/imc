package sa.med.imc.myimc.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.SmartLabReport;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Queries to add, fetch Lab Report
 */
@Dao
public interface SmartLabReportDataDao {


    @Insert(onConflict = REPLACE)
    void saveSamrtLabReportData(SmartLabReport data);

/*    @Query("SELECT * FROM SmartLabReport WHERE MRN=:accessionNum")
    List<SmartLabReport>loadSmartLabReportData(String accessionNum);*/

    @Query("SELECT * FROM SmartLabReport WHERE accessionNum=:accessionNum")
    SmartLabReport getSelectReport(String accessionNum);

//    @Query("SELECT * FROM LabReport WHERE orderno=:orderno ")
//    LabReport getSelectReport(String orderno);

    @Query("UPDATE SmartLabReport SET localPath=:path WHERE accessionNum=:accessionNum")
    void saveSmartLabLocalPath(String path, String accessionNum);

    @Query("DELETE FROM SmartLabReport")
    void deleteData();
}
