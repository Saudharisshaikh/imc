package sa.med.imc.myimc.medprescription.invoice.model;

import com.google.gson.annotations.SerializedName;

public class PrescriptiontInvoiceItem{

	@SerializedName("episodeType")
	private String episodeType;

	@SerializedName("rxNo")
	private String rxNo;

	@SerializedName("orderNo")
	private int orderNo;

	@SerializedName("hosp")
	private String hosp;

	@SerializedName("deliveryMethod")
	private String deliveryMethod;

	@SerializedName("patId")
	private String patId;

	@SerializedName("episodeNo")
	private int episodeNo;

	@SerializedName("delivertAddress")
	private Object delivertAddress;

	@SerializedName("id")
	private int id;

	@SerializedName("orderDate")
	private String orderDate;

	@SerializedName("status")
	private int status;

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

	public void setDeliveryMethod(String deliveryMethod){
		this.deliveryMethod = deliveryMethod;
	}

	public String getDeliveryMethod(){
		return deliveryMethod;
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

	public void setDelivertAddress(Object delivertAddress){
		this.delivertAddress = delivertAddress;
	}

	public Object getDelivertAddress(){
		return delivertAddress;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"PrescriptiontInvoiceItem{" + 
			"episodeType = '" + episodeType + '\'' + 
			",rxNo = '" + rxNo + '\'' + 
			",orderNo = '" + orderNo + '\'' + 
			",hosp = '" + hosp + '\'' + 
			",deliveryMethod = '" + deliveryMethod + '\'' + 
			",patId = '" + patId + '\'' + 
			",episodeNo = '" + episodeNo + '\'' + 
			",delivertAddress = '" + delivertAddress + '\'' + 
			",id = '" + id + '\'' + 
			",orderDate = '" + orderDate + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}