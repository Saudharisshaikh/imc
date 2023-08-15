package sa.med.imc.myimc.pharmacy.model;

import com.google.gson.annotations.SerializedName;

public class PharmacyTelrResponse{

	@SerializedName("paymentUrl")
	private String paymentUrl;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	@SerializedName("paymentRef")
	private String paymentRef;

	@SerializedName("phOrderId")
	private int phOrderId;

	public void setPaymentUrl(String paymentUrl){
		this.paymentUrl = paymentUrl;
	}

	public String getPaymentUrl(){
		return paymentUrl;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setPaymentRef(String paymentRef){
		this.paymentRef = paymentRef;
	}

	public String getPaymentRef(){
		return paymentRef;
	}

	public int getPhOrderId() {
		return phOrderId;
	}

	public void setPhOrderId(int phOrderId) {
		this.phOrderId = phOrderId;
	}

	@Override
 	public String toString(){
		return 
			"PharmacyTelrResponse{" + 
			"paymentUrl = '" + paymentUrl + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			",paymentRef = '" + paymentRef + '\'' + 
			"}";
		}
}