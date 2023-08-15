package sa.med.imc.myimc.medprescription.invoice.list;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public interface PrescriptionInvoicePresenter {

    void getInvoiceList(String episodeType,String rxNo, Integer episodeNo, Integer orderNo);

}
