package sa.med.imc.myimc.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sa.med.imc.myimc.Managebookings.model.Booking;
import sa.med.imc.myimc.MedicineDetail.model.MedicineReminderModel;
import sa.med.imc.myimc.Records.model.LabReport;
import sa.med.imc.myimc.Records.model.MedicalReport;
import sa.med.imc.myimc.Records.model.SickLeave;


import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Converter class
 */

public class Converters {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Booking> stringToBookingDataList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Booking>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String bookingDataListToString(List<Booking> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }

    @TypeConverter
    public static List<SickLeave> stringToSickLeaveDataList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SickLeave>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String sickLeaveListToString(List<SickLeave> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }


    @TypeConverter
    public static List<MedicalReport> stringToMedicalReportDataList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<MedicalReport>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String medicalReportListToString(List<MedicalReport> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }


    @TypeConverter
    public static List<LabReport> stringTolabReportDataList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<LabReport>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String labReportListToString(List<LabReport> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }

    @TypeConverter
    public static String medicationReminderListToString(List<MedicineReminderModel> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }

    @TypeConverter
    public static List<MedicineReminderModel> stringToMedicationReminderDataList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<MedicineReminderModel>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }


}