package sa.med.imc.myimc.healthcare.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HealthCareItem {

	@SerializedName("hhcformId")
	private int hhcformId;

	@SerializedName("hosp")
	private String hosp;

	@SerializedName("questionAr")
	private String questionAr;

	@SerializedName("linkedHHCFormQuestions")
	private List<HealthCareItem> linkedHHCFormQuestions;

	@SerializedName("id")
	private int id;

	@SerializedName("isActive")
	private int isActive;

	@SerializedName("questionEn")
	private String questionEn;

	@SerializedName("parentId")
	private String parentId;

	@SerializedName("tooltipEn")
	private String tooltipEn;

	@SerializedName("tooltipAr")
	private String tooltipAr;


	private transient boolean isChecked;

	private transient int color;


	public void setHhcformId(int hhcformId){
		this.hhcformId = hhcformId;
	}

	public int getHhcformId(){
		return hhcformId;
	}

	public void setHosp(String hosp){
		this.hosp = hosp;
	}

	public String getHosp(){
		return hosp;
	}

	public void setQuestionAr(String questionAr){
		this.questionAr = questionAr;
	}

	public String getQuestionAr(){
		return questionAr;
	}

	public void setLinkedHHCFormQuestions(List<HealthCareItem> linkedHHCFormQuestions){
		this.linkedHHCFormQuestions = linkedHHCFormQuestions;
	}

	public List<HealthCareItem> getLinkedHHCFormQuestions(){
		return linkedHHCFormQuestions;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setQuestionEn(String questionEn){
		this.questionEn = questionEn;
	}

	public String getQuestionEn(){
		return questionEn;
	}

	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return parentId;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getTooltipEn() {
		return tooltipEn;
	}

	public void setTooltipEn(String tooltipEn) {
		this.tooltipEn = tooltipEn;
	}

	public String getTooltipAr() {
		return tooltipAr;
	}

	public void setTooltipAr(String tooltipAr) {
		this.tooltipAr = tooltipAr;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"hhcformId = '" + hhcformId + '\'' + 
			",hosp = '" + hosp + '\'' + 
			",questionAr = '" + questionAr + '\'' + 
			",linkedHHCFormQuestions = '" + linkedHHCFormQuestions + '\'' + 
			",id = '" + id + '\'' + 
			",isActive = '" + isActive + '\'' + 
			",questionEn = '" + questionEn + '\'' + 
			",parentId = '" + parentId + '\'' + 
			"}";
		}
}