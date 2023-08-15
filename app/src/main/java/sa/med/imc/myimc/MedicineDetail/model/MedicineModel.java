package sa.med.imc.myimc.MedicineDetail.model;

import java.io.Serializable;

public class MedicineModel implements Serializable {


    String oldData;
    String id;
    String episode_number;
    String medication_description;
    String order_start_date;
    String order_start_time;
    String prescription_number;
    String quantity;
    String route;
    String duration;
    String order_status;
    String order_priority;
    String dispensed_date;
    String dispensed_time;
    String english_instruction;
    String arabic_instruction;

    public MedicineModel() {
    }

    public MedicineModel(String oldData, String id, String episode_number, String medication_description, String order_start_date, String order_start_time, String prescription_number, String quantity, String route, String duration, String order_status, String order_priority, String dispensed_date, String dispensed_time, String english_instruction, String arabic_instruction) {
        this.oldData = oldData;
        this.id = id;
        this.episode_number = episode_number;
        this.medication_description = medication_description;
        this.order_start_date = order_start_date;
        this.order_start_time = order_start_time;
        this.prescription_number = prescription_number;
        this.quantity = quantity;
        this.route = route;
        this.duration = duration;
        this.order_status = order_status;
        this.order_priority = order_priority;
        this.dispensed_date = dispensed_date;
        this.dispensed_time = dispensed_time;
        this.english_instruction = english_instruction;
        this.arabic_instruction = arabic_instruction;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(String episode_number) {
        this.episode_number = episode_number;
    }

    public String getMedication_description() {
        return medication_description;
    }

    public void setMedication_description(String medication_description) {
        this.medication_description = medication_description;
    }

    public String getOrder_start_date() {
        return order_start_date;
    }

    public void setOrder_start_date(String order_start_date) {
        this.order_start_date = order_start_date;
    }

    public String getOrder_start_time() {
        return order_start_time;
    }

    public void setOrder_start_time(String order_start_time) {
        this.order_start_time = order_start_time;
    }

    public String getPrescription_number() {
        return prescription_number;
    }

    public void setPrescription_number(String prescription_number) {
        this.prescription_number = prescription_number;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_priority() {
        return order_priority;
    }

    public void setOrder_priority(String order_priority) {
        this.order_priority = order_priority;
    }

    public String getDispensed_date() {
        return dispensed_date;
    }

    public void setDispensed_date(String dispensed_date) {
        this.dispensed_date = dispensed_date;
    }

    public String getDispensed_time() {
        return dispensed_time;
    }

    public void setDispensed_time(String dispensed_time) {
        this.dispensed_time = dispensed_time;
    }

    public String getEnglish_instruction() {
        return english_instruction;
    }

    public void setEnglish_instruction(String english_instruction) {
        this.english_instruction = english_instruction;
    }

    public String getArabic_instruction() {
        return arabic_instruction;
    }

    public void setArabic_instruction(String arabic_instruction) {
        this.arabic_instruction = arabic_instruction;
    }
}
