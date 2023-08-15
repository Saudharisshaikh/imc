package sa.med.imc.myimc.pharmacy.model;

import com.google.gson.annotations.SerializedName;

public class ValidatePharmacyPaymentRequest{

	@SerializedName("rxNo")
	private String rxNo;

	@SerializedName("amount")
	private String amount;

	@SerializedName("orderNo")
	private String orderNo;

	@SerializedName("episodeNo")
	private String episodeNo;

	@SerializedName("patName")
	private String patName;

	@SerializedName("mrNumber")
	private String mrNumber;

	public void setRxNo(String rxNo){
		this.rxNo = rxNo;
	}

	public String getRxNo(){
		return rxNo;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setEpisodeNo(String episodeNo){
		this.episodeNo = episodeNo;
	}

	public String getEpisodeNo(){
		return episodeNo;
	}

	public void setPatName(String patName){
		this.patName = patName;
	}

	public String getPatName(){
		return patName;
	}

	public void setMrNumber(String mrNumber){
		this.mrNumber = mrNumber;
	}

	public String getMrNumber(){
		return mrNumber;
	}

	@Override
 	public String toString(){
		return 
			"ValidatePharmacyPaymentRequest{" + 
			"rxNo = '" + rxNo + '\'' + 
			",amount = '" + amount + '\'' + 
			",orderNo = '" + orderNo + '\'' + 
			",episodeNo = '" + episodeNo + '\'' + 
			",patName = '" + patName + '\'' + 
			",mrNumber = '" + mrNumber + '\'' + 
			"}";
		}
}