package sa.med.imc.myimc.Records.model;

public class CardiologyModel {
    public String order_id;
    public String order_description;
    public String ordered_doctor;
    public String order_status;
    public String authorized_user;
    public String result_date;
    public String result_time;

    public CardiologyModel() {
    }

    public CardiologyModel(String order_id, String order_description, String ordered_doctor, String order_status, String authorized_user, String result_date, String result_time) {
        this.order_id = order_id;
        this.order_description = order_description;
        this.ordered_doctor = ordered_doctor;
        this.order_status = order_status;
        this.authorized_user = authorized_user;
        this.result_date = result_date;
        this.result_time = result_time;
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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
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
