package sa.med.imc.myimc.Records.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class LabReportResponse {

    String item_count;
    String page;
    String total;
    String status;
    private List<LabReport> order_list;

    public LabReportResponse() {
    }

    public LabReportResponse(String itemCount, String page, String total, String status, List<LabReport> order_list) {
        this.item_count = itemCount;
        this.page = page;
        this.total = total;
        this.status = status;
        this.order_list = order_list;
    }

    public String getItemCount() {
        return item_count;
    }

    public void setItemCount(String itemCount) {
        this.item_count = itemCount;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LabReport> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<LabReport> order_list) {
        this.order_list = order_list;
    }
}
