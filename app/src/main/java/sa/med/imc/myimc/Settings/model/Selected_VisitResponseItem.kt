package sa.med.imc.myimc.Settings.model

data class Selected_VisitResponseItem(
    var apptDate: String,
    var consultant: String,
    var consultantAr: String,
    var episodeKey: String,
    var episodeNo: String,
    var episodeType: String,
    var patid: String
)