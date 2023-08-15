package sa.med.imc.myimc.Records.model;

public class RadiologyOldModel {
    public Id id;
    public String patId;
    public String prodcutCode;
    public String orderDate;
    public String deliveringDept;
    public String orderingDept;
    public String desc;
    public String orderFlag;
    public int resultAvailable;
    public int orderStatusWeb;
    public String episodeNo;
    public int hosp;
    public String isCareStream;
    public String careStreamReportPath;

    public RadiologyOldModel() {
    }

    public RadiologyOldModel(Id id, String patId, String prodcutCode, String orderDate, String deliveringDept, String orderingDept, String desc, String orderFlag, int resultAvailable, int orderStatusWeb, String episodeNo, int hosp, String isCareStream, String careStreamReportPath) {
        this.id = id;
        this.patId = patId;
        this.prodcutCode = prodcutCode;
        this.orderDate = orderDate;
        this.deliveringDept = deliveringDept;
        this.orderingDept = orderingDept;
        this.desc = desc;
        this.orderFlag = orderFlag;
        this.resultAvailable = resultAvailable;
        this.orderStatusWeb = orderStatusWeb;
        this.episodeNo = episodeNo;
        this.hosp = hosp;
        this.isCareStream = isCareStream;
        this.careStreamReportPath = careStreamReportPath;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getProdcutCode() {
        return prodcutCode;
    }

    public void setProdcutCode(String prodcutCode) {
        this.prodcutCode = prodcutCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveringDept() {
        return deliveringDept;
    }

    public void setDeliveringDept(String deliveringDept) {
        this.deliveringDept = deliveringDept;
    }

    public String getOrderingDept() {
        return orderingDept;
    }

    public void setOrderingDept(String orderingDept) {
        this.orderingDept = orderingDept;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
    }

    public int getResultAvailable() {
        return resultAvailable;
    }

    public void setResultAvailable(int resultAvailable) {
        this.resultAvailable = resultAvailable;
    }

    public int getOrderStatusWeb() {
        return orderStatusWeb;
    }

    public void setOrderStatusWeb(int orderStatusWeb) {
        this.orderStatusWeb = orderStatusWeb;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public int getHosp() {
        return hosp;
    }

    public void setHosp(int hosp) {
        this.hosp = hosp;
    }

    public String getIsCareStream() {
        return isCareStream;
    }

    public void setIsCareStream(String isCareStream) {
        this.isCareStream = isCareStream;
    }

    public String getCareStreamReportPath() {
        return careStreamReportPath;
    }

    public void setCareStreamReportPath(String careStreamReportPath) {
        this.careStreamReportPath = careStreamReportPath;
    }

    public class Id{
        public String orderNo;
        public String orderNoLine;

        public Id() {
        }

        public Id(String orderNo, String orderNoLine) {
            this.orderNo = orderNo;
            this.orderNoLine = orderNoLine;
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
    }
}
