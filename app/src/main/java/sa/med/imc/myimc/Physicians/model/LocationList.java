package sa.med.imc.myimc.Physicians.model;

public class LocationList {

    String location_code;
    String location_description;
    String date_from;
    String date_to;

    public LocationList() {
    }

    public LocationList(String location_code, String location_description, String date_from, String date_to) {
        this.location_code = location_code;
        this.location_description = location_description;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public String getLocation_description() {
        return location_description;
    }

    public void setLocation_description(String location_description) {
        this.location_description = location_description;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }
}
