package sa.med.imc.myimc.medprescription.invoice.download;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface PrescriptionInvoiceDownloadPresenter {
    void createFile(Fragment fragment);

    void downloadInvoice(AppCompatActivity context,String episodeType, String rxNo, Integer episodeNo, Integer orderNo, Integer phOrderId);

    void setUri(Uri uri);

    Uri getUri();

    void setSelectedOrderId (int orderId);

    int getSelectedOrderId();

    void showDialog(FragmentActivity activity);

    default void onActivityResult(FragmentActivity fragmentActivity, Bundle bundle , int requestCode, int resultCode, Intent data){

    }
}
