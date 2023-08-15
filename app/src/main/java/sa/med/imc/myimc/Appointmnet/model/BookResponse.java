package sa.med.imc.myimc.Appointmnet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("paymentUrl")
    @Expose
    private String paymentUrl;
    @SerializedName("paymentRef")
    @Expose
    private String paymentRef;
    @SerializedName("data")
    @Expose
    private Data data;


    @SerializedName("trace")
    @Expose
    private String trace;
    @SerializedName("order")
    @Expose
    private Order order;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("bookingDate")
        @Expose
        private String bookingDate;
        @SerializedName("clinicCode")
        @Expose
        private String clinicCode;
        @SerializedName("docCode")
        @Expose
        private String docCode;
        @SerializedName("deptCode")
        @Expose
        private String deptCode;
        @SerializedName("serviceCode")
        @Expose
        private String serviceCode;
        @SerializedName("clinicName")
        @Expose
        private String clinicName;
        @SerializedName("clinicNameAr")
        @Expose
        private String clinicNameAr;
        @SerializedName("doctorName")
        @Expose
        private String doctorName;
        @SerializedName("doctorNameAr")
        @Expose
        private String doctorNameAr;
        @SerializedName("deptName")
        @Expose
        private String deptName;
        @SerializedName("serviceName")
        @Expose
        private String serviceName;
        @SerializedName("statusDesc")
        @Expose
        private String statusDesc;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("msg")
        @Expose
        private String msg;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public String getClinicCode() {
            return clinicCode;
        }

        public void setClinicCode(String clinicCode) {
            this.clinicCode = clinicCode;
        }

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getClinicName() {
            return clinicName;
        }

        public void setClinicName(String clinicName) {
            this.clinicName = clinicName;
        }

        public String getClinicNameAr() {
            return clinicNameAr;
        }

        public void setClinicNameAr(String clinicNameAr) {
            this.clinicNameAr = clinicNameAr;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDoctorNameAr() {
            return doctorNameAr;
        }

        public void setDoctorNameAr(String doctorNameAr) {
            this.doctorNameAr = doctorNameAr;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public class Order {

        @SerializedName("ref")
        @Expose
        private String ref;
        @SerializedName("cartid")
        @Expose
        private String cartid;
        @SerializedName("test")
        @Expose
        private String test;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("status")
        @Expose
        private Status status;
        @SerializedName("transaction")
        @Expose
        private Transaction transaction;

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getCartid() {
            return cartid;
        }

        public void setCartid(String cartid) {
            this.cartid = cartid;
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }

    }


    public class Status {

        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("text")
        @Expose
        private String text;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }


    public class Transaction {

        @SerializedName("ref")
        @Expose
        private String ref;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("class")
        @Expose
        private String _class;

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getClass_() {
            return _class;
        }

        public void setClass_(String _class) {
            this._class = _class;
        }

    }
}



