package sa.med.imc.myimc.webView.model;

import com.google.gson.annotations.SerializedName;

public class ValidatePaymentRefRequest{

	@SerializedName("refId")
	private String refId;

	public void setRefId(String refId){
		this.refId = refId;
	}

	public String getRefId(){
		return refId;
	}

	@Override
 	public String toString(){
		return 
			"ValidatePaymenrRefRequest{" + 
			"refId = '" + refId + '\'' + 
			"}";
		}
}