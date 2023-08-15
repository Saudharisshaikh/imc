package sa.med.imc.myimc.HealthSummary.model;

import java.util.ArrayList;

public class NewVitalModel {

    public ArrayList<VitalList> vital_list;
    public int item_count;
    public int page;
    public int total;
    public String status;

    public NewVitalModel() {
    }

    public NewVitalModel(ArrayList<VitalList> vital_list, int item_count, int page, int total, String status) {
        this.vital_list = vital_list;
        this.item_count = item_count;
        this.page = page;
        this.total = total;
        this.status = status;
    }

    public ArrayList<VitalList> getVital_list() {
        return vital_list;
    }

    public void setVital_list(ArrayList<VitalList> vital_list) {
        this.vital_list = vital_list;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class VitalList{
        public String id;
        public String episode_number;
        public String observation_item_code;
        public String observation_item_description;
        public String observation_date;
        public String observation_time;
        public String value;
        public String uom;

        public VitalList() {
        }

        public VitalList(String id, String episode_number, String observation_item_code, String observation_item_description, String observation_date, String observation_time, String value, String uom) {
            this.id = id;
            this.episode_number = episode_number;
            this.observation_item_code = observation_item_code;
            this.observation_item_description = observation_item_description;
            this.observation_date = observation_date;
            this.observation_time = observation_time;
            this.value = value;
            this.uom = uom;
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

        public String getObservation_item_code() {
            return observation_item_code;
        }

        public void setObservation_item_code(String observation_item_code) {
            this.observation_item_code = observation_item_code;
        }

        public String getObservation_item_description() {
            return observation_item_description;
        }

        public void setObservation_item_description(String observation_item_description) {
            this.observation_item_description = observation_item_description;
        }

        public String getObservation_date() {
            return observation_date;
        }

        public void setObservation_date(String observation_date) {
            this.observation_date = observation_date;
        }

        public String getObservation_time() {
            return observation_time;
        }

        public void setObservation_time(String observation_time) {
            this.observation_time = observation_time;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }
    }
}
