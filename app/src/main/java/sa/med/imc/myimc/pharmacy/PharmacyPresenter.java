package sa.med.imc.myimc.pharmacy;

import sa.med.imc.myimc.pharmacy.model.PharmacyItem;

public interface PharmacyPresenter {
    void callPrescriptionDetails(String rxNo,String episodeNo);
    void validate(PharmacyItem item);
}

