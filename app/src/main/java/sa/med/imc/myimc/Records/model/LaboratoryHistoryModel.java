package sa.med.imc.myimc.Records.model;

import java.util.List;

public class LaboratoryHistoryModel {

    String name;
    List<Data> dataList;

    public LaboratoryHistoryModel() {
    }

    public LaboratoryHistoryModel(String name, List<Data> dataList) {
        this.name = name;
        this.dataList = dataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public class Data{
        public String mainTest;
        public String longName;
        public String specimenNum;
        public String accessionNum;
        public String dateTaken;
        public String patId;
        public String trackingNum;
        public String orderNum;
        public String orderNumLine;
        public String reportType;
        public int status;
        public int statusWeb;
        public String episodeNo;
        public String docName;
        public String docNameAr;
        public String aptTime;
        public String aptTimeAr;
        public int smartReport;
        public int hosp;

        public Data() {
        }

        public Data(String mainTest, String longName, String specimenNum, String accessionNum, String dateTaken, String patId, String trackingNum, String orderNum, String orderNumLine, String reportType, int status, int statusWeb, String episodeNo, String docName, String docNameAr, String aptTime, String aptTimeAr, int smartReport, int hosp) {
            this.mainTest = mainTest;
            this.longName = longName;
            this.specimenNum = specimenNum;
            this.accessionNum = accessionNum;
            this.dateTaken = dateTaken;
            this.patId = patId;
            this.trackingNum = trackingNum;
            this.orderNum = orderNum;
            this.orderNumLine = orderNumLine;
            this.reportType = reportType;
            this.status = status;
            this.statusWeb = statusWeb;
            this.episodeNo = episodeNo;
            this.docName = docName;
            this.docNameAr = docNameAr;
            this.aptTime = aptTime;
            this.aptTimeAr = aptTimeAr;
            this.smartReport = smartReport;
            this.hosp = hosp;
        }

        public String getMainTest() {
            return mainTest;
        }

        public void setMainTest(String mainTest) {
            this.mainTest = mainTest;
        }

        public String getLongName() {
            return longName;
        }

        public void setLongName(String longName) {
            this.longName = longName;
        }

        public String getSpecimenNum() {
            return specimenNum;
        }

        public void setSpecimenNum(String specimenNum) {
            this.specimenNum = specimenNum;
        }

        public String getAccessionNum() {
            return accessionNum;
        }

        public void setAccessionNum(String accessionNum) {
            this.accessionNum = accessionNum;
        }

        public String getDateTaken() {
            return dateTaken;
        }

        public void setDateTaken(String dateTaken) {
            this.dateTaken = dateTaken;
        }

        public String getPatId() {
            return patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }

        public String getTrackingNum() {
            return trackingNum;
        }

        public void setTrackingNum(String trackingNum) {
            this.trackingNum = trackingNum;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getOrderNumLine() {
            return orderNumLine;
        }

        public void setOrderNumLine(String orderNumLine) {
            this.orderNumLine = orderNumLine;
        }

        public String getReportType() {
            return reportType;
        }

        public void setReportType(String reportType) {
            this.reportType = reportType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatusWeb() {
            return statusWeb;
        }

        public void setStatusWeb(int statusWeb) {
            this.statusWeb = statusWeb;
        }

        public String getEpisodeNo() {
            return episodeNo;
        }

        public void setEpisodeNo(String episodeNo) {
            this.episodeNo = episodeNo;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getDocNameAr() {
            return docNameAr;
        }

        public void setDocNameAr(String docNameAr) {
            this.docNameAr = docNameAr;
        }

        public String getAptTime() {
            return aptTime;
        }

        public void setAptTime(String aptTime) {
            this.aptTime = aptTime;
        }

        public String getAptTimeAr() {
            return aptTimeAr;
        }

        public void setAptTimeAr(String aptTimeAr) {
            this.aptTimeAr = aptTimeAr;
        }

        public int getSmartReport() {
            return smartReport;
        }

        public void setSmartReport(int smartReport) {
            this.smartReport = smartReport;
        }

        public int getHosp() {
            return hosp;
        }

        public void setHosp(int hosp) {
            this.hosp = hosp;
        }
    }

}

