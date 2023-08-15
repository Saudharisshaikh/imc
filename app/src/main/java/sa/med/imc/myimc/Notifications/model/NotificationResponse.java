package sa.med.imc.myimc.Notifications.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<NotificationModel> notificationModels = null;
    @SerializedName("total_items")
    @Expose
    private String totalItems;
    @SerializedName("total_pages")
    @Expose
    private String totalPages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public List<NotificationModel> getNotificationModels() {
        return notificationModels;
    }

    public void setNotificationModels(List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
    }

    public class NotificationModel {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("patId")
        @Expose
        private String patId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("titleAr")
        @Expose
        private String titleAr;
        @SerializedName("descp")
        @Expose
        private String descp;
        @SerializedName("descpAr")
        @Expose
        private String descpAr;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("isActive")
        @Expose
        private String isActive;
        @SerializedName("itemId")
        @Expose
        private String itemId;
        @SerializedName("notifTime")
        @Expose
        private String notifTime;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("read")
        @Expose
        private String read;
        @SerializedName("html")
        @Expose
        private String html;

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleAr() {
            return titleAr;
        }

        public void setTitleAr(String titleAr) {
            this.titleAr = titleAr;
        }

        public String getDescp() {
            return descp;
        }

        public void setDescp(String descp) {
            this.descp = descp;
        }

        public String getDescpAr() {
            return descpAr;
        }

        public void setDescpAr(String descpAr) {
            this.descpAr = descpAr;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getNotifTime() {
            return notifTime;
        }

        public void setNotifTime(String notifTime) {
            this.notifTime = notifTime;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }
    }

}
