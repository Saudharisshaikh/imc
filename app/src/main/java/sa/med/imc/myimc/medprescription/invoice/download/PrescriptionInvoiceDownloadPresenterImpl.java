package sa.med.imc.myimc.medprescription.invoice.download;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ServiceGenerator;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Network.WebService;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.medprescription.invoice.model.RequestPrescriptionInvoice;

public class PrescriptionInvoiceDownloadPresenterImpl implements PrescriptionInvoiceDownloadPresenter{
    private PrescriptionInvoiceDownloadView view;
    private Uri uri;
    private int selectedOrderId=-1;
    private final int CODE = 90;

    public PrescriptionInvoiceDownloadPresenterImpl(PrescriptionInvoiceDownloadView view) {
        this.view = view;
    }

    @Override
    public void createFile(Fragment fragment) {
        Common.createFile(fragment,System.currentTimeMillis()+".pdf",CODE);
    }

    @Override
    public void setUri(Uri uri) {
        this.uri=uri;
    }

    @Override
    public Uri getUri() {
        return uri;
    }

    @Override
    public void setSelectedOrderId(int orderId) {
        this.selectedOrderId = orderId;
    }

    @Override
    public int getSelectedOrderId() {
        return selectedOrderId;
    }

    @Override
    public void onActivityResult(FragmentActivity fragmentActivity, Bundle bundle, int requestCode, int resultCode, Intent data) {
        if (requestCode==CODE && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null){
            setUri(data.getData());
            downloadInvoice((AppCompatActivity) fragmentActivity,
                    bundle.getString(Constants.Bundle.episodeType,""),
                    bundle.getString(Constants.Bundle.rxNo,""),
                    bundle.getInt(Constants.Bundle.episodeNo,-1),
                    bundle.getInt(Constants.Bundle.orderNo,-1),
                    getSelectedOrderId());
        }
        else{
            view.onCancelFile();
        }
    }

    @Override
    public void downloadInvoice(AppCompatActivity activity,String episodeType, String rxNo, Integer episodeNo, Integer orderNo, Integer phOrderId) {
        view.showLoading(1);
        int hosp = SharedPreferencesUtils.getInstance().getValue(Constants.SELECTED_HOSPITAL,-1);
        String patId = SharedPreferencesUtils.getInstance().getValue(Constants.KEY_MRN,"");
        RequestPrescriptionInvoice requestPrescriptionInvoice = new RequestPrescriptionInvoice();
        requestPrescriptionInvoice.setEpisodeType(episodeType);
        requestPrescriptionInvoice.setHosp(String.valueOf(hosp));
        requestPrescriptionInvoice.setEpisodeNo(episodeNo);
        requestPrescriptionInvoice.setOrderNo(orderNo);
        requestPrescriptionInvoice.setRxNo(rxNo);
        requestPrescriptionInvoice.setPatId(String.valueOf(patId));
        requestPrescriptionInvoice.setPhOrderId(phOrderId);
        Call<ResponseBody> call = ServiceGenerator.createPharmacyService(WebService.class,SharedPreferencesUtils.getInstance().getValue(Constants.KEY_ACCESS_TOKEN,""))
                .generatePrescriptionInvoice(requestPrescriptionInvoice);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code()==200 && response.body()!=null){
                    view.onDownload(response.body());
                    onDownload(activity,response.body());
                }
                else {
                    view.hideLoading(1);
                    view.onFail(response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                view.hideLoading(1);
                view.onFail(t.getMessage());
            }
        });
    }

    private void onDownload(AppCompatActivity context,ResponseBody responseBody){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(()->{
            try {
                if (context == null || getUri() == null){
                    return;
                }
                OutputStream out = context.getContentResolver().openOutputStream(getUri());
                InputStream in = new BufferedInputStream(responseBody.byteStream(), 8192);
                byte[] stream = new byte[1024];
                while (in.read(stream)!=-1){
                    out.write(stream);
                }
                out.close();
                out.flush();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            context.runOnUiThread(()->{
                executor.shutdownNow();
                view.hideLoading(1);
                showDialog(context);
            });
        });
    }

    @Override
    public void showDialog(FragmentActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(activity.getResources().getString(R.string.success));
        builder.setMessage(activity.getResources().getString(R.string.txt_success_download_receipt));
        builder.setPositiveButton(activity.getResources().getString(R.string.open), (dialog, which) -> {
            dialog.dismiss();
            view.onOpenFile();
            Common.openFile(activity,uri);
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.later), (dialog, which) -> {
            dialog.dismiss();
            view.onCancelFile();
        });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(arg0 -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#CCCBCB"));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#005497"));
            try {
                Typeface typeface = ResourcesCompat.getFont(activity, R.font.font_app);
                ((TextView) alert.findViewById(android.R.id.message)).setTypeface(typeface);

                if (SharedPreferencesUtils.getInstance(activity).getValue(Constants.KEY_LANGUAGE, "").equalsIgnoreCase(Constants.ARABIC)) {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // set title and message direction to RTL
                } else {
                    alert.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR); // set title and message direction to RTL
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        alert.show();
    }
}
