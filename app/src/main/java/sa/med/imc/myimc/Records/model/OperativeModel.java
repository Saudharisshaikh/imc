package sa.med.imc.myimc.Records.model;

public class OperativeModel {
    public String document_id;
    public String authorized_user;
    public String authorized_date;
    public String authorized_time;

    public OperativeModel() {
    }

    public OperativeModel(String document_id, String authorized_user, String authorized_date, String authorized_time) {
        this.document_id = document_id;
        this.authorized_user = authorized_user;
        this.authorized_date = authorized_date;
        this.authorized_time = authorized_time;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getAuthorized_user() {
        return authorized_user;
    }

    public void setAuthorized_user(String authorized_user) {
        this.authorized_user = authorized_user;
    }

    public String getAuthorized_date() {
        return authorized_date;
    }

    public void setAuthorized_date(String authorized_date) {
        this.authorized_date = authorized_date;
    }

    public String getAuthorized_time() {
        return authorized_time;
    }

    public void setAuthorized_time(String authorized_time) {
        this.authorized_time = authorized_time;
    }
}
