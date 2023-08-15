package sa.med.imc.myimc.Physicians.model;

public class LicenseList {
    String license_number;
    String date_from;
    String date_to;

    public LicenseList() {
    }

    public LicenseList(String license_number, String date_from, String date_to) {
        this.license_number = license_number;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
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
