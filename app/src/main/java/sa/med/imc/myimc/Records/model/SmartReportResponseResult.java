package sa.med.imc.myimc.Records.model;

/**
 * Created by Rohit Singh on 03,February,2021
 * Absolve Tech company,
 * Mohali, India.
 */

public class SmartReportResponseResult {

    private String file;
    private String message;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

class SmartReportResponse {

    private Integer code;
    private SmartReportResponseResult data;
    private String message;
    private Integer status;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public SmartReportResponseResult getData() {
        return data;
    }

    public void setData(SmartReportResponseResult data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}