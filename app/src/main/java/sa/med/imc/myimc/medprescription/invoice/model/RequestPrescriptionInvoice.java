package sa.med.imc.myimc.medprescription.invoice.model;

import com.google.gson.annotations.SerializedName;

public class RequestPrescriptionInvoice{

	@SerializedName("episodeType")
	private String episodeType;

	@SerializedName("rxNo")
	private String rxNo;

	@SerializedName("orderNo")
	private int orderNo;

	@SerializedName("hosp")
	private String hosp;

	@SerializedName("patId")
	private String patId;

	@SerializedName("episodeNo")
	private int episodeNo;

	@SerializedName("phOrderId")
	private int phOrderId;

	public void setEpisodeType(String episodeType){
		this.episodeType = episodeType;
	}

	public String getEpisodeType(){
		return episodeType;
	}

	public void setRxNo(String rxNo){
		this.rxNo = rxNo;
	}

	public String getRxNo(){
		return rxNo;
	}

	public void setOrderNo(int orderNo){
		this.orderNo = orderNo;
	}

	public int getOrderNo(){
		return orderNo;
	}

	public void setHosp(String hosp){
		this.hosp = hosp;
	}

	public String getHosp(){
		return hosp;
	}

	public void setPatId(String patId){
		this.patId = patId;
	}

	public String getPatId(){
		return patId;
	}

	public void setEpisodeNo(int episodeNo){
		this.episodeNo = episodeNo;
	}

	public int getEpisodeNo(){
		return episodeNo;
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
			"RequestPrescriptiontInvoice{" + 
			"episodeType = '" + episodeType + '\'' + 
			",rxNo = '" + rxNo + '\'' + 
			",orderNo = '" + orderNo + '\'' + 
			",hosp = '" + hosp + '\'' + 
			",patId = '" + patId + '\'' + 
			",episodeNo = '" + episodeNo + '\'' + 
			"}";
		}
}