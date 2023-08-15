package sa.med.imc.myimc.pharmacy;

import java.util.List;

import sa.med.imc.myimc.Utils.Loading;
import sa.med.imc.myimc.pharmacy.model.PharmacyItem;
import sa.med.imc.myimc.pharmacy.model.PharmacyResponse;
import sa.med.imc.myimc.pharmacy.model.PharmacyTelrResponse;

public interface PharmacyView extends Loading {
   default void onDetailResponse(List<PharmacyItem> response){

   }
   default void onPaymentValidation(PharmacyTelrResponse response){

   }
}
