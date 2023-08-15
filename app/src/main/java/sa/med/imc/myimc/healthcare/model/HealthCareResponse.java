package sa.med.imc.myimc.healthcare.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HealthCareResponse{

	@SerializedName("data")
	private List<HealthCareItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<HealthCareItem> data){
		this.data = data;
	}

	public List<HealthCareItem> getData(){
		return data;
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

	@Override
 	public String toString(){
		return 
			"HealthCareResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}