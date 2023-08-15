package sa.med.imc.myimc.pharmacy.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PharmacyResponse {

	@SerializedName("PrescriptionDetailsResponse")
	private List<PharmacyItem> prescriptionDetailsResponse;

	public void setPrescriptionDetailsResponse(List<PharmacyItem> prescriptionDetailsResponse){
		this.prescriptionDetailsResponse = prescriptionDetailsResponse;
	}

	public List<PharmacyItem> getPrescriptionDetailsResponse(){
		return prescriptionDetailsResponse;
	}

	@Override
 	public String toString(){
		return 
			"PrescriptionDetailsResponse{" + 
			"prescriptionDetailsResponse = '" + prescriptionDetailsResponse + '\'' + 
			"}";
		}
}