package sa.med.imc.myimc.Records.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdModel {
    @SerializedName("orderNo")
    @Expose
    private String orderNo;
    @SerializedName("orderNoLine")
    @Expose
    private String orderNoLine;

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

}
