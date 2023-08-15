package sa.med.imc.myimc.Home.model;

import com.google.gson.annotations.SerializedName;

public class ERInfoItem{

	@SerializedName("subHeadingAr")
	private String subHeadingAr;

	@SerializedName("headingAr")
	private String headingAr;

	@SerializedName("labelEn")
	private String labelEn;

	@SerializedName("subHeadingEn")
	private String subHeadingEn;

	@SerializedName("headingEn")
	private String headingEn;

	@SerializedName("value")
	private int value;

	@SerializedName("labelAr")
	private String labelAr;

	public void setSubHeadingAr(String subHeadingAr){
		this.subHeadingAr = subHeadingAr;
	}

	public String getSubHeadingAr(){
		return subHeadingAr;
	}

	public void setHeadingAr(String headingAr){
		this.headingAr = headingAr;
	}

	public String getHeadingAr(){
		return headingAr;
	}

	public void setLabelEn(String labelEn){
		this.labelEn = labelEn;
	}

	public String getLabelEn(){
		return labelEn;
	}

	public void setSubHeadingEn(String subHeadingEn){
		this.subHeadingEn = subHeadingEn;
	}

	public String getSubHeadingEn(){
		return subHeadingEn;
	}

	public void setHeadingEn(String headingEn){
		this.headingEn = headingEn;
	}

	public String getHeadingEn(){
		return headingEn;
	}

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	public void setLabelAr(String labelAr){
		this.labelAr = labelAr;
	}

	public String getLabelAr(){
		return labelAr;
	}

	@Override
 	public String toString(){
		return 
			"ERInfoItem{" + 
			"subHeadingAr = '" + subHeadingAr + '\'' + 
			",headingAr = '" + headingAr + '\'' + 
			",labelEn = '" + labelEn + '\'' + 
			",subHeadingEn = '" + subHeadingEn + '\'' + 
			",headingEn = '" + headingEn + '\'' + 
			",value = '" + value + '\'' + 
			",labelAr = '" + labelAr + '\'' + 
			"}";
		}
}