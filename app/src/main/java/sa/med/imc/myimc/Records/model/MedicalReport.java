package sa.med.imc.myimc.Records.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicalReport {

    String orderNo;
    String orderNoLine;
    String hosp;
    String oldData;
    String order_id;
    String order_description;
    String ordered_doctor;
    String order_status;
    String authorized_user;
    String result_date;
    String result_time;

    public MedicalReport() {
    }

    public MedicalReport(String orderNo, String orderNoLine, String hosp, String oldData, String order_id, String order_description, String ordered_doctor, String ordered_status, String authorized_user, String result_date, String result_time) {
        this.orderNo = orderNo;
        this.orderNoLine = orderNoLine;
        this.hosp = hosp;
        this.oldData = oldData;
        this.order_id = order_id;
        this.order_description = order_description;
        this.ordered_doctor = ordered_doctor;
        this.order_status = ordered_status;
        this.authorized_user = authorized_user;
        this.result_date = result_date;
        this.result_time = result_time;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNoLine() {
        return orderNoLine;
    }

    public void setOrderNoLine(String orderNoLine) {
        this.orderNoLine = orderNoLine;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }

    public String getOrdered_doctor() {
        return ordered_doctor;
    }

    public void setOrdered_doctor(String ordered_doctor) {
        this.ordered_doctor = ordered_doctor;
    }

    public String getOrdered_status() {
        return order_status;
    }

    public void setOrdered_status(String ordered_status) {
        this.order_status = ordered_status;
    }

    public String getAuthorized_user() {
        return authorized_user;
    }

    public void setAuthorized_user(String authorized_user) {
        this.authorized_user = authorized_user;
    }

    public String getResult_date() {
        return result_date;
    }

    public void setResult_date(String result_date) {
        this.result_date = result_date;
    }

    public String getResult_time() {
        return result_time;
    }

    public void setResult_time(String result_time) {
        this.result_time = result_time;
    }
}

