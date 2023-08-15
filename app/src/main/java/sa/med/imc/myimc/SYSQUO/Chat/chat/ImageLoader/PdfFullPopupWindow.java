package sa.med.imc.myimc.SYSQUO.Chat.chat.ImageLoader;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.palette.graphics.Palette;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.R;

public class PdfFullPopupWindow extends PopupWindow implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener, PickiTCallbacks {

    View view;
    Context mContext;
    PDFView pdfView;
    ProgressBar loading;
    TextView textPdfFileName;
    ViewGroup parent;
    String pdfFileName = null;
    Integer pageNumber = 0;
    private static final String TAG = "PDF View";
    private static PdfFullPopupWindow instance = null;
    Activity mActivity;
    PickiT pickiT;
    View view1;

    public PdfFullPopupWindow(Context ctx, int layout, View v, String fileName, Activity activity) {
        super(((LayoutInflater) ctx.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate( R.layout.sysquo_dialog_pdf_view, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= 21) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.mActivity = activity;
        this.view = getContentView();
        this.pdfFileName = fileName;
        this.view1 = v;
        pickiT = new PickiT(mContext, this, mActivity);
        ImageView closeButton = (ImageView) this.view.findViewById(R.id.iv_back);
        setOutsideTouchable(true);

        setFocusable(true);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                pickiT.deleteTemporaryFile(mContext);
                dismiss();
            }
        });
        //---------Begin customising this popup--------------------

        pdfView = (PDFView) view.findViewById(R.id.PdfView);
        textPdfFileName = (TextView) view.findViewById(R.id.textPdfFileName);
        loading = (ProgressBar) view.findViewById(R.id.loading);

        parent = (ViewGroup) pdfView.getParent();
        // ImageUtils.setZoomable(imageView);
        //----------------------------
        if (fileName != null) {
            try {
                textPdfFileName.setText(pdfFileName);
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download/" + pdfFileName);
                String absoluteFilePath = file.getAbsolutePath();
//                Uri uri = Uri.parse("content://" + "sa.med.imc.myimc" + "/" + absoluteFilePath);
                Uri uri = Uri.fromFile(file);
                pickiT.getPath(uri, Build.VERSION.SDK_INT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //------------------------------

    }

    public void onPalette(Palette palette) {
        if (null != palette) {
            ViewGroup parent = (ViewGroup) pdfView.getParent().getParent();
            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        if (wasSuccessful) {
            //  Set returned path to TextView
            try {
                File file = new File(path);
                pdfView.fromFile(file)
                        .defaultPage(pageNumber)
                        .onPageChange(this)
                        .enableAnnotationRendering(true)
                        .onLoad(this)
                        .scrollHandle(new DefaultScrollHandle(mContext))
                        .spacing(10) // in dp
                        .onPageError(this)
                        .load();

                showAtLocation(view1, Gravity.CENTER, 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            Common.makeToast(this, "Error, please see the log..");
        }
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {

    }
}
