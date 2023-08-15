package sa.med.imc.myimc.Base;

import com.google.gson.annotations.SerializedName;

public class GenericResponse<T>{

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	@SerializedName(value = "data",alternate = {"prescription"})
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}