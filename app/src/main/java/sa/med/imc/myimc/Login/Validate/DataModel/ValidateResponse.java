package sa.med.imc.myimc.Login.Validate.DataModel;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;

public class ValidateResponse implements Serializable {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("accessToken")
    String accessToken;

    @SerializedName(value = "patientExist",alternate = "patient_exist")
    boolean patientExist;

    @SerializedName(value = "patientId",alternate = "patient_id")
    String patientId;

    @SerializedName(value = "mrn", alternate = "mrnumber")
    String mrn;

    @SerializedName(value = "nationalId",alternate = "national_id")
    String nationalId;

    @SerializedName(value = "firstName",alternate = "first_name")
    String firstName;

    String arabic_first_name;

    @SerializedName(value = "lastName",alternate = "last_name")
    String lastName;

    String arabic_last_name;

    @SerializedName(value = "mobileNumber",alternate = "mobile_number")
    String mobileNumber;

    @SerializedName("email")
    String email;

    @SerializedName("hosp")
    String hosp;

    @SerializedName(value = "address")
    String address;

    @SerializedName("dob")
    String dob;

    @SerializedName("gender")
    String gender;

    @SerializedName(value = "insuranceList",alternate = "insurance_list")
    List<InsuranceList> insuranceList;

    public String getArabic_first_name() {
        String s=firstName;

        try {
            if (!arabic_first_name.isEmpty()){
                s=arabic_first_name;
            }
        } catch (Exception e) {
            s=firstName;
        }
        return s;
    }

    public void setArabic_first_name(String arabic_first_name) {
        this.arabic_first_name = arabic_first_name;
    }

    public String getArabic_last_name() {
        String s=lastName;

        try {
            if (!arabic_last_name.isEmpty()){
                s=arabic_last_name;
            }
        } catch (Exception e) {
            s=lastName;
        }


        return s;
    }

    public void setArabic_last_name(String arabic_last_name) {
        this.arabic_last_name = arabic_last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setPatientExist(boolean patientExist) {
        this.patientExist = patientExist;
    }

    public boolean getPatientExist() {
        return patientExist;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getMrn() {
        return mrn;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail(Context context) {


        String s= SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_PATIENT_EMAIL,"");
        if (s.trim().isEmpty()){
            return email;
        }
        return s;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setInsuranceList(List<InsuranceList> insuranceList) {
        this.insuranceList = insuranceList;
    }

    public List<InsuranceList> getInsuranceList() {
        return insuranceList;
    }


    public class InsuranceList {

        @SerializedName("id")
        String id;

        @SerializedName(value = "payorCode",alternate = "payor_code")
        String payorCode;

        @SerializedName(value = "payorDescription",alternate = "payor_description")
        String payorDescription;

        @SerializedName(value = "planCode",alternate = "plan_code")
        String planCode;

        @SerializedName(value = "planDescription",alternate = "plan_description")
        String planDescription;

        @SerializedName(value = "cardNo",alternate = "card_no")
        String cardNo;

        @SerializedName(value = "dateFrom",alternate = "date_from")
        String dateFrom;

        @SerializedName(value = "dateTo",alternate = "date_to")
        String dateTo;

        @SerializedName("rank")
        String rank;


        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setPayorCode(String payorCode) {
            this.payorCode = payorCode;
        }

        public String getPayorCode() {
            return payorCode;
        }

        public void setPayorDescription(String payorDescription) {
            this.payorDescription = payorDescription;
        }

        public String getPayorDescription() {
            return payorDescription;
        }

        public void setPlanCode(String planCode) {
            this.planCode = planCode;
        }

        public String getPlanCode() {
            return planCode;
        }

        public void setPlanDescription(String planDescription) {
            this.planDescription = planDescription;
        }

        public String getPlanDescription() {
            return planDescription;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setDateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
        }

        public String getDateFrom() {
            return dateFrom;
        }

        public void setDateTo(String dateTo) {
            this.dateTo = dateTo;
        }

        public String getDateTo() {
            return dateTo;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRank() {
            return rank;
        }

    }
}
