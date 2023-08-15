package sa.med.imc.myimc.healthcare.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HealthCareSubmission{

	@SerializedName("hosp")
	private String hosp;

	@SerializedName("email")
	private String email;

	@SerializedName("patId")
	private String patId;

	@SerializedName("submittedFor")
	private String submittedFor;

	@SerializedName("contact")
	private String contact;

	@SerializedName("HHCFormId")
	private int hHCFormId;

	@SerializedName("questionIds")
	private List<Integer> questionIds;

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

	public void setHHCFormId(int hHCFormId){
		this.hHCFormId = hHCFormId;
	}

	public int getHHCFormId(){
		return hHCFormId;
	}

	public void setQuestionIds(List<Integer> questionIds){
		this.questionIds = questionIds;
	}

	public List<Integer> getQuestionIds(){
		return questionIds;
	}

	public String getSubmittedFor() {
		return submittedFor;
	}

	public void setSubmittedFor(String submittedFor) {
		this.submittedFor = submittedFor;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
 	public String toString(){
		return 
			"HealthCareSubmission{" + 
			"hosp = '" + hosp + '\'' + 
			",patId = '" + patId + '\'' + 
			",hHCFormId = '" + hHCFormId + '\'' + 
			",questionIds = '" + questionIds + '\'' + 
			"}";
		}
}