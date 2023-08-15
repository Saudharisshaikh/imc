package sa.med.imc.myimc.healthcare.model;

import com.google.gson.annotations.SerializedName;

public class HealthCareListRequest{

	@SerializedName("hosp")
	private String hosp;

	@SerializedName("HHCFormId")
	private int hHCFormId;

	public void setHosp(String hosp){
		this.hosp = hosp;
	}

	public String getHosp(){
		return hosp;
	}

	public void setHHCFormId(int hHCFormId){
		this.hHCFormId = hHCFormId;
	}

	public int getHHCFormId(){
		return hHCFormId;
	}

	@Override
 	public String toString(){
		return 
			"HealthCareListRequest{" + 
			"hosp = '" + hosp + '\'' + 
			",hHCFormId = '" + hHCFormId + '\'' + 
			"}";
		}
}