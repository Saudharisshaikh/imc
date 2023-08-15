package sa.med.imc.myimc.Settings.model;

import java.util.ArrayList;
import java.util.List;

public class Select_TypeResponse {

    public List<EpisodeList> episode_list;
    public String status;

    public Select_TypeResponse() {
    }

    public Select_TypeResponse(List<EpisodeList> episode_list, String status) {
        this.episode_list = episode_list;
        this.status = status;
    }

    public List<EpisodeList> getEpisode_list() {
        return episode_list;
    }

    public void setEpisode_list(List<EpisodeList> episode_list) {
        this.episode_list = episode_list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class EpisodeList{
        public String episode_number;
        public String episode_id;
        public String episode_status;
        public String episode_date;
        public String episode_time;
        public String episodeType;
        public String department;
        public String hospital;
        public String care_provider_code;
        public String care_provider_description;

        public EpisodeList() {
        }

        public EpisodeList(String episode_number, String episode_id, String episode_status, String episode_date, String episode_time, String episodeType, String department, String hospital, String care_provider_code, String care_provider_description) {
            this.episode_number = episode_number;
            this.episode_id = episode_id;
            this.episode_status = episode_status;
            this.episode_date = episode_date;
            this.episode_time = episode_time;
            this.episodeType = episodeType;
            this.department = department;
            this.hospital = hospital;
            this.care_provider_code = care_provider_code;
            this.care_provider_description = care_provider_description;
        }

        public String getEpisode_number() {
            return episode_number;
        }

        public void setEpisode_number(String episode_number) {
            this.episode_number = episode_number;
        }

        public String getEpisode_id() {
            return episode_id;
        }

        public void setEpisode_id(String episode_id) {
            this.episode_id = episode_id;
        }

        public String getEpisode_status() {
            return episode_status;
        }

        public void setEpisode_status(String episode_status) {
            this.episode_status = episode_status;
        }

        public String getEpisode_date() {
            return episode_date;
        }

        public void setEpisode_date(String episode_date) {
            this.episode_date = episode_date;
        }

        public String getEpisode_time() {
            return episode_time;
        }

        public void setEpisode_time(String episode_time) {
            this.episode_time = episode_time;
        }

        public String getEpisodeType() {
            return episodeType;
        }

        public void setEpisodeType(String episodeType) {
            this.episodeType = episodeType;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getCare_provider_code() {
            return care_provider_code;
        }

        public void setCare_provider_code(String care_provider_code) {
            this.care_provider_code = care_provider_code;
        }

        public String getCare_provider_description() {
            return care_provider_description;
        }

        public void setCare_provider_description(String care_provider_description) {
            this.care_provider_description = care_provider_description;
        }
    }
}
