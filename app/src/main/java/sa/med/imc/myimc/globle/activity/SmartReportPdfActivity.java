package sa.med.imc.myimc.globle.activity;

import static sa.med.imc.myimc.globle.UrlWithURLDecoder.generate_smart_pdf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;

import sa.med.imc.myimc.R;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.interfaces.PdfListner;

public class SmartReportPdfActivity extends AppCompatActivity {
    PDFView pdfView;
    byte[] bytes;
    String mrNumber;
    String orderNumber;

    TextView toolbar_title;
    ImageView iv_more, iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_report_pdf);
        pdfView = findViewById(R.id.pdfView);
        toolbar_title = findViewById(R.id.toolbar_title);
        iv_back = findViewById(R.id.iv_back);
        iv_more = findViewById(R.id.iv_more);


        mrNumber = getIntent().getStringExtra("mrNumber");
        orderNumber = getIntent().getStringExtra("orderNumber");

        toolbar_title.setText("Smart Report: "+orderNumber);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new MyhttpMethod(this).loadSmartPdf(generate_smart_pdf, orderNumber, mrNumber, new PdfListner() {


            @Override
            public void onSuccess(String pdf_base64) {
                Log.e("abcd", "onSuccess");
                bytes = Base64.decode(pdf_base64, Base64.DEFAULT);
                pdfView.fromBytes(bytes).load();

            }

            @Override
            public void onFail() {
                Log.e("abcd", "onFail");
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(SmartReportPdfActivity.this).inflate(R.layout.simple_alert, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(SmartReportPdfActivity.this);


                TextView contentTextView = dialogView.findViewById(R.id.content);
                TextView buttonOk = dialogView.findViewById(R.id.buttonOk);
                contentTextView.setText(getString(R.string.result_not_published_yet));
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                buttonOk.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    finish();
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

        iv_more.setOnClickListener(view -> {
            showPopUpMenu();
        });

    }

    File saveToLocal() {
        File document = null;
        File path=null;
        try {
            /* file_byte is yous json string*/
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            document = new File(path, toolbar_title.getText().toString() + ".pdf");

            if (document.exists()) {
                document.delete();
            }

            FileOutputStream fos = new FileOutputStream(document.getPath());
            fos.write(bytes);
            fos.close();
            Toast.makeText(this, "Pdf Successfully Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("abcd", "error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SmartReportPdfActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return path;
    }

    // Show pop up of filter value list.
    private void showPopUpMenu() {
        View menuItemView = findViewById(R.id.iv_more);
        PopupMenu popup = new PopupMenu(this, menuItemView);

        MenuInflater inflate = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.print:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (checkPermissionForGallery()) {
                                open_File();
                            }
                        } else {
                            Toast.makeText(SmartReportPdfActivity.this, getString(R.string.no_service_print), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.download:
                        if (checkPermissionForGallery()) {
                            saveToLocal();
                        }
                        break;
                }
                return true;
            }
        });
        inflate.inflate(R.menu.option_menu, popup.getMenu());
        popup.show();

    }

    // open file with print preview to print job
    public void open_File() {
        File file = saveToLocal();

        // Get URI and MIME type of file
        Uri uri = Uri.fromFile(file).normalizeScheme();
        String mime = get_mime_type(uri.toString());

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setType(mime);
        startActivity(Intent.createChooser(intent, "Open file with"));
    }
    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveToLocal();
        } else {
            checkPermissionForGallery();
        }


    }

    private boolean checkPermissionForGallery() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            return false;
        }
    }

}