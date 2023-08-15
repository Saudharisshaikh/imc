package sa.med.imc.myimc.MedicineDetail.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

@Entity
public class MedicineReminderModel {

    @PrimaryKey
    @NonNull
    private String med_dispense_id;
    private String patient_name;

    private String medication_name;
    private String start_date;
    private String end_date;
    private String day_time_one;
    private String day_time_two;
    private String day_time_three;

    private boolean is_on_going = false;
    private boolean is_reminder = false;

    //New added
    private int hours;
    private String freq;
    private String schedule_time;
    private String freq_desp;
    private int total_count_of_notifications;
    private String last_sent_date;
    private String last_sent_time;

    private String week_days;
    private String day_time_four;

    public MedicineReminderModel() {
        med_dispense_id = "0";
    }

    @NotNull
    public String getMed_dispense_id() {
        return med_dispense_id;
    }

    public void setMed_dispense_id(@NotNull String med_dispense_id) {
        this.med_dispense_id = med_dispense_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getMedication_name() {
        return medication_name;
    }

    public void setMedication_name(String medication_name) {
        this.medication_name = medication_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDay_time_one() {
        return day_time_one;
    }

    public void setDay_time_one(String day_time_one) {
        this.day_time_one = day_time_one;
    }

    public String getDay_time_two() {
        return day_time_two;
    }

    public void setDay_time_two(String day_time_two) {
        this.day_time_two = day_time_two;
    }

    public String getDay_time_three() {
        return day_time_three;
    }

    public void setDay_time_three(String day_time_three) {
        this.day_time_three = day_time_three;
    }

    public boolean isIs_on_going() {
        return is_on_going;
    }

    public void setIs_on_going(boolean is_on_going) {
        this.is_on_going = is_on_going;
    }

    public boolean isIs_reminder() {
        return is_reminder;
    }

    public void setIs_reminder(boolean is_reminder) {
        this.is_reminder = is_reminder;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public int getTotal_count_of_notifications() {
        return total_count_of_notifications;
    }

    public void setTotal_count_of_notifications(int total_count_of_notifications) {
        this.total_count_of_notifications = total_count_of_notifications;
    }

    public String getLast_sent_time() {
        return last_sent_time;
    }

    public void setLast_sent_time(String last_sent_time) {
        this.last_sent_time = last_sent_time;
    }

    public String getWeek_days() {
        return week_days;
    }

    public void setWeek_days(String week_days) {
        this.week_days = week_days;
    }

    public String getDay_time_four() {
        return day_time_four;
    }

    public void setDay_time_four(String day_time_four) {
        this.day_time_four = day_time_four;
    }

    public String getLast_sent_date() {
        return last_sent_date;
    }

    public void setLast_sent_date(String last_sent_date) {
        this.last_sent_date = last_sent_date;
    }

    public String getFreq_desp() {
        return freq_desp;
    }

    public void setFreq_desp(String freq_desp) {
        this.freq_desp = freq_desp;
    }
}
