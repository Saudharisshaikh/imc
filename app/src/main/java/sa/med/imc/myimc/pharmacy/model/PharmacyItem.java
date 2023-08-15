package sa.med.imc.myimc.pharmacy.model;

import com.google.gson.annotations.SerializedName;

public class PharmacyItem {

	@SerializedName("orderComment")
	private String orderComment;

	@SerializedName("patId")
	private String patId;

	@SerializedName("patName")
	private String patName;

	@SerializedName("orderBy")
	private String orderBy;

	@SerializedName("dispensingId")
	private int dispensingId;

	@SerializedName("cmpsId")
	private String cmpsId;

	@SerializedName("enteredBy")
	private String enteredBy;

	@SerializedName("noOfDays")
	private String noOfDays;

	@SerializedName("slideDose")
	private String slideDose;

	@SerializedName("rxNo")
	private String rxNo;

	@SerializedName("docName")
	private String docName;

	@SerializedName("orderNoLine")
	private int orderNoLine;

	@SerializedName("hosp")
	private int hosp;

	@SerializedName("patHeight")
	private String patHeight;

	@SerializedName("patientAge")
	private String patientAge;

	@SerializedName("instrUom")
	private Object instrUom;

	@SerializedName("tprFlag")
	private String tprFlag;

	@SerializedName("erMed")
	private String erMed;

	@SerializedName("epsoideType")
	private String epsoideType;

	@SerializedName("instrFreq")
	private String instrFreq;

	@SerializedName("orderNo")
	private int orderNo;

	@SerializedName("patBsa")
	private String patBsa;

	@SerializedName("episodeNo")
	private int episodeNo;

	@SerializedName("thMed")
	private String thMed;

	@SerializedName("productDesc")
	private String productDesc;

	@SerializedName("patWeight")
	private String patWeight;

	@SerializedName("productCode")
	private String productCode;

	@SerializedName("pregnacyAlert")
	private String pregnacyAlert;

	@SerializedName("inStr")
	private String inStr;

	@SerializedName("patAlerts")
	private String patAlerts;

	@SerializedName("patDiagnosis")
	private String patDiagnosis;

	@SerializedName("instrRoute")
	private String instrRoute;

	@SerializedName("orderDate")
	private String orderDate;

	@SerializedName("docNameAr")
	private String docNameAr;

	public void setOrderComment(String orderComment){
		this.orderComment = orderComment;
	}

	public String getOrderComment(){
		return orderComment;
	}

	public void setPatId(String patId){
		this.patId = patId;
	}

	public String getPatId(){
		return patId;
	}

	public void setPatName(String patName){
		this.patName = patName;
	}

	public String getPatName(){
		return patName;
	}

	public void setOrderBy(String orderBy){
		this.orderBy = orderBy;
	}

	public String getOrderBy(){
		return orderBy;
	}

	public void setDispensingId(int dispensingId){
		this.dispensingId = dispensingId;
	}

	public int getDispensingId(){
		return dispensingId;
	}

	public void setCmpsId(String cmpsId){
		this.cmpsId = cmpsId;
	}

	public String getCmpsId(){
		return cmpsId;
	}

	public void setEnteredBy(String enteredBy){
		this.enteredBy = enteredBy;
	}

	public String getEnteredBy(){
		return enteredBy;
	}

	public void setNoOfDays(String noOfDays){
		this.noOfDays = noOfDays;
	}

	public String getNoOfDays(){
		return noOfDays;
	}

	public void setSlideDose(String slideDose){
		this.slideDose = slideDose;
	}

	public String getSlideDose(){
		return slideDose;
	}

	public void setRxNo(String rxNo){
		this.rxNo = rxNo;
	}

	public String getRxNo(){
		return rxNo;
	}

	public void setDocName(String docName){
		this.docName = docName;
	}

	public String getDocName(){
		return docName;
	}

	public void setOrderNoLine(int orderNoLine){
		this.orderNoLine = orderNoLine;
	}

	public int getOrderNoLine(){
		return orderNoLine;
	}

	public void setHosp(int hosp){
		this.hosp = hosp;
	}

	public int getHosp(){
		return hosp;
	}

	public void setPatHeight(String patHeight){
		this.patHeight = patHeight;
	}

	public String getPatHeight(){
		return patHeight;
	}

	public void setPatientAge(String patientAge){
		this.patientAge = patientAge;
	}

	public String getPatientAge(){
		return patientAge;
	}

	public void setInstrUom(Object instrUom){
		this.instrUom = instrUom;
	}

	public Object getInstrUom(){
		return instrUom;
	}

	public void setTprFlag(String tprFlag){
		this.tprFlag = tprFlag;
	}

	public String getTprFlag(){
		return tprFlag;
	}

	public void setErMed(String erMed){
		this.erMed = erMed;
	}

	public String getErMed(){
		return erMed;
	}

	public void setEpsoideType(String epsoideType){
		this.epsoideType = epsoideType;
	}

	public String getEpsoideType(){
		return epsoideType;
	}

	public void setInstrFreq(String instrFreq){
		this.instrFreq = instrFreq;
	}

	public String getInstrFreq(){
		return instrFreq;
	}

	public void setOrderNo(int orderNo){
		this.orderNo = orderNo;
	}

	public int getOrderNo(){
		return orderNo;
	}

	public void setPatBsa(String patBsa){
		this.patBsa = patBsa;
	}

	public String getPatBsa(){
		return patBsa;
	}

	public void setEpisodeNo(int episodeNo){
		this.episodeNo = episodeNo;
	}

	public int getEpisodeNo(){
		return episodeNo;
	}

	public void setThMed(String thMed){
		this.thMed = thMed;
	}

	public String getThMed(){
		return thMed;
	}

	public void setProductDesc(String productDesc){
		this.productDesc = productDesc;
	}

	public String getProductDesc(){
		return productDesc;
	}

	public void setPatWeight(String patWeight){
		this.patWeight = patWeight;
	}

	public String getPatWeight(){
		return patWeight;
	}

	public void setProductCode(String productCode){
		this.productCode = productCode;
	}

	public String getProductCode(){
		return productCode;
	}

	public void setPregnacyAlert(String pregnacyAlert){
		this.pregnacyAlert = pregnacyAlert;
	}

	public String getPregnacyAlert(){
		return pregnacyAlert;
	}

	public void setInStr(String inStr){
		this.inStr = inStr;
	}

	public String getInStr(){
		return inStr;
	}

	public void setPatAlerts(String patAlerts){
		this.patAlerts = patAlerts;
	}

	public String getPatAlerts(){
		return patAlerts;
	}

	public void setPatDiagnosis(String patDiagnosis){
		this.patDiagnosis = patDiagnosis;
	}

	public String getPatDiagnosis(){
		return patDiagnosis;
	}

	public void setInstrRoute(String instrRoute){
		this.instrRoute = instrRoute;
	}

	public String getInstrRoute(){
		return instrRoute;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setDocNameAr(String docNameAr){
		this.docNameAr = docNameAr;
	}

	public String getDocNameAr(){
		return docNameAr;
	}

	@Override
 	public String toString(){
		return 
			"PrescriptionDetailsResponseItem{" + 
			"orderComment = '" + orderComment + '\'' + 
			",patId = '" + patId + '\'' + 
			",patName = '" + patName + '\'' + 
			",orderBy = '" + orderBy + '\'' + 
			",dispensingId = '" + dispensingId + '\'' + 
			",cmpsId = '" + cmpsId + '\'' + 
			",enteredBy = '" + enteredBy + '\'' + 
			",noOfDays = '" + noOfDays + '\'' + 
			",slideDose = '" + slideDose + '\'' + 
			",rxNo = '" + rxNo + '\'' + 
			",docName = '" + docName + '\'' + 
			",orderNoLine = '" + orderNoLine + '\'' + 
			",hosp = '" + hosp + '\'' + 
			",patHeight = '" + patHeight + '\'' + 
			",patientAge = '" + patientAge + '\'' + 
			",instrUom = '" + instrUom + '\'' + 
			",tprFlag = '" + tprFlag + '\'' + 
			",erMed = '" + erMed + '\'' + 
			",epsoideType = '" + epsoideType + '\'' + 
			",instrFreq = '" + instrFreq + '\'' + 
			",orderNo = '" + orderNo + '\'' + 
			",patBsa = '" + patBsa + '\'' + 
			",episodeNo = '" + episodeNo + '\'' + 
			",thMed = '" + thMed + '\'' + 
			",productDesc = '" + productDesc + '\'' + 
			",patWeight = '" + patWeight + '\'' + 
			",productCode = '" + productCode + '\'' + 
			",pregnacyAlert = '" + pregnacyAlert + '\'' + 
			",inStr = '" + inStr + '\'' + 
			",patAlerts = '" + patAlerts + '\'' + 
			",patDiagnosis = '" + patDiagnosis + '\'' + 
			",instrRoute = '" + instrRoute + '\'' + 
			",orderDate = '" + orderDate + '\'' + 
			",docNameAr = '" + docNameAr + '\'' + 
			"}";
		}
}