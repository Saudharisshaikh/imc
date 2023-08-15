package sa.med.imc.myimc.Records.model;

import java.util.ArrayList;

public class SickReportOldModel {
    public ArrayList<Content> content;
    public Pageable pageable;
    public boolean last;
    public int totalPages;
    public int totalElements;
    public int size;
    public int number;
    public Sort sort;
    public boolean first;
    public int numberOfElements;
    public boolean empty;

    public SickReportOldModel() {
    }

    public SickReportOldModel(ArrayList<Content> content, Pageable pageable, boolean last, int totalPages, int totalElements, int size, int number, Sort sort, boolean first, int numberOfElements, boolean empty) {
        this.content = content;
        this.pageable = pageable;
        this.last = last;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
        this.sort = sort;
        this.first = first;
        this.numberOfElements = numberOfElements;
        this.empty = empty;
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public class Sort{
        public boolean unsorted;
        public boolean sorted;
        public boolean empty;

        public Sort(boolean unsorted, boolean sorted, boolean empty) {
            this.unsorted = unsorted;
            this.sorted = sorted;
            this.empty = empty;
        }

        public Sort() {
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }

    public class Content{
        public int id;
        public String sysCode;
        public String staffId;
        public String leaveType;
        public String leaveExpectStartDate;
        public String leaveExpectReturnDate;
        public String enteredByName;
        public String locationName;
        public String locationNameAr;
        public String leaveActualLeaveDate;
        public String leaveActualReturnDate;
        public String leaveNotes;
        public String leaveStatus;
        public String amendBy;
        public String amendDate;
        public int hosp;
        public int episodeNo;
        public String episodeType;
        public String episodeConsultant;
        public String episodeDate;
        public String episodeLocation;
        public String sickLeaveDayFlag;
        public String followupFlag;
        public String referralFlag;
        public String reqApprovalFlag;
        public String untreatedFlag;
        public String disabilityFlag;
        public String treatingConsultant;
        public String treatingDate;
        public String approvingConsultant;
        public String approvingDate;
        public int sickLeaveDay;
        public String referralReason;
        public String otherRecFlag;
        public String otherRecommend;
        public String patid;
        public String mrPatDocId;
        public String admitDate;
        public String dischDate;
        public String noVerifiers;
        public String verifiedBy1;
        public String verifiedBy1Date;
        public String verifiedBy2;
        public String verifiedBy2Date;
        public String ver1Flag;
        public String ver2Flag;
        public String ver1AmendBy;
        public String ver2AmendBy;
        public String addAmendBy;
        public String addAmendDate;
        public String addNotes;
        public String incomingNotes;
        public String incomingDates;
        public String addRefId;
        public String approvedAdminBy;
        public String approvedAdminDate;
        public String outDoc;
        public String outClinic;
        public String outDiagnosis;
        public String extendPatLeave;
        public String printCount;
        public String cancelReason;
        public String deptCode;

        public Content() {
        }

        public Content(int id, String sysCode, String staffId, String leaveType, String leaveExpectStartDate, String leaveExpectReturnDate, String enteredByName, String locationName, String locationNameAr, String leaveActualLeaveDate, String leaveActualReturnDate, String leaveNotes, String leaveStatus, String amendBy, String amendDate, int hosp, int episodeNo, String episodeType, String episodeConsultant, String episodeDate, String episodeLocation, String sickLeaveDayFlag, String followupFlag, String referralFlag, String reqApprovalFlag, String untreatedFlag, String disabilityFlag, String treatingConsultant, String treatingDate, String approvingConsultant, String approvingDate, int sickLeaveDay, String referralReason, String otherRecFlag, String otherRecommend, String patid, String mrPatDocId, String admitDate, String dischDate, String noVerifiers, String verifiedBy1, String verifiedBy1Date, String verifiedBy2, String verifiedBy2Date, String ver1Flag, String ver2Flag, String ver1AmendBy, String ver2AmendBy, String addAmendBy, String addAmendDate, String addNotes, String incomingNotes, String incomingDates, String addRefId, String approvedAdminBy, String approvedAdminDate, String outDoc, String outClinic, String outDiagnosis, String extendPatLeave, String printCount, String cancelReason, String deptCode) {
            this.id = id;
            this.sysCode = sysCode;
            this.staffId = staffId;
            this.leaveType = leaveType;
            this.leaveExpectStartDate = leaveExpectStartDate;
            this.leaveExpectReturnDate = leaveExpectReturnDate;
            this.enteredByName = enteredByName;
            this.locationName = locationName;
            this.locationNameAr = locationNameAr;
            this.leaveActualLeaveDate = leaveActualLeaveDate;
            this.leaveActualReturnDate = leaveActualReturnDate;
            this.leaveNotes = leaveNotes;
            this.leaveStatus = leaveStatus;
            this.amendBy = amendBy;
            this.amendDate = amendDate;
            this.hosp = hosp;
            this.episodeNo = episodeNo;
            this.episodeType = episodeType;
            this.episodeConsultant = episodeConsultant;
            this.episodeDate = episodeDate;
            this.episodeLocation = episodeLocation;
            this.sickLeaveDayFlag = sickLeaveDayFlag;
            this.followupFlag = followupFlag;
            this.referralFlag = referralFlag;
            this.reqApprovalFlag = reqApprovalFlag;
            this.untreatedFlag = untreatedFlag;
            this.disabilityFlag = disabilityFlag;
            this.treatingConsultant = treatingConsultant;
            this.treatingDate = treatingDate;
            this.approvingConsultant = approvingConsultant;
            this.approvingDate = approvingDate;
            this.sickLeaveDay = sickLeaveDay;
            this.referralReason = referralReason;
            this.otherRecFlag = otherRecFlag;
            this.otherRecommend = otherRecommend;
            this.patid = patid;
            this.mrPatDocId = mrPatDocId;
            this.admitDate = admitDate;
            this.dischDate = dischDate;
            this.noVerifiers = noVerifiers;
            this.verifiedBy1 = verifiedBy1;
            this.verifiedBy1Date = verifiedBy1Date;
            this.verifiedBy2 = verifiedBy2;
            this.verifiedBy2Date = verifiedBy2Date;
            this.ver1Flag = ver1Flag;
            this.ver2Flag = ver2Flag;
            this.ver1AmendBy = ver1AmendBy;
            this.ver2AmendBy = ver2AmendBy;
            this.addAmendBy = addAmendBy;
            this.addAmendDate = addAmendDate;
            this.addNotes = addNotes;
            this.incomingNotes = incomingNotes;
            this.incomingDates = incomingDates;
            this.addRefId = addRefId;
            this.approvedAdminBy = approvedAdminBy;
            this.approvedAdminDate = approvedAdminDate;
            this.outDoc = outDoc;
            this.outClinic = outClinic;
            this.outDiagnosis = outDiagnosis;
            this.extendPatLeave = extendPatLeave;
            this.printCount = printCount;
            this.cancelReason = cancelReason;
            this.deptCode = deptCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSysCode() {
            return sysCode;
        }

        public void setSysCode(String sysCode) {
            this.sysCode = sysCode;
        }

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(String leaveType) {
            this.leaveType = leaveType;
        }

        public String getLeaveExpectStartDate() {
            return leaveExpectStartDate;
        }

        public void setLeaveExpectStartDate(String leaveExpectStartDate) {
            this.leaveExpectStartDate = leaveExpectStartDate;
        }

        public String getLeaveExpectReturnDate() {
            return leaveExpectReturnDate;
        }

        public void setLeaveExpectReturnDate(String leaveExpectReturnDate) {
            this.leaveExpectReturnDate = leaveExpectReturnDate;
        }

        public String getEnteredByName() {
            return enteredByName;
        }

        public void setEnteredByName(String enteredByName) {
            this.enteredByName = enteredByName;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLocationNameAr() {
            return locationNameAr;
        }

        public void setLocationNameAr(String locationNameAr) {
            this.locationNameAr = locationNameAr;
        }

        public String getLeaveActualLeaveDate() {
            return leaveActualLeaveDate;
        }

        public void setLeaveActualLeaveDate(String leaveActualLeaveDate) {
            this.leaveActualLeaveDate = leaveActualLeaveDate;
        }

        public String getLeaveActualReturnDate() {
            return leaveActualReturnDate;
        }

        public void setLeaveActualReturnDate(String leaveActualReturnDate) {
            this.leaveActualReturnDate = leaveActualReturnDate;
        }

        public String getLeaveNotes() {
            return leaveNotes;
        }

        public void setLeaveNotes(String leaveNotes) {
            this.leaveNotes = leaveNotes;
        }

        public String getLeaveStatus() {
            return leaveStatus;
        }

        public void setLeaveStatus(String leaveStatus) {
            this.leaveStatus = leaveStatus;
        }

        public String getAmendBy() {
            return amendBy;
        }

        public void setAmendBy(String amendBy) {
            this.amendBy = amendBy;
        }

        public String getAmendDate() {
            return amendDate;
        }

        public void setAmendDate(String amendDate) {
            this.amendDate = amendDate;
        }

        public int getHosp() {
            return hosp;
        }

        public void setHosp(int hosp) {
            this.hosp = hosp;
        }

        public int getEpisodeNo() {
            return episodeNo;
        }

        public void setEpisodeNo(int episodeNo) {
            this.episodeNo = episodeNo;
        }

        public String getEpisodeType() {
            return episodeType;
        }

        public void setEpisodeType(String episodeType) {
            this.episodeType = episodeType;
        }

        public String getEpisodeConsultant() {
            return episodeConsultant;
        }

        public void setEpisodeConsultant(String episodeConsultant) {
            this.episodeConsultant = episodeConsultant;
        }

        public String getEpisodeDate() {
            return episodeDate;
        }

        public void setEpisodeDate(String episodeDate) {
            this.episodeDate = episodeDate;
        }

        public String getEpisodeLocation() {
            return episodeLocation;
        }

        public void setEpisodeLocation(String episodeLocation) {
            this.episodeLocation = episodeLocation;
        }

        public String getSickLeaveDayFlag() {
            return sickLeaveDayFlag;
        }

        public void setSickLeaveDayFlag(String sickLeaveDayFlag) {
            this.sickLeaveDayFlag = sickLeaveDayFlag;
        }

        public String getFollowupFlag() {
            return followupFlag;
        }

        public void setFollowupFlag(String followupFlag) {
            this.followupFlag = followupFlag;
        }

        public String getReferralFlag() {
            return referralFlag;
        }

        public void setReferralFlag(String referralFlag) {
            this.referralFlag = referralFlag;
        }

        public String getReqApprovalFlag() {
            return reqApprovalFlag;
        }

        public void setReqApprovalFlag(String reqApprovalFlag) {
            this.reqApprovalFlag = reqApprovalFlag;
        }

        public String getUntreatedFlag() {
            return untreatedFlag;
        }

        public void setUntreatedFlag(String untreatedFlag) {
            this.untreatedFlag = untreatedFlag;
        }

        public String getDisabilityFlag() {
            return disabilityFlag;
        }

        public void setDisabilityFlag(String disabilityFlag) {
            this.disabilityFlag = disabilityFlag;
        }

        public String getTreatingConsultant() {
            return treatingConsultant;
        }

        public void setTreatingConsultant(String treatingConsultant) {
            this.treatingConsultant = treatingConsultant;
        }

        public String getTreatingDate() {
            return treatingDate;
        }

        public void setTreatingDate(String treatingDate) {
            this.treatingDate = treatingDate;
        }

        public String getApprovingConsultant() {
            return approvingConsultant;
        }

        public void setApprovingConsultant(String approvingConsultant) {
            this.approvingConsultant = approvingConsultant;
        }

        public String getApprovingDate() {
            return approvingDate;
        }

        public void setApprovingDate(String approvingDate) {
            this.approvingDate = approvingDate;
        }

        public int getSickLeaveDay() {
            return sickLeaveDay;
        }

        public void setSickLeaveDay(int sickLeaveDay) {
            this.sickLeaveDay = sickLeaveDay;
        }

        public String getReferralReason() {
            return referralReason;
        }

        public void setReferralReason(String referralReason) {
            this.referralReason = referralReason;
        }

        public String getOtherRecFlag() {
            return otherRecFlag;
        }

        public void setOtherRecFlag(String otherRecFlag) {
            this.otherRecFlag = otherRecFlag;
        }

        public String getOtherRecommend() {
            return otherRecommend;
        }

        public void setOtherRecommend(String otherRecommend) {
            this.otherRecommend = otherRecommend;
        }

        public String getPatid() {
            return patid;
        }

        public void setPatid(String patid) {
            this.patid = patid;
        }

        public String getMrPatDocId() {
            return mrPatDocId;
        }

        public void setMrPatDocId(String mrPatDocId) {
            this.mrPatDocId = mrPatDocId;
        }

        public String getAdmitDate() {
            return admitDate;
        }

        public void setAdmitDate(String admitDate) {
            this.admitDate = admitDate;
        }

        public String getDischDate() {
            return dischDate;
        }

        public void setDischDate(String dischDate) {
            this.dischDate = dischDate;
        }

        public String getNoVerifiers() {
            return noVerifiers;
        }

        public void setNoVerifiers(String noVerifiers) {
            this.noVerifiers = noVerifiers;
        }

        public String getVerifiedBy1() {
            return verifiedBy1;
        }

        public void setVerifiedBy1(String verifiedBy1) {
            this.verifiedBy1 = verifiedBy1;
        }

        public String getVerifiedBy1Date() {
            return verifiedBy1Date;
        }

        public void setVerifiedBy1Date(String verifiedBy1Date) {
            this.verifiedBy1Date = verifiedBy1Date;
        }

        public String getVerifiedBy2() {
            return verifiedBy2;
        }

        public void setVerifiedBy2(String verifiedBy2) {
            this.verifiedBy2 = verifiedBy2;
        }

        public String getVerifiedBy2Date() {
            return verifiedBy2Date;
        }

        public void setVerifiedBy2Date(String verifiedBy2Date) {
            this.verifiedBy2Date = verifiedBy2Date;
        }

        public String getVer1Flag() {
            return ver1Flag;
        }

        public void setVer1Flag(String ver1Flag) {
            this.ver1Flag = ver1Flag;
        }

        public String getVer2Flag() {
            return ver2Flag;
        }

        public void setVer2Flag(String ver2Flag) {
            this.ver2Flag = ver2Flag;
        }

        public String getVer1AmendBy() {
            return ver1AmendBy;
        }

        public void setVer1AmendBy(String ver1AmendBy) {
            this.ver1AmendBy = ver1AmendBy;
        }

        public String getVer2AmendBy() {
            return ver2AmendBy;
        }

        public void setVer2AmendBy(String ver2AmendBy) {
            this.ver2AmendBy = ver2AmendBy;
        }

        public String getAddAmendBy() {
            return addAmendBy;
        }

        public void setAddAmendBy(String addAmendBy) {
            this.addAmendBy = addAmendBy;
        }

        public String getAddAmendDate() {
            return addAmendDate;
        }

        public void setAddAmendDate(String addAmendDate) {
            this.addAmendDate = addAmendDate;
        }

        public String getAddNotes() {
            return addNotes;
        }

        public void setAddNotes(String addNotes) {
            this.addNotes = addNotes;
        }

        public String getIncomingNotes() {
            return incomingNotes;
        }

        public void setIncomingNotes(String incomingNotes) {
            this.incomingNotes = incomingNotes;
        }

        public String getIncomingDates() {
            return incomingDates;
        }

        public void setIncomingDates(String incomingDates) {
            this.incomingDates = incomingDates;
        }

        public String getAddRefId() {
            return addRefId;
        }

        public void setAddRefId(String addRefId) {
            this.addRefId = addRefId;
        }

        public String getApprovedAdminBy() {
            return approvedAdminBy;
        }

        public void setApprovedAdminBy(String approvedAdminBy) {
            this.approvedAdminBy = approvedAdminBy;
        }

        public String getApprovedAdminDate() {
            return approvedAdminDate;
        }

        public void setApprovedAdminDate(String approvedAdminDate) {
            this.approvedAdminDate = approvedAdminDate;
        }

        public String getOutDoc() {
            return outDoc;
        }

        public void setOutDoc(String outDoc) {
            this.outDoc = outDoc;
        }

        public String getOutClinic() {
            return outClinic;
        }

        public void setOutClinic(String outClinic) {
            this.outClinic = outClinic;
        }

        public String getOutDiagnosis() {
            return outDiagnosis;
        }

        public void setOutDiagnosis(String outDiagnosis) {
            this.outDiagnosis = outDiagnosis;
        }

        public String getExtendPatLeave() {
            return extendPatLeave;
        }

        public void setExtendPatLeave(String extendPatLeave) {
            this.extendPatLeave = extendPatLeave;
        }

        public String getPrintCount() {
            return printCount;
        }

        public void setPrintCount(String printCount) {
            this.printCount = printCount;
        }

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }
    }

    public class Pageable{
        public Sort sort;
        public int offset;
        public int pageNumber;
        public int pageSize;
        public boolean paged;
        public boolean unpaged;

        public Pageable(Sort sort, int offset, int pageNumber, int pageSize, boolean paged, boolean unpaged) {
            this.sort = sort;
            this.offset = offset;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.paged = paged;
            this.unpaged = unpaged;
        }

        public Pageable() {
        }

        public Sort getSort() {
            return sort;
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public boolean isPaged() {
            return paged;
        }

        public void setPaged(boolean paged) {
            this.paged = paged;
        }

        public boolean isUnpaged() {
            return unpaged;
        }

        public void setUnpaged(boolean unpaged) {
            this.unpaged = unpaged;
        }
    }

}
